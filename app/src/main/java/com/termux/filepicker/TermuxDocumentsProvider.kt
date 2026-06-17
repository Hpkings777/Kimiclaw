package com.termux.filepicker

import android.content.res.AssetFileDescriptor
import android.database.Cursor
import android.database.MatrixCursor
import android.graphics.Point
import android.os.CancellationSignal
import android.os.ParcelFileDescriptor
import android.provider.DocumentsProvider
import android.webkit.MimeTypeMap
import com.moonshot.kimiclaw.R
import java.io.File
import java.io.FileNotFoundException
import java.util.LinkedList

class TermuxDocumentsProvider : DocumentsProvider() {

    companion object {
        private const val ROOT_ID = "root_openclaw_workspace"
        private const val ROOT_DIR = "/data/data/com.moonshot.kimiclaw/files/usr/var/lib/proot-distro/installed-rootfs/debian/root/.openclaw/workspace"
        private val ROOTS = mapOf(
            "root_openclaw" to File("/data/data/com.moonshot.kimiclaw/files/usr/var/lib/proot-distro/installed-rootfs/debian/root/.openclaw"),
            ROOT_ID to File(ROOT_DIR)
        )
        private val DEFAULT_ROOT_PROJECTION = arrayOf(
            "root_id", "mime_types", "flags", "icon", "title", "summary",
            "document_id", "available_bytes"
        )
        private val DEFAULT_DOCUMENT_PROJECTION = arrayOf(
            "document_id", "mime_type", "_display_name", "last_modified",
            "flags", "_size"
        )
    }

    private fun toDocumentId(file: File): String {
        return file.absolutePath
    }

    private fun toFile(docId: String): File {
        val file = File(docId)
        if (!file.exists()) throw FileNotFoundException("$docId not found")
        return file
    }

    private fun getMimeType(file: File): String {
        if (file.isDirectory) return "vnd.android.document/directory"
        val ext = file.extension.lowercase()
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(ext) ?: "application/octet-stream"
    }

    private fun addDocumentRow(cursor: MatrixCursor, docId: String?, file: File?) {
        val id = docId ?: toDocumentId(file!!)
        val f = file ?: toFile(docId!!)
        val mime = getMimeType(f)

        var flags = 0
        if (f.isDirectory) {
            if (f.canWrite()) flags = flags or 8
        } else if (f.canWrite()) {
            flags = flags or 2
        }
        if (f.parentFile?.canWrite() == true) flags = flags or 4
        if (mime.startsWith("image/")) flags = flags or 1

        var displayName = f.name
        if (displayName.startsWith(".")) displayName = displayName.substring(1)

        cursor.newRow().apply {
            add("document_id", id)
            add("_display_name", displayName)
            add("_size", f.length())
            add("mime_type", mime)
            add("last_modified", f.lastModified())
            add("flags", flags)
            add("icon", R.mipmap.ic_launcher)
        }
    }

    override fun onCreate(): Boolean = true

    override fun queryRoots(projection: Array<String>?): Cursor {
        val cols = projection ?: DEFAULT_ROOT_PROJECTION
        val cursor = MatrixCursor(cols)

        val workspaceDir = File(ROOT_DIR)
        if (workspaceDir.exists()) {
            cursor.newRow().apply {
                add("root_id", ROOT_ID)
                add("document_id", toDocumentId(workspaceDir))
                add("summary", "Kimi Claw")
                add("flags", 25)
                add("title", "OpenClaw Workspace")
                add("mime_types", "*/*")
                add("available_bytes", workspaceDir.freeSpace)
                add("icon", R.mipmap.ic_launcher)
            }
        }
        return cursor
    }

    override fun queryDocument(documentId: String, projection: Array<String>?): Cursor {
        val cols = projection ?: DEFAULT_DOCUMENT_PROJECTION
        val cursor = MatrixCursor(cols)
        addDocumentRow(cursor, documentId, null)
        return cursor
    }

    override fun queryChildDocuments(parentDocumentId: String, projection: Array<String>?, sortOrder: String?): Cursor {
        val cols = projection ?: DEFAULT_DOCUMENT_PROJECTION
        val cursor = MatrixCursor(cols)
        val dir = toFile(parentDocumentId)
        dir.listFiles()?.forEach { file ->
            if (file.exists() && file.canRead()) {
                addDocumentRow(cursor, null, file)
            }
        }
        return cursor
    }

    override fun querySearchDocuments(rootId: String, query: String, projection: Array<String>?): Cursor {
        val cols = projection ?: DEFAULT_DOCUMENT_PROJECTION
        val cursor = MatrixCursor(cols)

        val rootDir = ROOTS[rootId] ?: toFile(rootId)
        if (!rootDir.exists()) return cursor

        val queue = LinkedList<File>()
        queue.add(rootDir)
        val canonicalRoot = rootDir.canonicalPath

        while (queue.isNotEmpty() && cursor.count < 50) {
            val file = queue.removeFirst()
            if (!file.exists() || !file.canRead()) continue
            try {
                if (!file.canonicalPath.startsWith(canonicalRoot)) continue
            } catch (_: Exception) { continue }

            if (file.isDirectory) {
                file.listFiles()?.forEach { queue.add(it) }
            } else if (file.name.lowercase().contains(query.lowercase())) {
                addDocumentRow(cursor, null, file)
            }
        }
        return cursor
    }

    override fun getDocumentType(documentId: String): String {
        return getMimeType(toFile(documentId))
    }

    override fun createDocument(parentDocumentId: String, mimeType: String, displayName: String): String {
        val parent = toFile(parentDocumentId)
        var file = File(parent, displayName)
        var counter = 2
        while (file.exists()) {
            file = File(parent, "${displayName} ($counter)")
            counter++
        }
        return if (mimeType == "vnd.android.document/directory") {
            if (file.mkdir()) file.path
            else throw FileNotFoundException("Failed to create directory: ${file.path}")
        } else {
            if (file.createNewFile()) file.path
            else throw FileNotFoundException("Failed to create file: ${file.path}")
        }
    }

    override fun deleteDocument(documentId: String) {
        if (!toFile(documentId).delete()) {
            throw FileNotFoundException("Failed to delete document: $documentId")
        }
    }

    override fun isChildDocument(parentDocumentId: String, documentId: String): Boolean {
        return documentId.startsWith(parentDocumentId)
    }

    override fun openDocument(documentId: String, mode: String, signal: CancellationSignal?): ParcelFileDescriptor {
        return ParcelFileDescriptor.open(toFile(documentId), ParcelFileDescriptor.parseMode(mode))
    }

    override fun openDocumentThumbnail(documentId: String, sizeHint: Point, signal: CancellationSignal?): AssetFileDescriptor {
        val file = toFile(documentId)
        return AssetFileDescriptor(
            ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY),
            0, file.length()
        )
    }
}
