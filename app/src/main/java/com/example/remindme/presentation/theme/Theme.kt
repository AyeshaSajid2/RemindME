package com.example.remindme.presentation.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Colors
import androidx.wear.compose.material.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.Shapes

// Define custom colors
val ButtonLight = Color(0xFFC9F2C7) // Light green color for button in light theme
val ButtonDark = Color(0xFF629460) // Dark green color for button in dark theme
val BackgroundLight = Color.White // White background for light theme
val BackgroundDark = Color.Black // Black background for dark theme
internal val TextLight = Color.Black // Black text for light theme
val TextDark = Color.White // White text for dark theme

// Define color palettes
private val LightColorPalette = Colors(
    primary = ButtonLight,
    primaryVariant = ButtonLight,
    secondary = ButtonLight,
    onPrimary = TextLight, // Text color for buttons
    onSecondary = TextLight, // Text color for buttons
    surface = BackgroundLight,
    onSurface = TextLight,
    background = BackgroundLight,
    onBackground = TextLight
)

private val DarkColorPalette = Colors(
    primary = ButtonDark,
    primaryVariant = ButtonDark,
    secondary = ButtonDark,
    onPrimary = TextDark, // Text color for buttons
    onSecondary = TextDark, // Text color for buttons
    surface = BackgroundDark,
    onSurface = TextDark,
    background = BackgroundDark,
    onBackground = TextDark
)

// Define typography (you can customize these as needed)
private val WearTypography = Typography(
    body1 = androidx.compose.ui.text.TextStyle(
        color = Color.White
    )
)

// RemindMeTheme composable to switch between light and dark themes
@Composable
fun RemindMeTheme(
    darkTheme: Boolean = androidx.compose.foundation.isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColorPalette else LightColorPalette

    MaterialTheme(
        colors = colors,
        typography = WearTypography,
        shapes = Shapes(),  // Optional: Customize shapes as per your design
        content = content
    )
}
