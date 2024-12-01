package com.example.remindme.presentation.MainScreens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.remindme.presentation.theme.ButtonDark
import com.example.remindme.presentation.theme.ButtonLight
import com.example.remindme.presentation.theme.TextDark
import com.example.remindme.presentation.theme.TextLight

@Composable
fun FrequencyPickingScreen(navController: NavController) {
    val context = LocalContext.current
    val isDarkTheme = androidx.compose.foundation.isSystemInDarkTheme()

    val frequencies = listOf(10, 20, 30, 40, 50, 60)

    // Load saved frequency or default to 10
    val sharedPreferences = context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
    var selectedFrequency by remember { mutableStateOf(sharedPreferences.getInt("frequency", 10)) }
    var isEditing by remember { mutableStateOf(false) }
    var newFrequency by remember { mutableStateOf(selectedFrequency) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Select Frequency",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(frequencies.size) { index ->
                val frequency = frequencies[index]
                Button(
                    onClick = {
                        if (isEditing) {
                            newFrequency = frequency
                        } else {
                            Toast.makeText(context, "Switch to Edit Mode to change frequency", Toast.LENGTH_SHORT).show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedFrequency == frequency || newFrequency == frequency) {
                            if (isDarkTheme) ButtonDark else ButtonLight
                        } else {
                            Color.Gray
                        },
                        contentColor = if (isDarkTheme) TextDark else TextLight
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(text = "$frequency")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (isEditing) {
                    selectedFrequency = newFrequency
                    sharedPreferences.edit().putInt("frequency", newFrequency).apply()
                    Toast.makeText(context, "Frequency updated to: $newFrequency", Toast.LENGTH_SHORT).show()
                    navController.navigate("home_screen") {
                        popUpTo("frequency_picking_screen") { inclusive = true }
                    }
                }
                isEditing = !isEditing
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isDarkTheme) ButtonDark else ButtonLight,
                contentColor = if (isDarkTheme) TextDark else TextLight
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (isEditing) "Save" else "Edit")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    FrequencyPickingScreen(navController = NavController(LocalContext.current))
}
