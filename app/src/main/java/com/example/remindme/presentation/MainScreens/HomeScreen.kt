package com.example.remindme.presentation.MainScreens

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.wear.compose.material.Scaffold
import com.example.remindme.presentation.theme.RemindMeTheme
import com.example.remindme.presentation.theme.ButtonLight
import com.example.remindme.presentation.theme.ButtonDark
import com.example.remindme.presentation.theme.BackgroundLight
import com.example.remindme.presentation.theme.BackgroundDark
import com.example.remindme.presentation.theme.TextLight
import com.example.remindme.presentation.theme.TextDark

@Composable
fun HomeScreen(navController: NavHostController) {
    // Scaffold to hold the content
    Scaffold(
        timeText = { Text(text = "Time here") } // Add the time text for WearOS
    ) {
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
                HomeButton("Select Days") {
                    navController.navigate("select_days")
                }
                HomeButton("Select Start Time") {
                    navController.navigate("select_start_time")
                }
                HomeButton("Select End Time") {
                    navController.navigate("select_end_time")
                }
                HomeButton("Select Frequency") {
                    navController.navigate("select_frequency")
                }
                HomeButton("Show Details") {
                    navController.navigate("show_details")
                }
                HomeButton("Set Reminder") {
                    navController.navigate("set_reminder")
                }
            }
        }
    }
}

@Composable
fun HomeButton(label: String, onClick: () -> Unit) {
    val isDarkTheme = androidx.compose.foundation.isSystemInDarkTheme()
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth(0.8f)
            .padding(horizontal = 8.dp)
            .padding(vertical = 8.dp), // Added vertical padding
        colors = androidx.compose.material3.ButtonDefaults.buttonColors(
            containerColor = if (isDarkTheme) ButtonDark else ButtonLight,
            contentColor = if (isDarkTheme) TextDark else TextLight
        )
    ) {
        Text(text = label)
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    RemindMeTheme {
        HomeScreen(navController = NavHostController(LocalContext.current))
    }
}