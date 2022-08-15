package com.onthewake.onthewakelive.navigation

sealed class Screen(val route: String) {
    object SplashScreen : Screen("splash_screen")
    object LoginScreen : Screen("login_screen")
    object RegisterScreen : Screen("register_screen")
    object QueueScreen : Screen("home_screen")
}