package com.termux.shared.net.socket.local

import java.io.IOException
import java.io.OutputStream

class SocketClientOutputStream(
    private val client: SocketClient
) : OutputStream() {

    override fun write(b: Int) {
        write(byteArrayOf(b.toByte()))
    }

    override fun write(b: ByteArray, off: Int, len: Int) {
        val chunk = if (off == 0 && len == b.size) b else b.copyOfRange(off, off + len)
        val error = client.write(chunk)
        if (error != null) throw IOException(error)
    }

    private fun SocketClient.write(data: ByteArray): String? {
        val result = LocalSocketManager.send(client.config.getSocketName() + " (client)", client.fd, data)
        return if (result == null || result.retval != 0) {
            JniResult.getErrorString(result)
        } else null
    }
}
