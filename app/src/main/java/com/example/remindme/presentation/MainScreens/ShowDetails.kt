import android.content.Context
import android.content.SharedPreferences
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.TextUnit
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
    val endTime = sharedPreferences.getString("end_time", "00:00")
    val startTime2 = sharedPreferences.getString("start_time_2", "00:00")
    val endTime2 = sharedPreferences.getString("end_time_2", "00:00")
    val startTime3 = sharedPreferences.getString("start_time_3", "00:00")
    val endTime3 = sharedPreferences.getString("end_time_3", "00:00")
    val frequency = sharedPreferences.getInt("frequency", 10)

    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val fontSize = when {
        screenWidth < 350.dp -> 14.sp // Smaller font for smaller screens
        screenWidth < 450.dp -> 16.sp // Medium font for medium screens
        else -> 18.sp // Larger font for bigger screens
    }

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
                fontSize = fontSize,
                fontWeight = FontWeight.Bold
            ),
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Display detail sections
        ResponsiveDetailCard(
            title = "Selected Days",
            value = savedDays,
            isDarkTheme = isDarkTheme,
            fontSize = fontSize
        )
        ResponsiveDetailCard(
            title = "Start Time",
            value = startTime.orEmpty(),
            isDarkTheme = isDarkTheme,
            fontSize = fontSize
        )
        ResponsiveDetailCard(
            title = "End Time",
            value = endTime.orEmpty(),
            isDarkTheme = isDarkTheme,
            fontSize = fontSize
        )
        ResponsiveDetailCard(
            title = "Frequency",
            value = "$frequency minutes",
            isDarkTheme = isDarkTheme,
            fontSize = fontSize
        )
        ResponsiveDetailCard(
            title = "Interval 2 start time",
            value = startTime2.orEmpty(),
            isDarkTheme = isDarkTheme,
            fontSize = fontSize
        )
        ResponsiveDetailCard(
            title = "Interval 2 end time",
            value = endTime2.orEmpty(),
            isDarkTheme = isDarkTheme,
            fontSize = fontSize
        )

        ResponsiveDetailCard(
            title = "Interval 3 start time",
            value = startTime3.orEmpty(),
            isDarkTheme = isDarkTheme,
            fontSize = fontSize
        )
        ResponsiveDetailCard(
            title = "Interval 3 end time",
            value = endTime3.orEmpty(),
            isDarkTheme = isDarkTheme,
            fontSize = fontSize
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
fun ResponsiveDetailCard(title: String, value: String, isDarkTheme: Boolean, fontSize: TextUnit) {
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
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = fontSize
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
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontSize = fontSize
                        )
                    )
                }
            } else {
                Text(
                    text = value,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Normal,
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontSize = fontSize
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
