package com.example.remindme.presentation

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.remindme.presentation.MainScreens.HomeScreen
import com.example.remindme.presentation.MainScreens.SelectDaysScreen
import com.example.remindme.presentation.MainScreens.SelectEndTimeScreen
//import com.example.remindme.presentation.MainScreens.SelectStartTimeScreen
import com.example.remindme.presentation.theme.RemindMeTheme
//import com.example.remindme.presentation.MainScreens.SelectStartTimeScreen
import com.example.remindme.presentation.theme.screens.SelectStartTimeScreen

//import com.example.remindme.presentation.theme.screens.SelectStartTimeScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RemindMeTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    // Register all the routes here
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(navController)
        }
        composable("select_days") {
            SelectDaysScreen(navController = navController, context = LocalContext.current)
        }

        composable("home_screen") {
            // Replace this with your actual home screen composable
            HomeScreen(navController = navController)
        }
        composable("select_start_time") {
            SelectStartTimeScreen(navController = navController)
        }
        composable("select_end_time") {
         SelectEndTimeScreen(navController = navController)
        }
        composable("select_frequency") {
//            SelectFrequencyScreen()
        }
        composable("show_details") {
//            ShowDetailsScreen()
        }
        composable("set_reminder") {
//            SetReminderScreen()
        }
    }
}
