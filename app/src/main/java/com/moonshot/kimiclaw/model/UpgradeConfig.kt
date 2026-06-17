package com.moonshot.kimiclaw.model

import kotlinx.serialization.Serializable

@Serializable
data class UpgradeConfig(
    val version: String = "",
    val displayVersion: String = "",
    val downloadUrl: String = "",
    val packageSizeMb: Int = 0,
    val upgradeStrategy: UpgradeStrategy = UpgradeStrategy.MANUAL,
    val upgradeDialog: UpgradeDialog = UpgradeDialog(),
    val showDialogIntervalMs: Long = 0L
)
