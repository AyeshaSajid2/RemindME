package com.example.remindme.presentation.MainScreens

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlarmManager

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.remindme.presentation.theme.*
//import com.example.remindme.presentation.Reminde
// r.ReminderScheduler
import kotlinx.coroutines.delay
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import android.text.format.DateFormat
import androidx.compose.ui.text.TextStyle
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.remindme.R
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

import androidx.activity.compose.BackHandler
import com.example.remindme.presentation.Reminder.ReminderReceiver

@RequiresApi(Build.VERSION_CODES.S)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(navController: NavHostController) {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)

    // State to hold the current time
    var currentTime by remember { mutableStateOf(getFormattedTime()) }
    var currentTimeMillis by remember { mutableStateOf(System.currentTimeMillis()) }

    // Update time every second
    LaunchedEffect(Unit) {
        while (true) {
            delay(1000L) // Update every second
            currentTime = getFormattedTime()
            currentTimeMillis = System.currentTimeMillis()
        }
    }

    // Handle back press to close the app
    BackHandler {
        // This will close the app when back is pressed
        (context as? android.app.Activity)?.finish()
    }

    // Scaffold to hold the content
    Scaffold {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(if (androidx.compose.foundation.isSystemInDarkTheme()) BackgroundDark else BackgroundLight)
                .padding(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Display current time at the top center
                Text(
                    text = "$currentTime",
                    fontSize = 12.sp,
                    color = if (androidx.compose.foundation.isSystemInDarkTheme()) TextDark else TextLight,
                    modifier = Modifier.padding(vertical = 2.dp)
                )

                // Button to navigate to Select Days screen
                HomeButton("Select Days") {
                    navController.navigate("select_days")
                }
                // Button to navigate to Select Start Time screen
                HomeButton("Select Start Time") {
                    navController.navigate("select_start_time")
                }
                // Button to navigate to Select End Time screen
                HomeButton("Select End Time") {
                    navController.navigate("select_end_time")
                }
                // Button to navigate to Select Frequency screen
                HomeButton("Select Frequency") {
                    navController.navigate("select_frequency")
                }
                // Button to navigate to Show Details screen
                HomeButton("Show Details") {
                    navController.navigate("show_details")
                }
                // Button to set reminders
                HomeButton("Set Reminder") {
                    setReminders(sharedPreferences, context)
                }
                // Button to view scheduled alarms
            }
        }
    }
}


fun setReminders(sharedPreferences: SharedPreferences, context: Context) {
    val selectedDays = sharedPreferences.getStringSet("selected_days", emptySet()) ?: emptySet()
    val startTimeString = sharedPreferences.getString("start_time", "08:00 AM") ?: "08:00 AM"
    val endTimeString = sharedPreferences.getString("end_time", "06:00 PM") ?: "06:00 PM"
    val frequencyMinutes = sharedPreferences.getInt("frequency", 10)

    // Get the device's time format
    val is24HourFormat = DateFormat.is24HourFormat(context)
    val formatter = if (is24HourFormat) {
        DateTimeFormatter.ofPattern("HH:mm")
    } else {
        DateTimeFormatter.ofPattern("h:mm a")
    }

    try {
        val startTime = LocalTime.parse(startTimeString, formatter)
        val endTime = LocalTime.parse(endTimeString, formatter)
        val now = LocalDate.now()
        val currentDay = DayOfWeek.from(now)
        val currentTime = LocalTime.now()
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, ReminderReceiver::class.java)

        // Trigger an immediate notification
        triggerImmediateNotification(context)

        for (dayName in selectedDays) {
            val day = DayOfWeek.valueOf(dayName.uppercase())

            if (currentDay == day) {
                when {
                    currentTime.isAfter(startTime) && currentTime.isBefore(endTime) -> {
                        // Current time within interval on a selected day
                        scheduleForDay(
                            day,
                            currentTime.plusMinutes(frequencyMinutes.toLong()),
                            endTime,
                            frequencyMinutes,
                            alarmManager,
                            intent,
                            context
                        )
                    }
                    currentTime.isBefore(startTime) -> {
                        // Current time earlier than start time on a selected day
                        scheduleForDay(
                            day, startTime, endTime, frequencyMinutes, alarmManager, intent, context
                        )
                    }
                }
            } else {
                // Schedule for other selected days
                scheduleForDay(
                    day, startTime, endTime, frequencyMinutes, alarmManager, intent, context
                )
            }
        }

        Toast.makeText(context, "Reminders have been configured for selected days.", Toast.LENGTH_SHORT).show()
    } catch (e: Exception) {
        Toast.makeText(context, "Invalid time format. Please check your inputs.", Toast.LENGTH_SHORT).show()
        Log.e("Reminder", "Error setting reminders: ${e.message}", e)
    }
}

