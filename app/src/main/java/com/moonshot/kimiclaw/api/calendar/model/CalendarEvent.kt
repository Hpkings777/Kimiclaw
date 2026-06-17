package com.moonshot.kimiclaw.api.calendar.model

import kotlinx.serialization.Serializable

@Serializable
data class CalendarEvent(
    val allDay: Boolean = false,
    val attendees: List<EventAttendee> = emptyList(),
    val calendarId: Long = 0L,
    val description: String = "",
    val end: String = "",
    val eventId: Long = 0L,
    val location: String = "",
    val recurrence: String = "",
    val reminders: List<EventReminder> = emptyList(),
    val start: String = "",
    val title: String = "",
    val timezone: String = ""
)
