package com.example.remindme.presentation.MainScreens

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.remindme.presentation.theme.RemindMeTheme
import com.example.remindme.presentation.theme.*

class DaysSelectionActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            val context = this@DaysSelectionActivity
            SelectDaysScreen(navController = navController, context = context)
        }
    }
}

@Composable
fun SelectDaysScreen(navController: NavController, context: Context) {
    val daysOfWeek = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")
    val sharedPreferences: SharedPreferences = context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)

    // Initialize with "Monday" if empty
    val initialDays = sharedPreferences.getStringSet("selected_days", setOf("Monday"))!!
    val selectedDays = remember { mutableStateListOf<String>().apply { addAll(initialDays) } }
    var isEditing by remember { mutableStateOf(false) }

    // Check device shape (Circular or Rectangular)
    val isCircularShape = isDeviceCircular(context)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(if (androidx.compose.foundation.isSystemInDarkTheme()) BackgroundDark else BackgroundLight),
        contentAlignment = Alignment.Center
    ) {
        // Make the entire screen scrollable
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .padding(12.dp)
                .align(Alignment.Center)
                .verticalScroll(rememberScrollState()) // Make the column scrollable
        ) {
            // Days layout based on device shape
            if (isCircularShape) {
                // Circular layout for days
                CircularDaySelector(
                    daysOfWeek = daysOfWeek,
                    selectedDays = selectedDays,
                    isEditing = isEditing
                ) { day ->
                    if (isEditing) {
                        if (selectedDays.contains(day)) {
                            selectedDays.remove(day)
                        } else {
                            selectedDays.add(day)
                        }
                    } else {
                        Toast.makeText(context, "Enable edit mode to select days", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                // Rectangular layout for days
                RectangularDaySelector(
                    daysOfWeek = daysOfWeek,
                    selectedDays = selectedDays,
                    isEditing = isEditing
                ) { day ->
                    if (isEditing) {
                        if (selectedDays.contains(day)) {
                            selectedDays.remove(day)
                        } else {
                            selectedDays.add(day)
                        }
                    } else {
                        Toast.makeText(context, "Enable edit mode to select days", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Edit/Save button
            Button(
                onClick = {
                    if (isEditing) {
                        sharedPreferences.edit()
                            .clear()
                            .putStringSet("selected_days", selectedDays.toSet())
                            .apply()

                        Log.d("SharedPreferences", "Saved days: ${selectedDays.joinToString(", ")}")
                        Toast.makeText(context, "Saved days successfully!", Toast.LENGTH_SHORT).show()

                        navController.navigate("home_screen") {
                            popUpTo("frequency_picking_screen") { inclusive = true }
                        }
                    }
                    isEditing = !isEditing
                },
                modifier = Modifier
                    .fillMaxWidth() // Button takes full width of screen
                    .padding(16.dp)
                    .clip(RoundedCornerShape(25.dp)), // Rounded corners
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (androidx.compose.foundation.isSystemInDarkTheme()) ButtonDark else ButtonLight,
                    contentColor = if (androidx.compose.foundation.isSystemInDarkTheme()) TextDark else TextLight
                )
            ) {
                Text(text = if (isEditing) "Save" else "Edit")
            }
        }
    }
}

@Composable
fun CircularDaySelector(
    daysOfWeek: List<String>,
    selectedDays: List<String>,
    isEditing: Boolean,
    onDayClick: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .size(180.dp) // Circle size for Wear OS
            .background(Color.Transparent),
        contentAlignment = Alignment.Center
    ) {
        daysOfWeek.forEachIndexed { index, day ->
            val angle = Math.toRadians((index * 360.0 / daysOfWeek.size) - 90)
            val xOffset = 70.dp * Math.cos(angle).toFloat()
            val yOffset = 70.dp * Math.sin(angle).toFloat()

            Box(
                modifier = Modifier
                    .offset(x = xOffset, y = yOffset)
                    .size(40.dp) // Smaller size for Wear OS
                    .background(
                        color = if (selectedDays.contains(day)) Color(0xFFACECA1) else Color.LightGray,
                        shape = RoundedCornerShape(50)
                    )
                    .clickable(enabled = true) { onDayClick(day) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = day.take(3), // Use abbreviations for better fit
                    color = if (selectedDays.contains(day)) Color.Black else Color.White,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
fun RectangularDaySelector(
    daysOfWeek: List<String>,
    selectedDays: List<String>,
    isEditing: Boolean,
    onDayClick: (String) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        contentPadding = PaddingValues(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(daysOfWeek) { day ->
            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .size(60.dp)
                    .background(
                        color = if (selectedDays.contains(day)) Color(0xFFACECA1) else Color.LightGray,
                        shape = RoundedCornerShape(12.dp)
                    )
                    .clickable(enabled = true) { onDayClick(day) },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = day.take(3), // Use abbreviations for better fit
                    color = if (selectedDays.contains(day)) Color.Black else Color.White,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

fun isDeviceCircular(context: Context): Boolean {
    // Check if the device shape is circular (for Wear OS)
    val screenHeight = context.resources.displayMetrics.heightPixels
    val screenWidth = context.resources.displayMetrics.widthPixels
    return screenWidth == screenHeight
}

@Preview(device = Devices.WEAR_OS_SMALL_ROUND, showSystemUi = true)
@Composable
fun SelectDaysScreenWearOSPreview() {
    RemindMeTheme {
        SelectDaysScreen(navController = rememberNavController(), context = LocalContext.current)
    }
}

@Preview(device = Devices.WEAR_OS_SQUARE, showSystemUi = true)
@Composable
fun SelectDaysScreenPhonePreview() {
    RemindMeTheme {
        SelectDaysScreen(navController = rememberNavController(), context = LocalContext.current)
    }
}


