package com.muskan.remotecontroller

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavBackStackEntry
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.systemuicontroller.rememberSystemUiController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp()
        }
    }
}

@Composable
fun MyApp() {
    // Create a navigation controller
    val navController = rememberNavController()

    // Obtain an instance of SystemUiController
    val systemUiController = rememberSystemUiController()
    LaunchedEffect(key1 = true) {
        // Set the status bar color
        systemUiController.setStatusBarColor(
            color = Color(0xFF100c08)
        )
    }

    // Define the navigation graph
    NavigationGraph(navController)
}

