    package com.example.remindme.presentation

    import IntervalPickerScreen
    import SelectEndTimeScreen

    import android.content.Context
    import android.os.Build
    import android.os.Bundle
    import androidx.activity.ComponentActivity
    import androidx.activity.compose.setContent
    import androidx.annotation.RequiresApi
    import androidx.compose.material3.MaterialTheme
    import androidx.compose.material3.Surface
    import androidx.compose.runtime.Composable
    import androidx.compose.ui.platform.LocalContext
    import androidx.navigation.compose.NavHost
    import androidx.navigation.compose.composable
    import androidx.navigation.compose.rememberNavController
    import com.example.remindme.presentation.MainScreens.DetailScreen
    import com.example.remindme.presentation.MainScreens.FrequencyPickingScreen
    import com.example.remindme.presentation.MainScreens.HomeScreen
    import com.example.remindme.presentation.MainScreens.IntervalOne
    import com.example.remindme.presentation.MainScreens.IntervalThree
    import com.example.remindme.presentation.MainScreens.IntervalTwo
    //import com.example.remindme.presentation.MainScreens.IntervalPickerScreen
    import com.example.remindme.presentation.MainScreens.SelectDaysScreen
    import com.example.remindme.presentation.MainScreens.SelectEndTimeScreenSecond
    import com.example.remindme.presentation.MainScreens.SelectEndTimeScreenthree
    import com.example.remindme.presentation.MainScreens.SelectStartTimeScreen
    import com.example.remindme.presentation.MainScreens.SelectStartTimeScreenSecond
    import com.example.remindme.presentation.MainScreens.SelectStartTimeScreenthree
    import com.example.remindme.presentation.Remainder.createNotificationChannel
    //import com.example.remindme.presentation.MainScreens.SelectEndTimeScreen
    //import com.example.remindme.presentation.MainScreens.SelectStartTimeScreen
    import com.example.remindme.presentation.theme.RemindMeTheme
    import com.example.remindme.presentation.utils.PermissionUtils

    //import com.example.remindme.presentation.MainScreens.SelectStartTimeScreen

    //import com.example.remindme.presentation.theme.screens.SelectStartTimeScreen

    class MainActivity : ComponentActivity() {
        @RequiresApi(Build.VERSION_CODES.S)
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            createNotificationChannel(this)
            PermissionUtils.requestNotificationPermission(this)

            setContent {
                RemindMeTheme {
                    AppNavigation()
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
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
            composable("interval_one") {
                // Replace this with your actual home screen composable
                IntervalOne(navController = navController)
            }

            composable("interval_two") {
                // Replace this with your actual home screen composable
                IntervalTwo(navController = navController)
            }

            composable("interval_three") {
                // Replace this with your actual home screen composable
                IntervalThree(navController = navController)
            }
            composable("select_start_time") {
                SelectStartTimeScreen(navController = navController)
            }
            composable("select_end_time") {
                SelectEndTimeScreen(navController = navController)
            }
            composable("select_frequency") {
                FrequencyPickingScreen(navController= navController)
            }
            composable("show_details") {
                DetailScreen(navController= navController)
            }
            composable("set_reminder") {
    //            SetReminderScreen()
            }
            composable("select_start_time_2") {
                SelectStartTimeScreenSecond(navController = navController)
            }
            composable("select_end_time_2") {
                SelectEndTimeScreenSecond(navController = navController )
            }
//third Interval
            composable("select_start_time_3") {
                SelectStartTimeScreenthree(navController = navController)
            }
            composable("select_end_time_3") {
                SelectEndTimeScreenthree(navController = navController )
            }
        }
    }
