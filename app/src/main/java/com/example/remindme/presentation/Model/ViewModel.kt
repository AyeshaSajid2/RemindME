package com.example.remindme.presentation.Model

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.remindme.presentation.NotificationSchedule

class ReminderViewModel : ViewModel() {
    private val _schedules = mutableStateListOf<NotificationSchedule>()
    val schedules: List<NotificationSchedule> = _schedules

    fun addSchedule(schedule: NotificationSchedule) {
        _schedules.add(schedule)
    }

    fun toggleSchedule(index: Int) {
        _schedules[index] = _schedules[index].copy(isEnabled = !_schedules[index].isEnabled)
    }
}
