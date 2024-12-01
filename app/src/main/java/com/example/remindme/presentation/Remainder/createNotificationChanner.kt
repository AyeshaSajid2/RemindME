package com.example.remindme.presentation.Remainder

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

fun createNotificationChannel(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channelId = "reminder_channel"
        val channelName = "Reminder Notifications"
        val channelDescription = "Notifications for reminders"
        val importance = NotificationManager.IMPORTANCE_HIGH

        val notificationChannel = NotificationChannel(channelId, channelName, importance)
        notificationChannel.description = channelDescription

        // Register the channel with the system
        val notificationManager = context.getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannel(notificationChannel)
    }
}
