package com.moonshot.kimiclaw.api.email.model

import kotlinx.serialization.Serializable

@Serializable
enum class EmailProvider(
    val imapHost: String,
    val imapPort: Int,
    val ssl: Boolean,
    val smtpHost: String,
    val smtpPort: Int
) {
    GMAIL("imap.gmail.com", 993, true, "smtp.gmail.com", 587),
    QQ("imap.qq.com", 993, true, "smtp.qq.com", 465),
    NETEASE_163("imap.163.com", 993, true, "smtp.163.com", 465),
    NETEASE_126("imap.126.com", 993, true, "smtp.126.com", 465),
    OUTLOOK("outlook.office365.com", 993, true, "smtp.office365.com", 587),
    CUSTOM("", 993, true, "", 587)
}
