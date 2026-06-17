package com.termux.shared.net.socket.local

import android.util.Log
import com.termux.shared.jni.models.JniResult
import java.io.Closeable
import java.io.IOException
import java.io.OutputStreamWriter

class SocketClient(
    private val config: SocketServerConfig,
    @JvmField var fd: Int,
    @JvmField val peerCred: PeerCred
) : Closeable {

    @JvmField val creationTime: Long = System.currentTimeMillis()
    val inputStream = SocketClientInputStream(this)
    val outputStream = SocketClientOutputStream(this)

    init {
        peerCred.fillPeerCred(LocalSocketManager.appContext)
    }

    fun read(buffer: ByteArray): Int {
        val result = LocalSocketManager.read(config.getSocketName() + " (client)", fd, buffer)
        if (result == null || result.retval != 0) {
            throw IOException("Read failed: ${JniResult.getErrorString(result)}")
        }
        return result.intData
    }

    fun read(buffer: ByteArray, resultHolder: SocketReadResult): Int {
        val result = LocalSocketManager.read(config.getSocketName() + " (client)", fd, buffer)
        return if (result == null || result.retval != 0) {
            throw IOException("Read failed: ${JniResult.getErrorString(result)}")
        } else {
            resultHolder.bytesRead = result.intData
            result.intData
        }
    }

    fun write(data: ByteArray) {
        val result = LocalSocketManager.send(config.getSocketName() + " (client)", fd, data)
        if (result == null || result.retval != 0) {
            throw IOException("Write failed: ${JniResult.getErrorString(result)}")
        }
    }

    fun writeString(text: String) {
        OutputStreamWriter(outputStream).use { writer ->
            writer.write(text)
            writer.flush()
        }
    }

    fun configureTimeouts() {
        LocalSocketManager.setReadTimeout(config.getSocketName() + " (client)", fd, config.getReceiveTimeoutOrElse())
        LocalSocketManager.setSendTimeout(config.getSocketName() + " (client)", fd, config.getSendTimeoutOrElse())
    }

    override fun close() {
        if (fd >= 0) {
            Log.d("LocalClientSocket", "Client socket close for \"${config.title}\" server: ${peerCred.getMinimalString()}")
            val result = LocalSocketManager.closeSocket(config.getSocketName() + " (client)", fd)
            if (result == null || result.retval != 0) {
                throw IOException(JniResult.getErrorString(result))
            }
            fd = -1
        }
    }

    fun closeSilently() {
        try { close() } catch (_: Exception) {}
    }

    fun available(): Int {
        val result = LocalSocketManager.available(config.getSocketName() + " (client)", fd)
        return result?.intData ?: 0
    }

    fun toLogString(): String = buildString {
        appendLine("Client Socket:")
        appendLine("  FD: $fd")
        appendLine("  Creation Time: $creationTime")
        appendLine()
        appendLine(peerCred.getLogString())
    }
}

class SocketReadResult {
    @JvmField var bytesRead: Int = 0
}
