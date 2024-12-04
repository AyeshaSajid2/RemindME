package com.example.remindme.presentation.MainScreens//package com.example.remindme.presentation.MainScreens
//
//import android.content.Context
//import android.text.Layout
//import android.widget.Toast
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.itemsIndexed
//import androidx.compose.material3.Button
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.unit.dp
//import com.example.remindme.presentation.Model.ReminderViewModel
//import com.example.remindme.presentation.NotificationSchedule
//import java.lang.reflect.Modifier
//
//@Composable
//fun ShowAlarmDetailsScreen() {
//    val context = LocalContext.current
//    val sharedPreferences = context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
//    val scheduledAlarms = remember {
//        sharedPreferences.getStringSet("scheduled_alarms", emptySet()) ?: emptySet()
//    }
//
//    Scaffold {
//        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
//            Text(text = "Scheduled Alarms", fontSize = 18.sp, fontWeight = FontWeight.Bold)
//            Spacer(modifier = Modifier.height(8.dp))
//
//            if (scheduledAlarms.isEmpty()) {
//                Text("No alarms scheduled.")
//            } else {
//                scheduledAlarms.forEach { alarm ->
//                    Text(text = alarm)
//                }
//            }
//        }
//    }
//}
