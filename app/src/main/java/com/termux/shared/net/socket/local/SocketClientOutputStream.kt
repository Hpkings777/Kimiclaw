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
        client.write(chunk)
    }
}
