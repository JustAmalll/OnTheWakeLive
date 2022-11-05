package com.onthewake.onthewakelive.navigation

import com.onthewake.onthewakelive.util.Constants

sealed class Screen(val route: String) {
    object SplashScreen : Screen("splash_screen")
    object LoginScreen : Screen("login_screen")
    object RegisterScreen : Screen("register_screen")
    object QueueScreen : Screen("queue_screen")
    object ProfileScreen : Screen("profile_screen")
    object EditProfileScreen : Screen("edit_profile_screen")
    object QueueDetailsScreen : Screen(
        "queue_details_screen/{${Constants.DETAILS_ARGUMENT_KEY}}"
    ) {
        fun passItemId(itemId: String): String = "queue_details_screen/$itemId"
    }
}