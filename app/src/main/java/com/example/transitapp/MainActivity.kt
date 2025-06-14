package com.example.transitapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.transitapp.nav.NavGraph
import com.example.transitapp.ui.theme.TransitAppTheme
import com.example.transitapp.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TransitAppTheme {
                val viewModel: MainViewModel = viewModel()
                NavGraph(viewModel = viewModel)
            }
        }
    }
}
