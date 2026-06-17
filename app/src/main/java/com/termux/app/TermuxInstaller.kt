package com.termux.app

import android.app.Activity
import android.app.ProgressDialog
import android.system.Os
import android.util.Log
import com.moonshot.kimiclaw.R
import java.io.BufferedReader
import java.io.ByteArrayInputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStreamReader
import java.util.zip.ZipInputStream

abstract class TermuxInstaller {

    companion object {
        private const val TAG = "TermuxInstaller"
        const val PREFIX_PATH = "/data/data/com.moonshot.kimiclaw/files/usr"
        const val STAGING_PATH = "/data/data/com.moonshot.kimiclaw/files/usr-staging"

        fun logError(msg: String) {
            Log.e(TAG, "KimiClaw Bootstrap Error: $msg")
        }

        fun setupBootstrap(activity: Activity, onComplete: Runnable) {
            val prefixDir = File(PREFIX_PATH)
            if (prefixDir.exists() && prefixDir.isDirectory) {
                val binDir = File(prefixDir, "bin")
                if (binDir.exists() && binDir.isDirectory) {
                    val bashFile = File(prefixDir, "bin/bash")
                    if (bashFile.exists()) {
                        Log.i(TAG, "Bootstrap already installed, skipping setup")
                        onComplete.run()
                        return
                    } else {
                        Log.w(TAG, "Bootstrap not fully installed: bash binary missing")
                    }
                } else {
                    Log.w(TAG, "Bootstrap not fully installed: bin directory missing")
                }
            } else {
                Log.w(TAG, "Bootstrap not installed: $PREFIX_PATH does not exist")
            }

            val progress = ProgressDialog.show(activity, null,
                activity.getString(R.string.bootstrap_installer_body), true, false)
            BootstrapInstallThread(activity, onComplete, progress).start()
        }

        fun handleError(activity: Activity, onComplete: Runnable, message: String) {
            logError(message)
            activity.runOnUiThread {
                onComplete.run()
            }
        }

        external fun getZip(): ByteArray
    }
}

class BootstrapInstallThread(
    private val activity: Activity,
    private val onComplete: Runnable,
    private val progress: ProgressDialog
) : Thread() {

    override fun run() {
        try {
            Log.i("TermuxInstaller", "Installing KimiClaw bootstrap packages.")

            val stagingDir = File(TermuxInstaller.STAGING_PATH)
            val prefixDir = File(TermuxInstaller.PREFIX_PATH)

            stagingDir.mkdirs()
            prefixDir.mkdirs()

            Log.i("TermuxInstaller", "Extracting bootstrap zip to prefix staging directory \"$stagingDir\".")

            System.loadLibrary("termux-bootstrap")
            val zipData = TermuxInstaller.getZip()

            val symlinks = mutableListOf<Pair<String, String>>()
            val buf = ByteArray(8096)

            ZipInputStream(ByteArrayInputStream(zipData)).use { zis ->
                while (true) {
                    val entry = zis.nextEntry ?: break

                    if (entry.name == "SYMLINKS.txt") {
                        BufferedReader(InputStreamReader(zis)).use { reader ->
                            var line = reader.readLine()
                            while (line != null) {
                                val parts = line.split("\u2190") // ←
                                if (parts.size == 2) {
                                    val target = parts[0]
                                    val linkPath = "$stagingDir/${parts[1]}"
                                    symlinks.add(target to linkPath)
                                    File(linkPath).parentFile?.mkdirs()
                                }
                                line = reader.readLine()
                            }
                        }
                        continue
                    }

                    val file = File(stagingDir, entry.name)
                    if (entry.isDirectory) {
                        file.mkdirs()
                    } else {
                        file.parentFile?.mkdirs()
                        FileOutputStream(file).use { fos ->
                            var n = zis.read(buf)
                            while (n != -1) {
                                fos.write(buf, 0, n)
                                n = zis.read(buf)
                            }
                        }

                        if (entry.name.startsWith("bin/") ||
                            entry.name.startsWith("libexec") ||
                            entry.name.startsWith("lib/apt/apt-helper") ||
                            entry.name.startsWith("lib/apt/methods")
                        ) {
                            file.setExecutable(true, false)
                        }
                    }
                }
            }

            for ((target, linkPath) in symlinks) {
                try {
                    Os.symlink(target, linkPath)
                } catch (e: Exception) {
                    Log.w("TermuxInstaller", "Failed to create symlink: $target -> $linkPath", e)
                }
            }

            Log.i("TermuxInstaller", "Moving termux prefix staging to prefix directory.")
            if (!stagingDir.renameTo(prefixDir)) {
                throw RuntimeException("Moving termux prefix staging to prefix directory failed")
            }

            Log.i("TermuxInstaller", "Bootstrap packages installed successfully.")
            activity.runOnUiThread(onComplete)

        } catch (e: Exception) {
            TermuxInstaller.handleError(activity, onComplete, e.message ?: "Unknown error")
        } finally {
            activity.runOnUiThread {
                try { progress.dismiss() } catch (_: Exception) {}
            }
        }
    }
}
