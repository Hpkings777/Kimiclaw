package com.termux.terminal

import java.io.FileDescriptor
import java.io.FileInputStream
import java.io.FileOutputStream

class TerminalSessionIOTask(
    private val session: TerminalSession,
    name: String,
    private val fd: FileDescriptor,
    private val mode: Int
) : Thread(name) {

    override fun run() {
        when (mode) {
            0 -> {
                val buf = ByteArray(4096)
                try {
                    FileInputStream(fd).use { input ->
                        while (!isInterrupted) {
                            val n = input.read(buf)
                            if (n <= 0) break
                            session.outputQueue.write(buf, n)
                        }
                    }
                } catch (_: Exception) {}
            }
            1 -> {
                val buf = ByteArray(4096)
                try {
                    FileOutputStream(fd).use { output ->
                        while (!isInterrupted) {
                            val n = session.inputQueue.read(buf, true)
                            if (n <= 0) break
                            output.write(buf, 0, n)
                            output.flush()
                        }
                    }
                } catch (_: Exception) {}
            }
        }
    }
}
