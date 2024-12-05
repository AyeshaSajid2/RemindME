package com.example.remindme.presentation.widgets

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.remindme.presentation.theme.ButtonDark
import com.example.remindme.presentation.theme.ButtonLight
import com.example.remindme.presentation.theme.TextDark
import com.example.remindme.presentation.theme.TextLight

@Composable
fun HomeButton(label: String, onClick: () -> Unit) {
    val isDarkTheme = androidx.compose.foundation.isSystemInDarkTheme()
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth(0.8f)
            .padding(horizontal = 8.dp)
            .padding(vertical = 8.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isDarkTheme) ButtonDark else ButtonLight,
            contentColor = if (isDarkTheme) TextDark else TextLight
        )
    ) {
        Text(
            text = label,
            style = TextStyle(fontSize = 16.sp),
            color = if (isDarkTheme) TextDark else TextLight
        )
    }
}
