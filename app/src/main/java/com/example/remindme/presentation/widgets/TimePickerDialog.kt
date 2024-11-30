package com.example.remindme.presentation.widgets

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.example.remindme.presentation.theme.RemindMeTheme
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun TimePickerDialogWithButtons(
    context: Context = LocalContext.current,
    onTimeSelected: (LocalTime) -> Unit,
    onDismiss: () -> Unit
) {
    RemindMeTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Time Picker UI (you can replace this with a custom time picker layout)
            var selectedTime = LocalTime.now()

            Text(
                text = "Select Time",
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Display selected time
            Text(
                text = "Selected Time: ${selectedTime.format(DateTimeFormatter.ofPattern("hh:mm a"))}",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Button to open the time picker logic (this part simulates opening the native picker)
            Button(
                onClick = {
                    // Here you would open the time picker or use your custom logic
                    // For this example, we will simulate time selection
                    selectedTime = LocalTime.of(12, 30) // Simulated time selection
                    onTimeSelected(selectedTime)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Pick Time")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Button to save the selected time
            Button(
                onClick = {
                    // Save the selected time
                    val formattedTime = selectedTime.format(DateTimeFormatter.ofPattern("hh:mm a"))
                    val sharedPreferences = context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
                    sharedPreferences.edit().putString("start_time", formattedTime).apply()

                    // Show confirmation message
                    Toast.makeText(context, "Start Time saved!", Toast.LENGTH_SHORT).show()

                    // Close the dialog or navigate back
                    onDismiss()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Time")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Cancel Button
            Button(
                onClick = {
                    // Dismiss dialog without saving
                    onDismiss()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cancel")
            }
        }
    }
}
