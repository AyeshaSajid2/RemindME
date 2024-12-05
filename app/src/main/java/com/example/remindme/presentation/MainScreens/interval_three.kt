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

import android.os.Build

import androidx.annotation.RequiresApi

import androidx.activity.compose.BackHandler
import com.example.remindme.presentation.widgets.HomeButton

@RequiresApi(Build.VERSION_CODES.S)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun IntervalThree(navController: NavHostController) {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)

    // State to hold the current time
    var currentTime by remember { mutableStateOf(getFormattedTime()) }
    var currentTimeMillis by remember { mutableStateOf(System.currentTimeMillis()) }

    BackHandler {
        navController.navigate("home_screen") {
            // Optionally, you can specify that this screen should be removed from the back stack
            popUpTo("select_start_time_screen_3") { inclusive = true }
        }
    }
    // Update time every second
    LaunchedEffect(Unit) {
        while (true) {
            delay(1000L) // Update every second
            currentTime = getFormattedTime()
            currentTimeMillis = System.currentTimeMillis()
        }
    }

    // Handle back press to close the app
//    BackHandler {
//        // This will close the app when back is pressed
//        (context as? android.app.Activity)?.finish()
//    }

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

                // Button to navigate to Select Start Time screen
                HomeButton("Select Start Time") {
                    navController.navigate("select_start_time_3")
                }
                // Button to navigate to Select End Time screen
                HomeButton("Select End Time") {
                    navController.navigate("select_end_time_3")
                }
            }
        }
    }
}



