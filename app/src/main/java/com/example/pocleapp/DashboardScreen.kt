package com.example.pocleapp.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.example.pocleapp.navigation.Screen


@Composable
fun DashboardScreen(navController: NavController) {
    val db = Firebase.firestore

    var totalDistance by remember { mutableStateOf(0f) }
    var totalDuration by remember { mutableStateOf(0L) }

    LaunchedEffect(true) {
        db.collection("tracks").get()
            .addOnSuccessListener { result ->
                var distance = 0f
                var duration = 0L

                for (doc in result) {
                    val d = doc.getDouble("distance")?.toFloat() ?: 0f
                    val t = doc.getLong("duration") ?: 0L
                    distance += d
                    duration += t
                }

                totalDistance = distance
                totalDuration = duration
            }
            .addOnFailureListener { e ->
                Log.e("Firebase", "Gagal mengambil data dashboard: $e")
            }
    }

    val hours = totalDuration / 3600000
    val minutes = (totalDuration % 3600000) / 60000

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("🚴 Total Jarak Tempuh", style = MaterialTheme.typography.titleMedium)
                Text("%.2f km".format(totalDistance / 1000), style = MaterialTheme.typography.headlineSmall)
            }
        }

        Card(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("⏱️ Total Waktu Tempuh", style = MaterialTheme.typography.titleMedium)
                Text("${hours} jam ${minutes} menit", style = MaterialTheme.typography.headlineSmall)
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(onClick = {
            navController.navigate(Screen.Tracking.route)
        }) {
            Text("Mulai Perjalanan")
        }
    }
}
