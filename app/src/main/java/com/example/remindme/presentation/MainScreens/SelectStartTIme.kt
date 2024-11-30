package com.example.remindme.presentation.theme.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import com.example.remindme.presentation.widgets.TimePickerDialogWithButtons

@Composable
fun SelectStartTimeScreen(navController: NavController) {
    // Define state for the selected time
    val selectedTime = remember { mutableStateOf(LocalTime.now()) }
    // Flag to show the time picker dialog
    val openDialog = remember { mutableStateOf(false) }

    // Get context for SharedPreferences
    val context = LocalContext.current

    // Handle showing the time picker dialog
    if (openDialog.value) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            // Call TimePickerDialogWithButtons Composable
            TimePickerDialogWithButtons(
                context = context,
                onTimeSelected = { time ->
                    selectedTime.value = time
                    openDialog.value = false
                },
                onDismiss = {
                    openDialog.value = false
                }
            )
        }
    }

    // UI Layout for the screen itself
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Select Start Time", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(16.dp))

        // Display the selected time
        Text(
            text = "Selected Time: ${selectedTime.value.format(DateTimeFormatter.ofPattern("hh:mm a"))}",
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Button to open the time picker dialog
        Button(
            onClick = { openDialog.value = true },
            modifier = Modifier.fillMaxWidth(0.5f) // Center and set width
        ) {
            Text(text = "Pick Time")
        }

        // Save Button at the bottom of the dialog
        Spacer(modifier = Modifier.height(16.dp))

        // Display the Save button only when the time has been selected
        if (selectedTime.value != LocalTime.now()) {
            Button(
                onClick = {
                    // Save the selected time to SharedPreferences
                    val sharedPreferences = context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
                    sharedPreferences.edit().putString("start_time", selectedTime.value.format(DateTimeFormatter.ofPattern("hh:mm a"))).apply()

                    // Show a toast to confirm the time is saved
                    Toast.makeText(context, "Start Time saved!", Toast.LENGTH_SHORT).show()

                    // Navigate to the home screen
                    navController.navigate("home_screen") {
                        // Pop the current screen from the stack so the user can't go back to it
                        popUpTo("select_start_time_screen") { inclusive = true }
                    }
                },
                modifier = Modifier.fillMaxWidth(0.5f) // Center and set width
            ) {
                Text(text = "Save Time")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    SelectStartTimeScreen(navController = NavController(LocalContext.current))
}
