package com.termux.terminal

abstract class JNI {
    companion object {
        init {
            System.loadLibrary("termux")
        }

        @JvmStatic
        external fun close(fd: Int)

        @JvmStatic
        external fun createSubprocess(
            cmd: String,
            cwd: String,
            argv: Array<String>?,
            envp: Array<String>?,
            processIdArray: IntArray,
            columns: Int,
            rows: Int,
            width: Int,
            height: Int
        ): Int

        @JvmStatic
        external fun setPtyWindowSize(fd: Int, rows: Int, cols: Int, width: Int, height: Int)

        @JvmStatic
        external fun waitFor(pid: Int): Int
    }
}
