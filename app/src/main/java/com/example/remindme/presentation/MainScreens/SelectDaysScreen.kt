package com.example.remindme.presentation.MainScreens

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
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
            val navController = rememberNavController()
            val context = this@DaysSelectionActivity
            SelectDaysScreen(navController = navController, context = context)
        }
    }
}

@Composable
fun SelectDaysScreen(navController: NavController, context: Context) {
    val daysOfWeek = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
    val sharedPreferences: SharedPreferences = context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
    val savedDays = sharedPreferences.getStringSet("selected_days", setOf()) ?: setOf()
    val selectedDays = remember { mutableStateListOf<String>().apply { addAll(savedDays) } }
    var isEditing by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(if (androidx.compose.foundation.isSystemInDarkTheme()) BackgroundDark else BackgroundLight)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        daysOfWeek.chunked(2).forEach { row ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                row.forEach { day ->
                    DayBox(
                        day = day,
                        isSelected = selectedDays.contains(day),
                        isEditing = isEditing
                    ) {
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

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = {
                    if (isEditing) {
                        // Save the updated days to shared preferences
                        sharedPreferences.edit().putStringSet("selected_days", selectedDays.toSet()).apply()
                        Toast.makeText(context, "Saved days: ${selectedDays.joinToString(", ")}", Toast.LENGTH_SHORT).show()
                    }
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
        }
    }
}

@Composable
fun DayBox(day: String, isSelected: Boolean, isEditing: Boolean, onClick: () -> Unit) {
    val selectedColor = Color(0xFFACECA1)
    Surface(
        modifier = Modifier
            .size(70.dp)
            .padding(8.dp)
            .clickable(enabled = isEditing, onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        color = if (isSelected) selectedColor else Color.LightGray
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
