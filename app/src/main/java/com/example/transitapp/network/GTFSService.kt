package com.example.transitapp.network

import com.example.transitapp.model.BusPosition
import retrofit2.http.GET
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface GTFSService {
    @GET("gtfs/Vehicle/VehiclePositions.json")
    suspend fun getBusPositions(): List<BusPosition>
}

object GTFSClient {
    val api: GTFSService by lazy {
        Retrofit.Builder()
            .baseUrl("https://hrmbusapi.halifax.ca/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GTFSService::class.java)
    }
}
