package com.onthewake.onthewakelive.navigation

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import coil.ImageLoader
import com.google.accompanist.pager.ExperimentalPagerApi
import com.onthewake.onthewakelive.feature_auth.presentation.LoginScreen
import com.onthewake.onthewakelive.feature_auth.presentation.RegisterScreen
import com.onthewake.onthewakelive.feature_auth.presentation.auth_otp.OtpScreen
import com.onthewake.onthewakelive.feature_profile.presentation.edit_profile.EditProfileScreen
import com.onthewake.onthewakelive.feature_profile.presentation.profile.ProfileScreen
import com.onthewake.onthewakelive.feature_queue.presentation.queue_details.QueueDetailsScreen
import com.onthewake.onthewakelive.feature_queue.presentation.queue_list.QueueScreen
import com.onthewake.onthewakelive.feature_splash.SplashScreen
import com.onthewake.onthewakelive.feature_trick_list.presentation.TrickListScreen
import com.onthewake.onthewakelive.util.Constants
import com.onthewake.onthewakelive.util.Constants.DETAILS_ARGUMENT_KEY

@ExperimentalMaterialApi
@ExperimentalMaterial3Api
@ExperimentalPagerApi
@Composable
fun SetupNavGraph(
    navController: NavHostController,
    imageLoader: ImageLoader
) {
    NavHost(
        navController = navController,
        startDestination = Screen.TrickListScreen.route
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
        composable(
            route = Screen.OtpScreen.route,
            arguments = listOf(navArgument(Constants.REGISTER_DATA_ARGUMENT_KEY) {
                type = NavType.StringType
            })
        ) {
            OtpScreen(navController = navController)
        }
        composable(route = Screen.QueueScreen.route) {
            QueueScreen(navController = navController, imageLoader = imageLoader)
        }
        composable(
            route = Screen.QueueDetailsScreen.route,
            arguments = listOf(navArgument(DETAILS_ARGUMENT_KEY) {
                type = NavType.StringType
            })
        ) {
            QueueDetailsScreen(imageLoader = imageLoader, navController = navController)
        }
        composable(route = Screen.ProfileScreen.route) {
            ProfileScreen(navController = navController, imageLoader = imageLoader)
        }
        composable(route = Screen.EditProfileScreen.route) {
            EditProfileScreen(imageLoader = imageLoader, navController = navController)
        }
        composable(route = Screen.TrickListScreen.route) {
            TrickListScreen(navController = navController)
        }
    }
}
