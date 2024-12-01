package com.example.remindme.presentation.utils

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat

object PermissionUtils {

    // Check if the app has notification permission (for Android 13+)
    fun checkNotificationPermission(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
        } else {
            // For Android versions before 13, notification permission is granted by default
            true
        }
    }

    // Show a dialog if permission is not granted
    fun requestNotificationPermission(context: Context) {
        if (!checkNotificationPermission(context)) {
            showPermissionDialog(context)
        }
    }

    // Show dialog to guide the user to settings if permission is not granted
    private fun showPermissionDialog(context: Context) {
        AlertDialog.Builder(context)
            .setTitle("Notification Permission Required")
            .setMessage("We need permission to send you reminders. Please enable it in the settings.")
            .setPositiveButton("Go to Settings") { _, _ ->
                navigateToSettings(context)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    // Navigate to the app's notification settings
    private fun navigateToSettings(context: Context) {
        val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
            putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
        }
        context.startActivity(intent)
    }
}
