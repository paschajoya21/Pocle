package com.example.pocleapp

import com.google.android.gms.maps.model.LatLng

data class TrackingState(
    val isTracking: Boolean = false,
    val pathPoints: List<LatLng> = emptyList(),
    val startTime: Long = 0L,
    val endTime: Long = 0L
)

