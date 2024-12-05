package com.example.remindme.presentation.Remainder

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.remindme.presentation.Remainder.ReminderScheduler

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        if (intent?.action == Intent.ACTION_BOOT_COMPLETED || intent?.action == "RESCHEDULE_ALARMS") {
            val sharedPreferences = context.getSharedPreferences("ReminderPrefs", Context.MODE_PRIVATE)
            ReminderScheduler.rescheduleAlarms(context, sharedPreferences)
        }
    }
}

