package com.scw.twtour.model.datasource.local

import android.Manifest
import android.content.Context
import android.location.Location
import androidx.annotation.RequiresPermission
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import timber.log.Timber

interface LocationLocalDataSource {
    fun getLastLocation(): Flow<Location?>
}

class LocationLocalDataSourceImpl(context: Context) : LocationLocalDataSource {
    private val locationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    @RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    override fun getLastLocation() = callbackFlow<Location?> {
        locationClient.lastLocation
            .addOnSuccessListener { location ->
                trySend(location)
            }
            .addOnFailureListener {
                Timber.e(it)
            }
        awaitClose {}
    }
}