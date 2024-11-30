package com.example.remindme.presentation.widgets

import android.app.TimePickerDialog
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*

@Composable
fun TimePickerDialogWithButtons(
    context: Context,
    onTimeSelected: (LocalTime) -> Unit,
    onDismiss: () -> Unit
) {
    val selectedTime = remember { mutableStateOf<LocalTime?>(null) }
    val timeFormatter = DateTimeFormatter.ofPattern("hh:mm a")

    // TimePicker Dialog logic
    fun showTimePickerDialog() {
        val calendar = Calendar.getInstance()
        val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
        val currentMinute = calendar.get(Calendar.MINUTE)

        TimePickerDialog(
            context,
            { _, hourOfDay, minute ->
                val selectedLocalTime = LocalTime.of(hourOfDay, minute)
                selectedTime.value = selectedLocalTime
                onTimeSelected(selectedLocalTime)
            },
            currentHour,
            currentMinute,
            false // Set to false for 12-hour format, true for 24-hour format
        ).show()
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface, shape = MaterialTheme.shapes.medium)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Select Time",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Display the selected time
        Text(
            text = selectedTime.value?.format(timeFormatter) ?: "No Time Selected",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { showTimePickerDialog() },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Text("Pick Time")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError
                )
            ) {
                Text("Cancel")
            }

            Button(
                onClick = {
                    if (selectedTime.value != null) {
                        onDismiss() // Close dialog after confirming
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.onSecondary
                )
            ) {
                Text("Confirm")
            }
        }
    }
}
