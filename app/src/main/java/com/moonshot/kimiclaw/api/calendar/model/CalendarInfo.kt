package com.moonshot.kimiclaw.api.calendar.model

import kotlinx.serialization.Serializable

@Serializable
data class CalendarInfo(
    val accountName: String = "",
    val color: Int = 0,
    val id: Long = 0L,
    val name: String = ""
)
