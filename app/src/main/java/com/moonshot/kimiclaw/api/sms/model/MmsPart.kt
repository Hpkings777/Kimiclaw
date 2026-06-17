package com.moonshot.kimiclaw.api.sms.model

import kotlinx.serialization.Serializable

@Serializable
data class MmsPart(
    val contentType: String = "",
    val filename: String = "",
    val partId: Long = 0L,
    val size: Long = 0L,
    val text: String = ""
)
