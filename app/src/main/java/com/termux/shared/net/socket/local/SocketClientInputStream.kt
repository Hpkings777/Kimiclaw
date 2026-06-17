package com.termux.shared.net.socket.local

import java.io.IOException
import java.io.InputStream

class SocketClientInputStream(
    private val client: SocketClient
) : InputStream() {

    private val singleByteBuf = ByteArray(1)

    override fun read(): Int {
        val n = client.read(singleByteBuf)
        return if (n <= 0) -1 else singleByteBuf[0].toInt() and 0xFF
    }

    override fun read(b: ByteArray): Int {
        return client.read(b)
    }

    override fun available(): Int {
        return client.available()
    }
}
