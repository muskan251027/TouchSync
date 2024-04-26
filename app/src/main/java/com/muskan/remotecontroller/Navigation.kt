package com.muskan.remotecontroller

import android.window.SplashScreen
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument

@Composable
fun NavigationGraph(navController: NavHostController) {
    NavHost(navController, startDestination = "splash_screen") {
        composable("splash_screen") {
            SplashScreen(navController)
        }
        composable("about_screen") {
            AboutUsScreen(navController)
        }
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
