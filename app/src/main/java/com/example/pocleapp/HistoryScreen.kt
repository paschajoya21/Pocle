package com.example.pocleapp

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.Timestamp
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.tasks.await

data class HistoryItem(
    val distance: Float,
    val duration: Long,
    val timestamp: Timestamp
)

@Composable
fun HistoryScreen() {
    var historyList by remember { mutableStateOf<List<HistoryItem>>(emptyList()) }

    LaunchedEffect(Unit) {
        val db = Firebase.firestore
        val snapshot = db.collection("tracks").orderBy("timestamp").get().await()

        historyList = snapshot.documents.mapNotNull { doc ->
            val distance = doc.getDouble("distance")?.toFloat() ?: return@mapNotNull null
            val duration = doc.getLong("duration") ?: return@mapNotNull null
            val timestamp = doc.getTimestamp("timestamp") ?: return@mapNotNull null
            HistoryItem(distance, duration, timestamp)
        }
    }

    LazyColumn(modifier = Modifier.padding(16.dp)) {
        items(historyList) { item ->
            Card(modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Jarak: %.2f meter".format(item.distance))
                    Text("Durasi: %.2f menit".format(item.duration / 60000f))
                    Text("Waktu: ${item.timestamp.toDate()}")
                }
            }
        }
    }
}
