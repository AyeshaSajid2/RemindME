package com.example.remindme.presentation.In_App_Purchase

import android.content.Context
import android.content.SharedPreferences
import java.util.*

class TrialManager(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("trial_preferences", Context.MODE_PRIVATE)

    // Key to store the trial start date
    private val trialStartDateKey = "trial_start_date"

    // Save the trial start date when the user starts the trial
    fun setTrialStartDate() {
        val editor = sharedPreferences.edit()
        editor.putLong(trialStartDateKey, System.currentTimeMillis()) // Save current time in milliseconds
        editor.apply()
    }

    // Get the trial start date
    fun getTrialStartDate(): Long {
        return sharedPreferences.getLong(trialStartDateKey, 0L)
    }

    // Check if the trial is expired (after 1 week)
    fun isTrialExpired(): Boolean {
        val trialStartDate = getTrialStartDate()
        if (trialStartDate == 0L) return false // No trial started yet
        val currentTime = System.currentTimeMillis()
        val trialPeriodEnd = trialStartDate + (7 * 24 * 60 * 60 * 1000) // 1 week in milliseconds
        return currentTime > trialPeriodEnd
    }
}
