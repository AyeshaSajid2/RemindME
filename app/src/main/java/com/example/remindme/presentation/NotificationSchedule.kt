package com.example.remindme.presentation

import java.time.DayOfWeek
import java.time.LocalTime

data class NotificationSchedule(
    val day: DayOfWeek,
    val time: LocalTime,
    var isEnabled: Boolean
)
