package com.moonshot.kimiclaw.model

import kotlinx.serialization.Serializable

@Serializable
enum class UpgradeStrategy {
    NORMAL,
    FORCE,
    MANUAL
}
