package com.example.transitapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.transit.realtime.GtfsRealtime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.net.URL
import android.util.Log
import kotlinx.coroutines.withContext

class MainViewModel : ViewModel() {
    private val _gtfs = MutableStateFlow<GtfsRealtime.FeedMessage?>(null)
    val gtfs = _gtfs.asStateFlow()

    private val _alerts = MutableStateFlow<GtfsRealtime.FeedMessage?>(null)
    val alerts = _alerts.asStateFlow()

    fun loadBusPositions() {
        viewModelScope.launch {
            try {
                val url = URL("http://gtfs.halifax.ca/realtime/Vehicle/VehiclePositions.pb")
                val feed = withContext(Dispatchers.IO) {
                    GtfsRealtime.FeedMessage.parseFrom(url.openStream())
                }
                Log.d("GTFS", "Success: ${feed.entityCount} entities")
                _gtfs.value = feed
            } catch (e: Exception) {
                Log.e("GTFS", "Error loading GTFS data", e)
            }
        }
    }
}
