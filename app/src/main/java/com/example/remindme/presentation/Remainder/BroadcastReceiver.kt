package com.example.remindme.presentation.Reminder

import android.Manifest
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.remindme.R
import com.example.remindme.presentation.Remainder.ReminderDetailActivity
import com.example.remindme.presentation.Remainder.ReminderDetailScreen
import com.example.remindme.presentation.theme.RemindMeTheme
import java.lang.reflect.Modifier
import kotlin.random.Random

class ReminderReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent?) {
        // Log the event of receiving the reminder broadcast
        Log.i("ReminderReceiver", "Reminder triggered at ${System.currentTimeMillis()}")

        // List of messages from strings.xml
        val messages = context.resources.getStringArray(R.array.reminder_messages)
        val randomMessage = messages[Random.nextInt(messages.size)]

        // Create an Intent to open the activity that shows the notification details
        val notificationIntent = Intent(context, ReminderDetailActivity::class.java).apply {
            putExtra("notification_message", randomMessage) // Pass message to the next screen
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Check for notification permission
        if (ActivityCompat.checkSelfPermission(
                context, Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Show notification with click action
            val notificationManager = NotificationManagerCompat.from(context)
            val notification = NotificationCompat.Builder(context, "reminder_channel")
                .setContentTitle("Reminder")
                .setContentText(randomMessage)
                .setSmallIcon(R.drawable.cerevia)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent) // Set the pending intent for click action
                .build()

            notificationManager.notify(Random.nextInt(), notification)

            // Trigger vibration
            triggerVibration(context)
        } else {
            Log.e("ReminderReceiver", "No permission to show notifications.")
        }
    }

    private fun triggerVibration(context: Context) {
        val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(
                VibrationEffect.createOneShot(
                    500, VibrationEffect.DEFAULT_AMPLITUDE
                )
            )
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(500)
        }
    }
}
