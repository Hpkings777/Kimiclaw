package com.termux.terminal

class TerminalSessionWaiter(
    private val session: TerminalSession,
    name: String
) : Thread(name) {
    override fun run() {
        val exitCode = JNI.waitFor(session.processId)
        session.outputHandler.sendMessage(
            session.outputHandler.obtainMessage(
                TerminalSessionOutputHandler.MSG_PROCESS_EXITED,
                exitCode
            )
        )
    }
}
