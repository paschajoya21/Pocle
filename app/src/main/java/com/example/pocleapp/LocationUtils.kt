package com.example.pocleapp

import android.annotation.SuppressLint
import android.content.Context
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.tasks.await
import android.location.Location
import com.google.android.gms.maps.model.LatLng

object LocationUtils {
    @SuppressLint("MissingPermission")
    suspend fun getLastKnownLocation(context: Context): Location? {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        return try {
            fusedLocationClient.lastLocation.await()
        } catch (e: Exception) {
            null
        }
    }

    fun calculateTotalDistance(pathPoints: List<LatLng>): Float {
        var distance = 0f
        for (i in 0 until pathPoints.size - 1) {
            val result = FloatArray(1)
            Location.distanceBetween(
                pathPoints[i].latitude,
                pathPoints[i].longitude,
                pathPoints[i + 1].latitude,
                pathPoints[i + 1].longitude,
                result
            )
            distance += result[0]
        }
        return distance
    }
}