//fun setReminders(sharedPreferences: SharedPreferences, context: Context) {
//    val selectedDays = sharedPreferences.getStringSet("selected_days", emptySet()) ?: emptySet()
//    val startTimeString = sharedPreferences.getString("start_time", "08:00 AM") ?: "08:00 AM"
//    val endTimeString = sharedPreferences.getString("end_time", "06:00 PM") ?: "06:00 PM"
//    val frequencyMinutes = sharedPreferences.getInt("frequency", 10)
//
//    // Get the device's time format
//    val is24HourFormat = DateFormat.is24HourFormat(context)
//    val formatter = if (is24HourFormat) {
//        DateTimeFormatter.ofPattern("HH:mm")
//    } else {
//        DateTimeFormatter.ofPattern("h:mm a")
//    }
//
//    try {
//        val startTime = LocalTime.parse(startTimeString, formatter)
//        val endTime = LocalTime.parse(endTimeString, formatter)
//        val now = LocalDate.now()
//        val currentDay = DayOfWeek.from(now)
//        val currentTime = LocalTime.now()
//        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
//        val intent = Intent(context, ReminderReceiver::class.java)
//
//        for (dayName in selectedDays) {
//            val day = DayOfWeek.valueOf(dayName.uppercase())
//
//            if (currentDay == day) {
//                when {
//                    currentTime.isAfter(startTime) && currentTime.isBefore(endTime) -> {
//                        // Current time within interval on a selected day
//                        triggerImmediateNotification(context)
//                        scheduleForDay(
//                            day,
//                            currentTime.plusMinutes(frequencyMinutes.toLong()),
//                            endTime,
//                            frequencyMinutes,
//                            alarmManager,
//                            intent,
//                            context
//                        )
//                    }
//                    currentTime.isBefore(startTime) -> {
//                        // Current time earlier than start time on a selected day
//                        scheduleForDay(
//                            day, startTime, endTime, frequencyMinutes, alarmManager, intent, context
//                        )
//                    }
//                }
//            } else {
//                // Schedule for other selected days
//                scheduleForDay(
//                    day, startTime, endTime, frequencyMinutes, alarmManager, intent, context
//                )
//            }
//        }
//
//        Toast.makeText(context, "Reminders have been configured for selected days.", Toast.LENGTH_SHORT).show()
////        triggerfirstImmediateNotification(context)
//    } catch (e: Exception) {
//        Toast.makeText(context, "Invalid time format. Please check your inputs.", Toast.LENGTH_SHORT).show()
//        Log.e("Reminder", "Error setting reminders: ${e.message}", e)
//    }
//}

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

    while (triggerTime < endTimeInMillis) {
        val alarmTime = Instant.ofEpochMilli(triggerTime)
            .atZone(ZoneId.systemDefault())
            .toLocalTime()

        Log.i("Reminder detail", "Scheduled alarm $alarmIndex:")
        Log.i("Reminder detail", "  Day: $day")
        Log.i("Reminder detail", "  Time: $alarmTime")

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
//        .setContentTitle("Reminder")
        .setContentText("You find a cozy spot to read or relax. Enjoy the tranquility and take a deep breath.")
        .setSmallIcon(R.drawable.splash_icon)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .build()

    // Check for POST_NOTIFICATIONS permission
    if (ActivityCompat.checkSelfPermission(
            context, // Replace `this` with `context`
            Manifest.permission.POST_NOTIFICATIONS
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        return
    }

    // Show the notification
    notificationManager.notify(System.currentTimeMillis().toInt(), notification)
}


// Helper function to get formatted time
fun getFormattedTime(): String {
    val formatter = DateTimeFormatter.ofPattern("hh:mm:ss a")
    return LocalTime.now().format(formatter)
}

// Helper function to calculate the next trigger time
fun calculateNextTriggerTime(dayOfWeek: DayOfWeek, time: LocalTime): Long {
    val now = LocalDate.now()
    val nextDay = now.with(dayOfWeek).let {
        if (it.isBefore(now) || (it == now && time.isBefore(LocalTime.now()))) it.plusWeeks(1) else it
    }
    return nextDay.atTime(time).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
}

@Composable
fun HomeButton(label: String, onClick: () -> Unit) {
    val isDarkTheme = androidx.compose.foundation.isSystemInDarkTheme()
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth(0.8f)
            .padding(horizontal = 8.dp)
            .padding(vertical = 8.dp),
        colors = androidx.compose.material3.ButtonDefaults.buttonColors(
            containerColor = if (isDarkTheme) ButtonDark else ButtonLight,
            contentColor = if (isDarkTheme) TextDark else TextLight
        )
    ) {
        Text(
            text = label,
            style = TextStyle(fontSize = 16.sp),
            color = if (isDarkTheme) TextDark else TextLight
        )
    }
}
