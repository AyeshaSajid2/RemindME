package com.example.remindme.presentation.MainScreens

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.text.format.DateFormat
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.remindme.R
import com.example.remindme.presentation.Reminder.ReminderReceiver
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun validateIntervals(sharedPreferences: SharedPreferences, context: Context): Boolean {
    // Retrieve the start and end times for all intervals from SharedPreferences
    val intervals = listOf(
        Pair(
            sharedPreferences.getString("start_time", "08:00 AM") ?: "08:00 AM",
            sharedPreferences.getString("end_time", "10:00 AM") ?: "10:00 AM"
        ),
        Pair(
            sharedPreferences.getString("start_time_2", "12:00 PM") ?: "12:00 PM",
            sharedPreferences.getString("end_time_2", "02:00 PM") ?: "02:00 PM"
        ),
        Pair(
            sharedPreferences.getString("start_time_3", "04:00 PM") ?: "04:00 PM",
            sharedPreferences.getString("end_time_3", "06:00 PM") ?: "06:00 PM"
        )
    )

    // Create DateTimeFormatter based on 12-hour or 24-hour format
    val is24HourFormat = DateFormat.is24HourFormat(context)
    val formatter = if (is24HourFormat) {
        DateTimeFormatter.ofPattern("HH:mm")
    } else {
        DateTimeFormatter.ofPattern("h:mm a")
    }

    // Validate each interval
    for (i in intervals.indices) {
        val (startTimeString, endTimeString) = intervals[i]
        try {
            val startTime = LocalTime.parse(startTimeString, formatter)
            val endTime = LocalTime.parse(endTimeString, formatter)

            // Check if the start time is greater than or equal to the end time
            if (startTime >= endTime) {
                // Show error message for the current interval
                Toast.makeText(
                    context,
                    "Invalid time range in interval ${i + 1}. Start time cannot be greater than or equal to end time.",
                    Toast.LENGTH_SHORT
                ).show()
                return false
            }

            // Additional logic to ensure all intervals are within the same day can be added here if needed
            // (e.g., check if start and end times fall on the same day, though by default it's assumed they are.)

        } catch (e: Exception) {
            // If there's a parsing error or any other issue, show the error for the current interval
            Toast.makeText(
                context,
                "Invalid time format in interval ${i + 1}. Please check your inputs.",
                Toast.LENGTH_SHORT
            ).show()
            return false
        }
    }

    // If all validations pass, return true
    return true
}

fun setReminders(sharedPreferences: SharedPreferences, context: Context) {
    // First, validate all intervals
    if (!validateIntervals(sharedPreferences, context)) {
        return // Stop if validation fails
    }

    // If validation passes, continue with setting the reminders
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

    val is24HourFormat = DateFormat.is24HourFormat(context)
    val formatter = if (is24HourFormat) {
        DateTimeFormatter.ofPattern("HH:mm")
    } else {
        DateTimeFormatter.ofPattern("h:mm a")
    }

    try {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, ReminderReceiver::class.java)

        triggerImmediateNotification(context)

        val selectedDayEnums = selectedDays.mapNotNull { dayName ->
            try {
                DayOfWeek.valueOf(dayName.uppercase())
            } catch (e: IllegalArgumentException) {
                Log.e("Reminder", "Invalid day name: $dayName", e)
                null
            }
        }

        for (day in selectedDayEnums) {
            for ((startString, endString) in intervals) {
                val startTime = LocalTime.parse(startString, formatter)
                val endTime = LocalTime.parse(endString, formatter)

                scheduleForDay(
                    day, startTime, endTime, frequencyMinutes, alarmManager, intent, context
                )
            }
        }

        Toast.makeText(context, "Reminders scheduled for selected days.", Toast.LENGTH_SHORT).show()
    } catch (e: Exception) {
        Toast.makeText(context, "Invalid time format. Please check your inputs.", Toast.LENGTH_SHORT).show()
        Log.e("Reminder", "Error setting reminders: ${e.message}", e)
    }
}

// Function to validate the intervals
fun validateIntervals(intervals: List<Pair<String, String>>): String? {
    intervals.forEachIndexed { index, (start, end) ->
        val startTime = LocalTime.parse(start, DateTimeFormatter.ofPattern("h:mm a"))
        val endTime = LocalTime.parse(end, DateTimeFormatter.ofPattern("h:mm a"))

        // Check if start time is greater than or equal to end time
        if (startTime >= endTime) {
            return "Issue in interval ${index + 1}: Start time cannot be greater than or equal to end time."
        }

        // Check if intervals are on the same day (end time must not be before start time)
        if (startTime.isAfter(endTime)) {
            return "Issue in interval ${index + 1}: Time intervals should be within the same day."
        }
    }
    return null
}

@SuppressLint("ScheduleExactAlarm")
fun scheduleForDay(
    day: DayOfWeek,
    startTime: LocalTime,
    endTime: LocalTime,
    frequencyMinutes: Int,
    alarmManager: AlarmManager,
    intent: Intent,
    context: Context
) {
    var triggerTime = calculateNextTriggerTime(day, startTime)
    val endTimeInMillis = calculateNextTriggerTime(day, endTime)
    var alarmIndex = 0

    Log.i("Reminder detail", "Scheduling alarms for day: $day")
    Log.i("Reminder detail", "Interval start: $startTime, end: $endTime, frequency: $frequencyMinutes minutes")

    while (triggerTime < endTimeInMillis) {
        val alarmTime = Instant.ofEpochMilli(triggerTime)
            .atZone(ZoneId.systemDefault())
            .toLocalTime()

        Log.i("Reminder detail", "Scheduled alarm $alarmIndex: Day $day, Time: $alarmTime")

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            day.ordinal * 100 + alarmIndex,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            triggerTime,
            pendingIntent
        )

        triggerTime += frequencyMinutes * 60 * 1000
        alarmIndex++
    }
}

fun triggerImmediateNotification(context: Context) {
    val notificationManager = NotificationManagerCompat.from(context)
    val notification = NotificationCompat.Builder(context, "reminder_channel")
        .setContentText("Reminder setup complete. Alarms scheduled!")
        .setSmallIcon(R.drawable.splash_icon)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .build()

    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
        return
    }

    notificationManager.notify(System.currentTimeMillis().toInt(), notification)
}

fun calculateNextTriggerTime(dayOfWeek: DayOfWeek, time: LocalTime): Long {
    val now = LocalDate.now()
    val nextDay = now.with(dayOfWeek).let {
        if (it.isBefore(now) || (it == now && time.isBefore(LocalTime.now()))) it.plusWeeks(1) else it
    }
    return nextDay.atTime(time).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
}
