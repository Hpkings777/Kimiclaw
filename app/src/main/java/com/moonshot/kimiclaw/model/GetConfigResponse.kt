package com.moonshot.kimiclaw.model

import kotlinx.serialization.Serializable

@Serializable
data class GetConfigResponse(
    val config: Config = Config()
)
