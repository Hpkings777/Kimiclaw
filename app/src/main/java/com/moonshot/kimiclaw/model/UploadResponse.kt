package com.moonshot.kimiclaw.model

import kotlinx.serialization.Serializable

@Serializable
data class UploadResponse(
    val objectName: String = "",
    val url: String = ""
)
