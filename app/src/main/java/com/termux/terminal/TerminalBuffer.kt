package com.termux.terminal

class TerminalBuffer {
    private val buffer = ByteArray(4096)
    private var writePos = 0
    private var readPos = 0
    var count = 0
    @Volatile var finished = false
    var completedCount: Int = 0

    @Synchronized
    fun write(source: ByteArray, length: Int, offset: Int = 0) {
        if (finished) return
        var remaining = length
        var srcOff = offset
        while (remaining > 0) {
            val space = buffer.size - writePos
            val chunk = minOf(remaining, space)
            System.arraycopy(source, srcOff, buffer, writePos, chunk)
            writePos = (writePos + chunk) % buffer.size
            count += chunk
            remaining -= chunk
            srcOff += chunk
        }
        (this as java.lang.Object).notify()
    }

    @Synchronized
    fun read(target: ByteArray, blocking: Boolean): Int {
        if (count == 0) {
            if (finished || !blocking) return 0
            try {
                (this as java.lang.Object).wait()
            } catch (_: InterruptedException) {
                return 0
            }
        }
        val toRead = minOf(count, target.size)
        for (i in 0 until toRead) {
            target[i] = buffer[readPos]
            readPos = (readPos + 1) % buffer.size
        }
        count -= toRead
        return toRead
    }

    @Synchronized
    fun setFinished() {
        finished = true
        (this as java.lang.Object).notify()
    }
}
