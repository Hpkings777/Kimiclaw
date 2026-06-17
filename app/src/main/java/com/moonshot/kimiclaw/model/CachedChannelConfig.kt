package com.moonshot.kimiclaw.model

import kotlinx.serialization.Serializable

@Serializable
data class CachedChannelConfig(
    val platform: String = "",
    val primary: String = "",
    val secondary: String = ""
)
