package com.example.remindme.presentation.Remainder


import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import com.example.remindme.presentation.MainScreens.calculateNextTriggerTime
import com.example.remindme.presentation.Reminder.ReminderReceiver
import java.time.DayOfWeek
import java.time.LocalTime
import java.time.format.DateTimeFormatter

object ReminderScheduler {
    @SuppressLint("ServiceCast")
    fun rescheduleAlarms(context: Context, sharedPreferences: SharedPreferences) {
        val selectedDays = sharedPreferences.getStringSet("selected_days", emptySet()) ?: emptySet()
        val startTimeString = sharedPreferences.getString("start_time", "08:00") ?: "08:00"
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        try {
            val startTime = LocalTime.parse(startTimeString, formatter)

            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(context, ReminderReceiver::class.java)
            val daysOfWeek = DayOfWeek.values()

            for (day in selectedDays) {
                val dayOfWeek = daysOfWeek.firstOrNull { it.name.startsWith(day.uppercase()) }
                dayOfWeek?.let {
                    val triggerTime = calculateNextTriggerTime(it, startTime)
                    val pendingIntent = PendingIntent.getBroadcast(
                        context,
                        it.ordinal,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                    )

                    alarmManager.setRepeating(
                        AlarmManager.RTC_WAKEUP,
                        triggerTime,
                        AlarmManager.INTERVAL_DAY * 7,
                        pendingIntent
                    )
                }
            }
        } catch (e: Exception) {
            // Handle any exceptions
        }
    }
}
