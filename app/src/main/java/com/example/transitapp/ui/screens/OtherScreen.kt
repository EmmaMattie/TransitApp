package com.example.transitapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class StopInfo(val name: String, val routes: List<String>, val times: List<String>)

@Composable
fun OtherScreen() {
    val mockStops = listOf(
        StopInfo("Chain Lake Terminal", listOf("21", "52", "53"), listOf("10:05 AM", "10:20 AM", "10:45 AM")),
        StopInfo("Lacewood Terminal", listOf("2", "4", "24"), listOf("10:12 AM", "10:25 AM", "10:40 AM")),
        StopInfo("Bayers Lake Entrance", listOf("22", "28"), listOf("10:15 AM", "10:35 AM")),
        StopInfo("Walmart Bayers Lake", listOf("21", "123"), listOf("10:18 AM", "10:50 AM")),
    )

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Nearby Stops", style = MaterialTheme.typography.headlineSmall)

        LazyColumn {
            items(mockStops) { stop ->
                StopCard(stop)
            }
        }
    }
}

@Composable
fun StopCard(stop: StopInfo) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(stop.name, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text("Routes: ${stop.routes.joinToString(", ")}")
            Text("Next buses: ${stop.times.joinToString(", ")}")
        }
    }
}
