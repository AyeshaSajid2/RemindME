package com.example.remindme.presentation.MainScreens

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.remindme.presentation.theme.RemindMeTheme
import com.example.remindme.presentation.theme.ButtonLight
import com.example.remindme.presentation.theme.ButtonDark
import com.example.remindme.presentation.theme.BackgroundLight
import com.example.remindme.presentation.theme.BackgroundDark
import com.example.remindme.presentation.theme.TextLight
import com.example.remindme.presentation.theme.TextDark

class DaysSelectionActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Set up NavController for navigation
            val navController = rememberNavController()
            val context = this@DaysSelectionActivity

            // Call the function that renders the UI
            SelectDaysScreen(navController = navController, context = context)
        }
    }
}

@Composable
fun SelectDaysScreen(navController: NavController, context: Context) {
    // Days of the week
    val daysOfWeek = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
    val selectedDays = remember { mutableStateListOf<String>() }

    // Load previously saved days from SharedPreferences
    val sharedPreferences: SharedPreferences = context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
    val savedDays = sharedPreferences.getStringSet("selected_days", setOf()) ?: setOf()

    // Set previously saved days
    selectedDays.addAll(savedDays)

    // State to toggle between "Edit" and "Save"
    var isEditing by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Display days in a grid-like format (2 per row)
        daysOfWeek.chunked(2).forEach { row ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                row.forEach { day ->
                    DayBox(day = day, isSelected = selectedDays.contains(day), isEditing = isEditing) {
                        if (isEditing) {
                            if (selectedDays.contains(day)) {
                                selectedDays.remove(day)
                            } else {
                                selectedDays.add(day)
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Button row with Save and Edit buttons
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Save Button
            Button(
                onClick = {
                    if (isEditing) {
                        // Save selected days to SharedPreferences
                        sharedPreferences.edit().putStringSet("selected_days", selectedDays.toSet()).apply()
                        // Display a toast message
                        Toast.makeText(context, "Days saved!", Toast.LENGTH_SHORT).show()
                    }
                    // Toggle Edit/Save mode
                    isEditing = !isEditing
                },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (androidx.compose.foundation.isSystemInDarkTheme()) ButtonDark else ButtonLight,
                    contentColor = if (androidx.compose.foundation.isSystemInDarkTheme()) TextDark else TextLight
                )
            ) {
                Text(text = if (isEditing) "Save" else "Edit")
            }

            // Edit Button (only enabled if not in editing mode)
            if (!isEditing) {
                Button(
                    onClick = {
                        isEditing = true
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (androidx.compose.foundation.isSystemInDarkTheme()) ButtonDark else ButtonLight,
                        contentColor = if (androidx.compose.foundation.isSystemInDarkTheme()) TextDark else TextLight
                    )
                ) {
                    Text(text = "Edit")
                }
            }
        }
    }
}

@Composable
fun DayBox(day: String, isSelected: Boolean, isEditing: Boolean, onClick: () -> Unit) {
    Surface(
        modifier = Modifier
            .size(70.dp) // Reduced size for the day boxes
            .padding(8.dp) // Adjusted padding for a cleaner look
            .clickable(enabled = isEditing, onClick = onClick), // Only clickable when editing
        shape = RoundedCornerShape(12.dp),
        color = if (isSelected) Color.Green else Color.LightGray
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text(text = day, color = Color.White)
        }
    }
}

@Preview(device = Devices.PHONE, showSystemUi = true)
@Composable
fun SelectDaysScreenPreview() {
    RemindMeTheme {
        SelectDaysScreen(navController = rememberNavController(), context = LocalContext.current)
    }
}
