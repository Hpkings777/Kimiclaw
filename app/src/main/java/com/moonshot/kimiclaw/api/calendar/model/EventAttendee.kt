package com.moonshot.kimiclaw.api.calendar.model

import kotlinx.serialization.Serializable

@Serializable
data class EventAttendee(
    val email: String = "",
    val name: String = "",
    val status: String = ""
)
