package com.termux.shared.models

import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.Date
import java.util.TimeZone

class ReportInfo(
    val userAction: String,
    val sender: String,
    val reportTitle: String
) : Serializable {
    var addReportInfoHeaderToMarkdown: Boolean = false
    var reportSaveFileLabel: String? = null
    var reportSaveFilePath: String? = null
    var reportString: String? = null
    var reportStringPrefix: String? = null
    var reportStringSuffix: String? = null
    val reportTimestamp: String = run {
        val fmt = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS z")
        fmt.timeZone = TimeZone.getTimeZone("UTC")
        fmt.format(Date())
    }
}
