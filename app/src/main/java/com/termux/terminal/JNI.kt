package com.termux.terminal

abstract class JNI {
    companion object {
        init {
            System.loadLibrary("termux")
        }
    }

    external fun close(fd: Int)
    external fun createSubprocess(
        cmd: String,
        cwd: String,
        argv: Array<String>?,
        envp: Array<String>?,
        processIdArray: IntArray,
        columns: Int,
        rows: Int,
        masterFd: Int,
        slaveFd: Int
    ): Int

    external fun setPtyWindowSize(fd: Int, rows: Int, cols: Int, width: Int, height: Int)
    external fun waitFor(pid: Int): Int
}
