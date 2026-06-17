package com.moonshot.kimiclaw.model

import kotlinx.serialization.Serializable

@Serializable
data class UpgradeDialog(
    val title: String = "",
    val content: String = "",
    val primaryAction: String = ""
)
