package com.example.transitapp.model

data class BusPosition(
    val VehicleNo: String,
    val Route: String,
    val Latitude: Double,
    val Longitude: Double,
    val Destination: String
)
