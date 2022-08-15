package com.onthewake.onthewakelive.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.accompanist.pager.ExperimentalPagerApi
import com.onthewake.onthewakelive.feature_auth.presentation.LoginScreen
import com.onthewake.onthewakelive.feature_auth.presentation.RegisterScreen
import com.onthewake.onthewakelive.feature_queue.presentation.QueueScreen
import com.onthewake.onthewakelive.feature_splash.SplashScreen


@ExperimentalPagerApi
@Composable
fun SetupNavGraph(
    navController: NavHostController,
    showSnackBar: (String) -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = Screen.SplashScreen.route
    ) {
        composable(route = Screen.SplashScreen.route) {
            SplashScreen(navController = navController)
        }
        composable(route = Screen.LoginScreen.route) {
            LoginScreen(navController = navController, showSnackBar = showSnackBar)
        }
        composable(route = Screen.RegisterScreen.route) {
            RegisterScreen(navController = navController, showSnackBar = showSnackBar)
        }
        composable(route = Screen.QueueScreen.route) {
            QueueScreen()
        }
    }
}