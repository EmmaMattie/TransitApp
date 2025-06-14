package com.example.transitapp.nav

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.Map
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.navigation.compose.*
import com.example.transitapp.ui.screens.MapScreen
import com.example.transitapp.ui.screens.OtherScreen
import com.example.transitapp.viewmodel.MainViewModel

@Composable
fun NavGraph(viewModel: MainViewModel) {
    val navController = rememberNavController()

    val items = listOf(
        NavItem("Map", Icons.Filled.Map),
        NavItem("Stops", Icons.Filled.DirectionsBus)
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
                items.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = screen.title) },
                        label = { Text(screen.title) },
                        selected = currentRoute == screen.title.lowercase(),
                        onClick = {
                            navController.navigate(screen.title.lowercase()) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "map",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("map") { MapScreen(viewModel) }
            composable("stops") { OtherScreen() }
        }
    }
}

data class NavItem(val title: String, val icon: androidx.compose.ui.graphics.vector.ImageVector)
