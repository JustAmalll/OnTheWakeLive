package com.onthewake.onthewakelive.navigation

import com.onthewake.onthewakelive.core.util.Constants
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

sealed class Screen(val route: String) {
    object SplashScreen : Screen("splash_screen")
    object LoginScreen : Screen("login_screen")
    object RegisterScreen : Screen("register_screen")
    object OtpScreen : Screen("otp_screen/{${Constants.REGISTER_DATA_ARGUMENT_KEY}}") {
        fun passRegisterData(registerData: String): String = "otp_screen/$registerData"
    }

    object QueueScreen : Screen("queue_screen")
    object ProfileScreen : Screen("profile_screen")
    object EditProfileScreen : Screen("edit_profile_screen")
    object TrickListScreen : Screen("trick_list_screen/{${Constants.USER_ID_ARGUMENT_KEY}}") {
        fun passUserId(userId: String): String = "trick_list_screen/$userId"
    }

    object AddTricksScreen : Screen("add_tricks_screen")
    object QueueDetailsScreen : Screen(
        "queue_details_screen/{${Constants.DETAILS_ARGUMENT_KEY}}"
    ) {
        fun passItemId(itemId: String): String = "queue_details_screen/$itemId"
    }

    object FullSizeAvatarScreen : Screen(
        "full_size_avatar_screen/{${Constants.PICTURE_URL_ARGUMENT_KEY}}"
    ) {
        fun passPictureUrl(url: String): String {
            val encodedUrl = URLEncoder.encode(url, StandardCharsets.UTF_8.toString())
            return "full_size_avatar_screen/$encodedUrl"
        }
    }
}