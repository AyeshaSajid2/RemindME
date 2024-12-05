import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import java.time.LocalTime

class IntervalPickerActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                IntervalPickerScreen()
            }
        }
    }
}

@Composable
fun IntervalPickerScreen() {
    var startTime by remember { mutableStateOf<LocalTime?>(null) }
    var endTime by remember { mutableStateOf<LocalTime?>(null) }
    var isStartDialogOpen by remember { mutableStateOf(false) }
    var isEndDialogOpen by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Selected Interval:",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Start: ${startTime?.toString() ?: "Not Set"}")
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "End: ${endTime?.toString() ?: "Not Set"}")

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = { isStartDialogOpen = true }) {
            Text(text = "Set Start Time")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = {
            if (startTime == null) {
                Toast.makeText(context, "Please set the start time first.", Toast.LENGTH_SHORT).show()
            } else {
                isEndDialogOpen = true
            }
        }) {
            Text(text = "Set End Time")
        }

        if (isStartDialogOpen) {
            TimePickerDialog(
                onTimeSelected = {
                    startTime = it
                    isStartDialogOpen = false
                },
                onDismiss = { isStartDialogOpen = false }
            )
        }

        if (isEndDialogOpen) {
            TimePickerDialog(
                onTimeSelected = {
                    if (startTime != null && it.isAfter(startTime)) {
                        endTime = it
                        isEndDialogOpen = false
                    } else {
                        Toast.makeText(context, "End time must be after start time.", Toast.LENGTH_SHORT).show()
                    }
                },
                onDismiss = { isEndDialogOpen = false }
            )
        }
    }
}

@Composable
fun TimePickerDialog(
    onTimeSelected: (LocalTime) -> Unit,
    onDismiss: () -> Unit
) {
    var hour by remember { mutableStateOf(0) }
    var minute by remember { mutableStateOf(0) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Pick a Time") },
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
                    NumberPicker(value = hour, onValueChange = { hour = it }, range = 0..23)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(":")
                    Spacer(modifier = Modifier.width(8.dp))
                    NumberPicker(value = minute, onValueChange = { minute = it }, range = 0..59)
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                onTimeSelected(LocalTime.of(hour, minute))
            }) {
                Text(text = "OK")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text(text = "Cancel")
            }
        }
    )
}

@Composable
fun NumberPicker(value: Int, onValueChange: (Int) -> Unit, range: IntRange) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Button(onClick = { if (value < range.last) onValueChange(value + 1) }) {
            Text(text = "+")
        }
        Text(text = value.toString(), style = MaterialTheme.typography.headlineMedium)
        Button(onClick = { if (value > range.first) onValueChange(value - 1) }) {
            Text(text = "-")
        }
    }
}
