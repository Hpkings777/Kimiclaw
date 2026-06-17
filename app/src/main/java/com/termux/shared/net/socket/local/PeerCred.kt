package com.termux.shared.net.socket.local

import android.app.ActivityManager
import android.content.Context

class PeerCred {
    @JvmField var pid: Int = -1
    @JvmField var uid: Int = -1
    @JvmField var gid: Int = -1
    @JvmField var cmdline: String? = null
    @JvmField var pname: String? = null
    @JvmField var uname: String? = null
    @JvmField var gname: String? = null

    fun getProcessString(): String {
        return if (pname.isNullOrEmpty()) "$pid" else "$pid ($pname)"
    }

    fun getUserString(): String {
        return if (uname == null) "$uid" else "$uid ($uname)"
    }

    fun getGroupString(): String {
        return if (gname == null) "$gid" else "$gid ($gname)"
    }

    fun getMinimalString(): String {
        return "process=${getProcessString()}, user=${getUserString()}, group=${getGroupString()}"
    }

    fun getLogString(): String {
        val sb = StringBuilder("Peer Cred:\n")
        sb.appendLine("Process: ${getProcessString()}")
        sb.appendLine("User: ${getUserString()}")
        sb.appendLine("Group: ${getGroupString()}")
        cmdline?.let { sb.appendLine("Cmdline: $it") }
        return sb.toString()
    }

    fun getMarkdownString(): String {
        val sb = StringBuilder("## Peer Cred\n")
        sb.appendLine("- **Process**: ${getProcessString()}")
        sb.appendLine("- **User**: ${getUserString()}")
        sb.appendLine("- **Group**: ${getGroupString()}")
        cmdline?.let { sb.appendLine("- **Cmdline**: `$it`") }
        return sb.toString()
    }

    fun fillPeerCred(context: Context) {
        fillUnameAndGname(context)
        fillPname(context)
    }

    private fun fillUnameAndGname(context: Context) {
        uname = uid.toString()
        gname = if (gid != uid) gid.toString() else uname
    }

    private fun fillPname(context: Context) {
        if (pid <= 0 || pname != null) return
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as? ActivityManager ?: return
        try {
            am.runningAppProcesses?.forEach { proc ->
                if (proc.pid == pid) {
                    pname = proc.processName
                    return
                }
            }
        } catch (_: Exception) {}
    }

    companion object {
        @JvmStatic
        fun getPeerCredLogString(cred: PeerCred?): String = cred?.getLogString() ?: "null"

        @JvmStatic
        fun getPeerCredMarkdownString(cred: PeerCred?): String = cred?.getMarkdownString() ?: "null"
    }
}
