package com.termux.terminal

interface TerminalSessionClient {
    fun onSessionUpdated(session: TerminalSession)
    fun onSessionFinished(session: TerminalSession)
    fun onSessionStarted(pid: Int, session: TerminalSession)
}
