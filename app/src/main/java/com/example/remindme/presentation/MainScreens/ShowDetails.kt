package com.example.remindme.presentation.MainScreens

import android.content.Context
import android.content.SharedPreferences
import android.widget.Toast
import androidx.compose.foundation.background
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.remindme.presentation.theme.ButtonDark
import com.example.remindme.presentation.theme.ButtonLight
import com.example.remindme.presentation.theme.TextDark
import com.example.remindme.presentation.theme.TextLight

@Composable
fun DetailScreen(navController: NavController) {
    val context = LocalContext.current
    val isDarkTheme = androidx.compose.foundation.isSystemInDarkTheme()

    // SharedPreferences to fetch data
    val sharedPreferences = context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)

    // Fetch data from SharedPreferences
    val savedDays = sharedPreferences.getStringSet("selected_days", setOf())?.joinToString(", ") ?: "No days saved"
    val startTime = sharedPreferences.getString("start_time", "00:00")
    val endTime = sharedPreferences.getString("end_time", "23:59")
    val frequency = sharedPreferences.getInt("frequency", 10)

    // UI
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
            .verticalScroll(rememberScrollState())
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Details",
            style = MaterialTheme.typography.titleLarge.copy(
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            ),
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Display detail sections
        ResponsiveDetailCard(
            title = "Selected Days",
            value = savedDays,
            isDarkTheme = isDarkTheme
        )
        ResponsiveDetailCard(
            title = "Start Time",
            value = startTime.orEmpty(),
            isDarkTheme = isDarkTheme
        )
        ResponsiveDetailCard(
            title = "End Time",
            value = endTime.orEmpty(),
            isDarkTheme = isDarkTheme
        )
        ResponsiveDetailCard(
            title = "Frequency",
            value = "$frequency minutes",
            isDarkTheme = isDarkTheme
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Back Button
        Button(
            onClick = { navController.popBackStack() },
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isDarkTheme) ButtonDark else ButtonLight,
                contentColor = if (isDarkTheme) TextDark else TextLight
            ),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Go Back")
        }
    }
}

@Composable
fun ResponsiveDetailCard(title: String, value: String, isDarkTheme: Boolean) {
    val isScrollable = value.length > 30 // Adjust threshold for scrollable content

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .wrapContentHeight(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isDarkTheme) ButtonDark else ButtonLight
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            if (isScrollable) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 60.dp, max = 200.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = value,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Normal,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    )
                }
            } else {
                Text(
                    text = value,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Normal,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDetailScreen() {
    DetailScreen(navController = NavController(LocalContext.current))
}