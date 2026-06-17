package com.moonshot.kimiclaw.model

import kotlinx.serialization.Serializable

@Serializable
data class CachedConfig(
    val apiKey: String,
    val channels: List<CachedChannelConfig>
)
