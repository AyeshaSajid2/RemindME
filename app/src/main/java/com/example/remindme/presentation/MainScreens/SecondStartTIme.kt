package com.example.remindme.presentation.MainScreens

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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import com.example.remindme.presentation.theme.ButtonDark
import com.example.remindme.presentation.theme.ButtonLight
import com.example.remindme.presentation.theme.TextDark
import com.example.remindme.presentation.theme.TextLight
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun SelectStartTimeScreenSecond(navController: NavController) {
    val context = LocalContext.current
    val isDarkTheme = androidx.compose.foundation.isSystemInDarkTheme()
    val selectedTime = remember { mutableStateOf(LocalTime.now()) }
    val showDialog = remember { mutableStateOf(false) }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        val maxWidth = maxWidth
        val isCompact = maxWidth < 600.dp // Adjust UI for small screens
        val isRound = maxWidth == maxHeight // Determine if the screen is circular

        // Make the screen responsive based on shape
        val containerShape = if (isRound) MaterialTheme.shapes.small else MaterialTheme.shapes.medium

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Select Start Time",
                style = if (isCompact) MaterialTheme.typography.titleMedium else MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(if (isCompact) 8.dp else 16.dp))

            Text(
                text = selectedTime.value.format(DateTimeFormatter.ofPattern("hh:mm a")),
                style = if (isCompact) MaterialTheme.typography.bodyMedium else MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(if (isCompact) 8.dp else 16.dp))

            Button(
                onClick = { showDialog.value = true },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isDarkTheme) ButtonDark else ButtonLight,
                    contentColor = if (isDarkTheme) TextDark else TextLight
                ),
                modifier = Modifier
                    .width(if (isCompact) maxWidth * 0.8f else maxWidth * 0.6f)
                    .height(if (isCompact) 50.dp else 60.dp) // Adjust button size for compact screens
                    .clip(containerShape) // Make the button shape adaptive
            ) {
                Text("Pick Time")
            }

            Spacer(modifier = Modifier.height(if (isCompact) 8.dp else 16.dp))

            Button(
                onClick = {
                    val sharedPreferences = context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
                    sharedPreferences.edit()
                        .putString("start_time_2", selectedTime.value.format(DateTimeFormatter.ofPattern("hh:mm a")))
                        .apply()

                    Toast.makeText(
                        context,
                        "Save time: ${selectedTime.value.format(DateTimeFormatter.ofPattern("h:mm a"))}",
                        Toast.LENGTH_SHORT
                    ).show()

                    navController.navigate("interval_two") {
                        popUpTo("select_start_time_screen_2") { inclusive = true }
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isDarkTheme) ButtonLight else ButtonDark,
                    contentColor = if (isDarkTheme) TextDark else TextLight
                ),
                modifier = Modifier
                    .width(if (isCompact) maxWidth * 0.8f else maxWidth * 0.6f)
                    .height(if (isCompact) 50.dp else 60.dp) // Adjust button size for compact screens
                    .clip(containerShape) // Make the button shape adaptive
            ) {
                Text("Save Time")
            }

            if (showDialog.value) {
                CustomTimePickerDialog1(
                    currentHour = selectedTime.value.hour,
                    currentMinute = selectedTime.value.minute,
                    currentAmPm = if (selectedTime.value.hour < 12) 0 else 1,
                    onTimeSelected = { hour, minute, amPm ->
                        val finalHour = if (amPm == 1) (hour % 12) + 12 else hour % 12
                        selectedTime.value = LocalTime.of(finalHour, minute)
                        showDialog.value = false
                    },
                    onCancel = { showDialog.value = false },
                    isRound = isRound // Pass the screen shape to the dialog
                )
            }
        }
    }
}

@Composable
fun CustomTimePickerDialog(
    currentHour: Int,
    currentMinute: Int,
    currentAmPm: Int,
    onTimeSelected: (Int, Int, Int) -> Unit,
    onCancel: () -> Unit
) {
    Dialog(onDismissRequest = { onCancel() }) {
        BoxWithConstraints {
            val maxHeight = maxHeight
            val isCompact = maxHeight < 200.dp // Adjust layout for smaller screens

            Surface(
                shape = MaterialTheme.shapes.medium,
                tonalElevation = 6.dp,
                color = MaterialTheme.colorScheme.surface,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .background(MaterialTheme.colorScheme.background)
                        .verticalScroll(rememberScrollState()) // Enable vertical scrolling
                        .heightIn(max = maxHeight * 0.8f), // Limit height to a portion of screen height
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Select Time",
                        style = if (isCompact) MaterialTheme.typography.titleMedium else MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    var selectedHour by remember { mutableStateOf(currentHour % 12) }
                    var selectedMinute by remember { mutableStateOf(currentMinute) }
                    var selectedAmPm by remember { mutableStateOf(currentAmPm) }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        NumberPicker(
                            value = selectedHour,
                            range = 0..11,
                            onValueChange = { selectedHour = it }
                        )

                        NumberPicker(
                            value = selectedMinute,
                            range = 0..59,
                            onValueChange = { selectedMinute = it }
                        )

                        NumberPicker(
                            value = selectedAmPm,
                            range = 0..1,
                            onValueChange = { selectedAmPm = it },
                            labels = listOf("AM", "PM")
                        )
                    }

//                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Button(
                            onClick = { onCancel() },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = ButtonDark,
                                contentColor = TextDark
                            ),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Cancel")
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(
                            onClick = {
                                onTimeSelected(selectedHour, selectedMinute, selectedAmPm)
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = ButtonLight,
                                contentColor = TextDark
                            ),
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("OK")
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun NumberPicker(
    value: Int,
    range: IntRange,
    onValueChange: (Int) -> Unit,
    labels: List<String>? = null
) {
    AndroidView(
        factory = { context ->
            android.widget.NumberPicker(context).apply {
                minValue = range.first
                maxValue = range.last
                wrapSelectorWheel = true
                setOnValueChangedListener { _, _, newValue -> onValueChange(newValue) }
            }
        },
        update = { picker ->
            picker.value = value
            labels?.let { picker.displayedValues = it.toTypedArray() }
        }
    )
}

@Preview(device = Devices.WEAR_OS_SMALL_ROUND, showSystemUi = true)
@Composable
fun DefaultPreview2() {
    SelectStartTimeScreenSecond(navController = NavController(LocalContext.current))
}
