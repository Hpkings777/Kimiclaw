package com.moonshot.kimiclaw.model

import kotlinx.serialization.Serializable

@Serializable
data class GetConfigRequest(
    val syncToken: String = ""
)
