package com.example.remindme.presentation.MainScreens


import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import com.example.remindme.presentation.widgets.TimePickerDialogWithButtons
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.navigation.NavController

@Composable
fun SelectEndTimeScreen(navController: NavController) {
    // Define state for the selected end time
    var selectedEndTime by remember { mutableStateOf(LocalTime.now()) }

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
                    selectedEndTime = time
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
        Text(text = "Select End Time", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(16.dp))

        // Display the selected end time
        Text(
            text = "Selected End Time: ${selectedEndTime.format(DateTimeFormatter.ofPattern("hh:mm a"))}",
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Button to open the time picker dialog
        Button(onClick = { openDialog.value = true }) {
            Text(text = "Pick End Time")
        }

        // Save Button at the bottom of the dialog
        Spacer(modifier = Modifier.height(16.dp))

        // Display the Save button only when the end time has been selected
        if (selectedEndTime != LocalTime.now()) {
            Button(
                onClick = {
                    // Save the selected end time to SharedPreferences
                    val sharedPreferences = context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
                    val endTimeFormatted = selectedEndTime.format(DateTimeFormatter.ofPattern("hh:mm a"))
                    sharedPreferences.edit().putString("end_time", endTimeFormatted).apply()

                    // Log the saved end time
                    Log.d("SelectEndTimeScreen", "End Time saved: $endTimeFormatted")

                    // Show a toast to confirm the end time is saved
                    Toast.makeText(context, "End Time saved!", Toast.LENGTH_SHORT).show()

                    // Navigate to the home screen
                    navController.navigate("home_screen") {
                        // Pop the current screen from the stack so the user can't go back to it
                        popUpTo("select_end_time_screen") { inclusive = true }
                    }
                },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text(text = "Save End Time")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    SelectEndTimeScreen(navController = NavController(LocalContext.current))
}
