package com.example.remindme.presentation.theme.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.ui.window.Dialog
import com.example.remindme.presentation.widgets.TimePickerDialogWithButtons
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun SelectStartTimeScreen(navController: NavController) {
    val selectedTime = remember { mutableStateOf(LocalTime.now()) }
    val showTimePicker = remember { mutableStateOf(false) }
    val showConfirmationDialog = remember { mutableStateOf(false) }
    val context = LocalContext.current

    // Time Picker Dialog
    if (showTimePicker.value) {
        Dialog(onDismissRequest = { showTimePicker.value = false }) {
            TimePickerDialogWithButtons(
                context = context,
                onTimeSelected = { time ->
                    selectedTime.value = time
                    showTimePicker.value = false
                    showConfirmationDialog.value = true // Show confirmation dialog after selection
                },
                onDismiss = {
                    showTimePicker.value = false
                }
            )
        }
    }

    // Confirmation Dialog
    if (showConfirmationDialog.value) {
        AlertDialog(
            onDismissRequest = { showConfirmationDialog.value = false },
            confirmButton = {
                TextButton(onClick = {
                    showConfirmationDialog.value = false
                    Toast.makeText(context, "Time Confirmed!", Toast.LENGTH_SHORT).show()
                }) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showConfirmationDialog.value = false
                    showTimePicker.value = true // Reopen time picker if user cancels
                }) {
                    Text("Reselect")
                }
            },
            title = { Text("Confirm Time") },
            text = { Text("You selected: ${selectedTime.value.format(DateTimeFormatter.ofPattern("hh:mm a"))}") }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Select Start Time",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .padding(16.dp)
                .background(MaterialTheme.colorScheme.primary, shape = MaterialTheme.shapes.medium)
                .padding(16.dp)
        ) {
            Text(
                text = selectedTime.value.format(DateTimeFormatter.ofPattern("hh:mm a")),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { showTimePicker.value = true },
            modifier = Modifier.fillMaxWidth(0.6f),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Text("Pick Time")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val sharedPreferences = context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
                sharedPreferences.edit()
                    .putString("start_time", selectedTime.value.format(DateTimeFormatter.ofPattern("hh:mm a")))
                    .apply()
                Toast.makeText(context, "Start Time saved!", Toast.LENGTH_SHORT).show()
                navController.navigate("home_screen") {
                    popUpTo("select_start_time_screen") { inclusive = true }
                }
            },
            modifier = Modifier.fillMaxWidth(0.6f),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondary
            )
        ) {
            Text("Save Time")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    SelectStartTimeScreen(navController = NavController(LocalContext.current))
}
