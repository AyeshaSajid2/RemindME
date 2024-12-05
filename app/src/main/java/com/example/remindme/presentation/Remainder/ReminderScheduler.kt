package com.example.remindme.presentation.Remainder


import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import com.example.remindme.presentation.MainScreens.calculateNextTriggerTime
import com.example.remindme.presentation.MainScreens.scheduleForDay
import com.example.remindme.presentation.Reminder.ReminderReceiver
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

object ReminderScheduler {
    @SuppressLint("ServiceCast")
    fun rescheduleAlarms(context: Context, sharedPreferences: SharedPreferences) {
        val selectedDays = sharedPreferences.getStringSet("selected_days", emptySet()) ?: emptySet()
        val intervals = listOf(
            Pair(sharedPreferences.getString("start_time", "08:00 AM") ?: "08:00 AM",
                sharedPreferences.getString("end_time", "10:00 AM") ?: "10:00 AM"),
            Pair(sharedPreferences.getString("start_time_2", "12:00 PM") ?: "12:00 PM",
                sharedPreferences.getString("end_time_2", "02:00 PM") ?: "02:00 PM"),
            Pair(sharedPreferences.getString("start_time_3", "04:00 PM") ?: "04:00 PM",
                sharedPreferences.getString("end_time_3", "06:00 PM") ?: "06:00 PM")
        )
        val frequencyMinutes = sharedPreferences.getInt("frequency", 10)

        val formatter = DateTimeFormatter.ofPattern("h:mm a")
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, ReminderReceiver::class.java)

        val selectedDayEnums = selectedDays.mapNotNull { dayName ->
            try {
                DayOfWeek.valueOf(dayName.uppercase())
            } catch (e: IllegalArgumentException) {
                null
            }
        }

        for (day in selectedDayEnums) {
            for ((start, end) in intervals) {
                val startTime = LocalTime.parse(start, formatter)
                val endTime = LocalTime.parse(end, formatter)

                scheduleForDay(day, startTime, endTime, frequencyMinutes, alarmManager, intent, context)
            }
        }

        // Schedule rescheduling at the end of the week
        scheduleWeeklyRescheduling(context, alarmManager, sharedPreferences)
    }

    @SuppressLint("ScheduleExactAlarm")
    private fun scheduleWeeklyRescheduling(context: Context, alarmManager: AlarmManager, sharedPreferences: SharedPreferences) {
        val intent = Intent(context, BootReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            1000,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val nextSunday = LocalDate.now().with(DayOfWeek.SUNDAY).plusWeeks(1)
        val rescheduleTime = nextSunday.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            rescheduleTime,
            pendingIntent
        )
    }
}
