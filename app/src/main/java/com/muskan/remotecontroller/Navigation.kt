package com.muskan.remotecontroller

import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

@Composable
fun NavigationGraph(navController: NavHostController) {
    NavHost(navController, startDestination = "main_screen") {
        composable("main_screen") {
            MainScreen(navController)
        }
        composable("another_screen?text={text}", arguments = listOf(navArgument("text"){type = NavType.StringType})) {
            val text = it.arguments?.getString("text")
            if (text != null) {
                SecondScreen(navController, text = text)
            }
        }


    }
}
