package com.moonshot.kimiclaw.api.email.model

import kotlinx.serialization.Serializable

@Serializable
data class EmailDetail(
    val attachments: List<EmailAttachment> = emptyList(),
    val bodyHtml: String = "",
    val bodyText: String = "",
    val cc: List<String> = emptyList(),
    val date: String = "",
    val from: String = "",
    val subject: String = "",
    val to: List<String> = emptyList(),
    val uid: Long = 0L
)
