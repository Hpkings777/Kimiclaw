package com.termux.shared.net.socket.local

import java.io.Serializable
import java.nio.charset.StandardCharsets

abstract class SocketServerConfig(
    val title: String = "TermuxAm"
) : Serializable {
    val isAbstractNamespaceSocket: Boolean
    val path: String
    var fd: Int = -1
    var backlog: Int? = null
    var deadline: Long? = null
    var receiveTimeout: Int? = null
    var sendTimeout: Int? = null

    val localSocketManagerClient: Any = Unit

    init {
        val rawPath = "/data/data/com.moonshot.kimiclaw/files/apps/com.moonshot.kimiclaw/termux-am/am.sock"
        val rawBytes = rawPath.toByteArray(StandardCharsets.UTF_8)
        isAbstractNamespaceSocket = rawBytes[0] == 0.toByte()
        path = if (isAbstractNamespaceSocket) rawPath else rawPath
    }

    fun getDeadlineOrZero(): Long = deadline ?: 0L
    fun getReceiveTimeoutOrElse(): Int = receiveTimeout ?: 10000
    fun getSendTimeoutOrElse(): Int = sendTimeout ?: 10000

    fun getSocketName(): String = "LocalSocket.$title"

    fun toLogString(): String = buildString {
        appendLine("$title Socket Server Run Config:")
        appendLine("  Path: $path")
        appendLine("  AbstractNamespaceSocket: $isAbstractNamespaceSocket")
        appendLine("  FD: $fd")
        appendLine("  ReceiveTimeout: ${getReceiveTimeoutOrElse()}")
        appendLine("  SendTimeout: ${getSendTimeoutOrElse()}")
        appendLine("  Deadline: ${getDeadlineOrZero()}")
        appendLine("  Backlog: ${backlog ?: 50}")
    }

    fun toMarkdownString(): String = buildString {
        appendLine("## $title Socket Server Run Config")
        appendLine("- **Path**: `$path`")
        appendLine("- **AbstractNamespaceSocket**: $isAbstractNamespaceSocket")
        appendLine("- **FD**: $fd")
        appendLine("- **ReceiveTimeout**: ${getReceiveTimeoutOrElse()}")
        appendLine("- **SendTimeout**: ${getSendTimeoutOrElse()}")
        appendLine("- **Deadline**: ${getDeadlineOrZero()}")
        appendLine("- **Backlog**: ${backlog ?: 50}")
    }
}
