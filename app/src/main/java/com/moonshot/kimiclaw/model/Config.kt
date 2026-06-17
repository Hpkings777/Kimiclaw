package com.moonshot.kimiclaw.model

import kotlinx.serialization.Serializable

@Serializable
data class Config(
    val upgrade: UpgradeConfig = UpgradeConfig()
)
