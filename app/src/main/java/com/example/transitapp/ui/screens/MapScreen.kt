package com.example.transitapp.ui.screens

import android.Manifest
import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.example.transitapp.viewmodel.MainViewModel
import com.google.accompanist.permissions.*
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapInitOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.Style
import com.mapbox.maps.plugin.annotation.AnnotationConfig
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.*
import com.mapbox.maps.plugin.locationcomponent.location
import androidx.core.graphics.drawable.toBitmap
import com.example.transitapp.location.GetLocation
import kotlinx.coroutines.delay

@SuppressLint("MissingPermission")
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MapScreen(viewModel: MainViewModel) {
    GetLocation()

    val permissionsState = rememberMultiplePermissionsState(
        listOf(Manifest.permission.ACCESS_FINE_LOCATION)
    )

    val context = LocalContext.current
    val gtfsFeed by viewModel.gtfs.collectAsState()

    // Auto-refresh every 15s
    LaunchedEffect("auto-refresh") {
        while (true) {
            viewModel.loadBusPositions()
            delay(15_000)
        }
    }

    AndroidView(
        factory = { ctx ->
            val mapView = MapView(ctx, MapInitOptions(ctx))

            mapView.mapboxMap.loadStyleUri(Style.MAPBOX_STREETS) { style ->
                val fallbackLocation = Point.fromLngLat(-63.6697, 44.6459)

                if (permissionsState.allPermissionsGranted) {
                    mapView.location.updateSettings {
                        enabled = true
                        pulsingEnabled = true
                    }

                    mapView.mapboxMap.setCamera(
                        CameraOptions.Builder()
                            .center(fallbackLocation)
                            .zoom(15.5)
                            .build()
                    )
                } else {
                    mapView.mapboxMap.setCamera(
                        CameraOptions.Builder()
                            .center(fallbackLocation)
                            .zoom(14.0)
                            .build()
                    )
                }

                val annotationPlugin = mapView.annotations
                val annotationConfig = AnnotationConfig("bus-layer")
                val pointAnnotationManager = annotationPlugin.createPointAnnotationManager(annotationConfig)

                val iconRes = context.resources.getIdentifier("bus", "drawable", context.packageName)
                val bitmap = context.getDrawable(iconRes)!!.toBitmap()
                val imageId = "bus-icon"
                style.addImage(imageId, bitmap)

                pointAnnotationManager.deleteAll()

                val buses = gtfsFeed?.entityList.orEmpty()
                Log.d("GTFS", "Loaded ${buses.size} buses")

                buses.forEach { entity ->
                    val vehicle = entity.vehicle
                    if (vehicle.hasPosition()) {
                        val lat = vehicle.position.latitude
                        val lon = vehicle.position.longitude
                        val route = vehicle.trip.routeId ?: "?"

                        val point = Point.fromLngLat(lon.toDouble(), lat.toDouble())

                        val options = PointAnnotationOptions()
                            .withPoint(point)
                            .withIconImage(imageId)
                            .withTextField(route)
                            .withTextSize(14.0)
                            .withTextOffset(listOf(0.0, -0.35))
                            .withTextColor("#003366")
                            .withTextHaloColor("#FFFFFF")
                            .withTextHaloWidth(2.5)



                        pointAnnotationManager.create(options)
                    }
                }
            }

            mapView
        },
        modifier = Modifier
    )
}
