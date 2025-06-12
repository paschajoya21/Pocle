package com.example.pocleapp

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.pocleapp.LocationUtils.calculateTotalDistance
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.maps.android.compose.*
import kotlinx.coroutines.delay

@Composable
fun MapScreen(context: Context) {
    var trackingState by remember { mutableStateOf(TrackingState()) }
    var totalDistance by remember { mutableStateOf(0f) }
    var trackingDuration by remember { mutableStateOf(0L) }
    val cameraPositionState = rememberCameraPositionState()

    // Timer efek untuk durasi
    LaunchedEffect(trackingState.isTracking) {
        if (trackingState.isTracking) {
            trackingState = trackingState.copy(startTime = System.currentTimeMillis())
            while (trackingState.isTracking) {
                delay(1000L)
                trackingDuration = System.currentTimeMillis() - trackingState.startTime
            }
        }
    }

    // Lokasi real-time
    LaunchedEffect(trackingState.isTracking) {
        if (trackingState.isTracking) {
            while (trackingState.isTracking) {
                val location = LocationUtils.getLastKnownLocation(context)
                location?.let {
                    val newPoint = LatLng(it.latitude, it.longitude)
                    val updatedPoints = trackingState.pathPoints + newPoint
                    totalDistance = calculateTotalDistance(updatedPoints)
                    trackingState = trackingState.copy(pathPoints = updatedPoints)
                    cameraPositionState.move(CameraUpdateFactory.newLatLng(newPoint))
                }
                delay(2000L)
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.weight(1f),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(isMyLocationEnabled = true)
        ) {
            if (trackingState.pathPoints.isNotEmpty()) {
                Polyline(points = trackingState.pathPoints)
            }
            trackingState.pathPoints.lastOrNull()?.let {
                Marker(state = MarkerState(position = it), title = "Posisi Saat Ini")
            }
        }

        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Jarak: %.2f meter".format(totalDistance))
            Text(text = "Durasi: ${trackingDuration / 1000} detik")
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = {
                    trackingState = TrackingState(isTracking = true, startTime = System.currentTimeMillis())
                    totalDistance = 0f
                    trackingDuration = 0L
                },
                enabled = !trackingState.isTracking
            ) {
                Text("Start")
            }

            Button(
                onClick = {
                    val finalEndTime = System.currentTimeMillis()
                    trackingState = trackingState.copy(isTracking = false, endTime = finalEndTime)
                    saveTrackingToFirebase(trackingState, totalDistance)
                },
                enabled = trackingState.isTracking
            ) {
                Text("Stop")
            }
        }
    }
}

fun saveTrackingToFirebase(trackingState: TrackingState, distance: Float) {
    val db = Firebase.firestore
    val duration = trackingState.endTime - trackingState.startTime

    val tripData = hashMapOf(
        "distance" to distance,
        "duration" to duration,
        "timestamp" to FieldValue.serverTimestamp(),
        "points" to trackingState.pathPoints.map {
            hashMapOf("lat" to it.latitude, "lng" to it.longitude)
        }
    )

    db.collection("tracks")
        .add(tripData)
        .addOnSuccessListener { Log.d("Firebase", "Berhasil disimpan!") }
        .addOnFailureListener { e -> Log.e("Firebase", "Gagal menyimpan: $e") }
}
