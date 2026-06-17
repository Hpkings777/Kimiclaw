package com.termux.terminal

import android.os.Handler
import android.os.Message
import java.nio.charset.StandardCharsets

class TerminalSessionOutputHandler(
    private val session: TerminalSession
) : Handler() {

    companion object {
        const val MSG_PROCESS_EXITED = 4
    }

    private val buffer = ByteArray(4096)

    override fun handleMessage(msg: Message) {
        val bytesRead = session.outputQueue.read(buffer, false)
        if (bytesRead > 0) {
            session.emulator?.let { emulator ->
                for (i in 0 until bytesRead) {
                    emulator.write(buffer[i])
                }
            }
            session.client?.onSessionUpdated(session)
        }

        if (msg.what == MSG_PROCESS_EXITED) {
            val exitCode = msg.obj as Int
            synchronized(session) {
                session.processId = -1
                session.exitCode = exitCode
            }
            session.inputQueue.setFinished()
            session.outputQueue.setFinished()
            JNI.close(session.masterFd)

            val message = buildProcessCompletedMessage(exitCode)
            session.emulator?.let { emulator ->
                for (b in message.toByteArray(StandardCharsets.UTF_8)) {
                    emulator.write(b)
                }
            }
            session.client?.onSessionUpdated(session)
            session.client?.onSessionFinished(session)
        }
    }

    private fun buildProcessCompletedMessage(exitCode: Int): String {
        val prefix = "\r\n[Process completed"
        val suffix = if (exitCode > 0) {
            " (code $exitCode)"
        } else if (exitCode < 0) {
            " (signal ${-exitCode})"
        } else ""
        return "$prefix$suffix - press Enter]"
    }
}
