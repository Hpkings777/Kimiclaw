package com.termux.terminal

import android.system.ErrnoException
import android.system.Os
import android.system.OsConstants
import java.io.FileDescriptor
import java.nio.charset.StandardCharsets
import java.util.UUID

class TerminalSession(
    val executable: String,
    val cwd: String,
    val argv: Array<String>?,
    val envp: Array<String>?,
    val desiredRows: Int?,
    val client: TerminalSessionClient?
) {
    val sessionId = UUID.randomUUID().toString()
    val inputQueue = TerminalBuffer()
    val outputQueue = TerminalBuffer()
    val outputHandler = TerminalSessionOutputHandler(this)

    private val encodeBuffer = ByteArray(5)
    var processId: Int = -1
        internal set
    var exitCode: Int = 0
        internal set
    var masterFd: Int = -1
        internal set

    var emulator: TerminalEmulator? = null
        internal set

    val isRunning: Boolean get() = processId != -1

    fun initialize(columns: Int, rows: Int, width: Int, height: Int) {
        emulator = TerminalEmulator(this, columns, rows, 2000)

        val pidArr = IntArray(1)
        val rowsArg = desiredRows ?: rows
        masterFd = JNI.createSubprocess(executable, cwd, argv, envp, pidArr, columns, rowsArg, width, height)
        processId = pidArr[0]

        client?.onSessionStarted(processId, this)

        val sessionFd = FileDescriptor()
        try {
            val fdField = FileDescriptor::class.java.getDeclaredField("descriptor")
            fdField.isAccessible = true
            fdField.set(sessionFd, masterFd)
        } catch (_: NoSuchFieldException) {
            try {
                val fdField = FileDescriptor::class.java.getDeclaredField("fd")
                fdField.isAccessible = true
                fdField.set(sessionFd, masterFd)
            } catch (_: Exception) {
                System.exit(1)
            }
        } catch (_: Exception) {
            System.exit(1)
        }

        TerminalSessionIOTask(this, "TermSessionInputReader[pid=$processId]", sessionFd, 0).start()
        TerminalSessionIOTask(this, "TermSessionOutputWriter[pid=$processId]", sessionFd, 1).start()
        TerminalSessionWaiter(this, "TermSessionWaiter[pid=$processId]").start()
    }

    fun write(input: String) {
        if (input == null || processId <= 0) return
        val bytes = input.toByteArray(StandardCharsets.UTF_8)
        inputQueue.write(bytes, bytes.size)
    }

    fun writeCodePoint(codePoint: Int, ctrl: Boolean) {
        require(codePoint <= 0x10FFFF && (codePoint !in 0xD800..0xDFFF)) {
            "Invalid code point: $codePoint"
        }

        var pos = 0
        if (ctrl) {
            encodeBuffer[0] = 0x1B // ESC
            pos = 1
        }

        pos += when {
            codePoint <= 0x7F -> {
                encodeBuffer[pos] = codePoint.toByte()
                1
            }
            codePoint <= 0x7FF -> {
                encodeBuffer[pos] = (0xC0 or (codePoint shr 6)).toByte()
                encodeBuffer[pos + 1] = (0x80 or (codePoint and 0x3F)).toByte()
                2
            }
            codePoint <= 0xFFFF -> {
                encodeBuffer[pos] = (0xE0 or (codePoint shr 12)).toByte()
                encodeBuffer[pos + 1] = (0x80 or ((codePoint shr 6) and 0x3F)).toByte()
                encodeBuffer[pos + 2] = (0x80 or (codePoint and 0x3F)).toByte()
                3
            }
            else -> {
                encodeBuffer[pos] = (0xF0 or (codePoint shr 18)).toByte()
                encodeBuffer[pos + 1] = (0x80 or ((codePoint shr 12) and 0x3F)).toByte()
                encodeBuffer[pos + 2] = (0x80 or ((codePoint shr 6) and 0x3F)).toByte()
                encodeBuffer[pos + 3] = (0x80 or (codePoint and 0x3F)).toByte()
                4
            }
        }

        if (processId > 0) {
            inputQueue.write(encodeBuffer, pos)
        }
    }

    fun resize(columns: Int, rows: Int, width: Int, height: Int) {
        emulator?.let {
            it.resize(columns, rows)
            JNI.setPtyWindowSize(masterFd, rows, columns, width, height)
        } ?: run {
            emulator = TerminalEmulator(this, columns, rows, 2000)
            val pidArr = IntArray(1)
            val rowsArg = desiredRows ?: rows
            masterFd = JNI.createSubprocess(executable, cwd, argv, envp, pidArr, columns, rowsArg, width, height)
            processId = pidArr[0]
            // FIXME: start I/O threads and waiter
        }
    }

    fun finish() {
        if (isRunning) {
            try {
                Os.kill(processId, OsConstants.SIGKILL)
            } catch (_: ErrnoException) {
                client?.let {
                    android.util.Log.w("TerminalSession", "Failed sending SIGKILL: ${it}")
                }
            }
        }
    }
}
