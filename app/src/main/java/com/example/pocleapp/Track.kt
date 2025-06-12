package com.example.pocleapp.model

import com.google.android.gms.maps.model.LatLng
import com.google.firebase.Timestamp

data class Track(
    val distance: Float = 0f,
    val duration: Long = 0L,
    val timestamp: Timestamp? = null,
    val points: List<LatLng> = emptyList()
)
