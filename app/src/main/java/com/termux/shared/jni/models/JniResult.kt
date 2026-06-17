package com.termux.shared.jni.models

import java.io.Serializable

class JniResult(
    @JvmField var retval: Int,
    @JvmField var errno: Int,
    @JvmField var errmsg: String?
) : Serializable {
    @JvmField var intData: Int = 0

    constructor(retval: Int, errno: Int, errmsg: String?, intData: Int) : this(retval, errno, errmsg) {
        this.intData = intData
    }

    constructor(errmsg: String?, throwable: Throwable?) : this(-1, 0, buildErrorString(errmsg, throwable))

    fun getErrorString(): String {
        val sb = StringBuilder()
        sb.append(formatField("Retval", retval))
        if (errno != 0) {
            sb.append("\n")
            sb.append(formatField("Errno", errno))
        }
        val e = errmsg
        if (!e.isNullOrEmpty()) {
            sb.append("\n")
            sb.append(formatField("Errmsg", errmsg))
        }
        return sb.toString()
    }

    companion object {
        @JvmStatic
        fun getErrorString(result: JniResult?): String {
            return result?.getErrorString() ?: "null"
        }

        private fun formatField(name: String, value: Any): String = "$name: $value"

        private fun buildErrorString(msg: String?, th: Throwable?): String {
            return if (th != null) "$msg: ${th.message}" else msg ?: "Unknown error"
        }
    }
}
