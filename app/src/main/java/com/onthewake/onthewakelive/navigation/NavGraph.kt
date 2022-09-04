package com.onthewake.onthewakelive.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import coil.ImageLoader
import com.google.accompanist.pager.ExperimentalPagerApi
import com.onthewake.onthewakelive.feature_auth.presentation.LoginScreen
import com.onthewake.onthewakelive.feature_auth.presentation.RegisterScreen
import com.onthewake.onthewakelive.feature_profile.presentation.edit_profile.EditProfileScreen
import com.onthewake.onthewakelive.feature_profile.presentation.profile.ProfileScreen
import com.onthewake.onthewakelive.feature_queue.presentation.QueueScreen
import com.onthewake.onthewakelive.feature_splash.SplashScreen

@ExperimentalMaterial3Api
@ExperimentalPagerApi
@Composable
fun SetupNavGraph(
    navController: NavHostController,
    imageLoader: ImageLoader
) {
    NavHost(
        navController = navController,
        startDestination = Screen.SplashScreen.route
    ) {
        composable(route = Screen.SplashScreen.route) {
            SplashScreen(navController = navController)
        }
        composable(route = Screen.LoginScreen.route) {
            LoginScreen(navController = navController)
        }
        composable(route = Screen.RegisterScreen.route) {
            RegisterScreen(navController = navController)
        }
        composable(route = Screen.QueueScreen.route) {
            QueueScreen()
        }
        composable(route = Screen.ProfileScreen.route) {
            ProfileScreen()
        }
        composable(route = Screen.EditProfileScreen.route) {
            EditProfileScreen(imageLoader = imageLoader)
        }
    }
}
