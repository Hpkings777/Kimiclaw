package com.moonshot.kimiclaw.api.email.model

import kotlinx.serialization.Serializable

@Serializable
data class EmailAttachment(
    val contentType: String = "",
    val filename: String = "",
    val size: Long = 0L
)
