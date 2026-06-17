package com.moonshot.kimiclaw.api.calendar.model

import kotlinx.serialization.Serializable

@Serializable
data class EventReminder(
    val method: String = "",
    val minutes: Int = 0
)
