package com.onthewake.onthewakelive.core.presentation

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.getValue
import androidx.datastore.dataStore
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.ImageLoader
import com.google.accompanist.pager.ExperimentalPagerApi
import com.onthewake.onthewakelive.core.presentation.ui.theme.OnTheWakeLiveTheme
import com.onthewake.onthewakelive.navigation.Screen
import com.onthewake.onthewakelive.navigation.SetupNavGraph
import com.onthewake.onthewakelive.core.util.Constants
import com.onthewake.onthewakelive.core.util.Constants.ADMIN_IDS
import com.onthewake.onthewakelive.core.util.UserProfileSerializer
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

val Context.dataStore by dataStore(
    fileName = "user-profile.json",
    serializer = UserProfileSerializer
)

@ExperimentalAnimationApi
@ExperimentalMaterial3Api
@ExperimentalPagerApi
@ExperimentalMaterialApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var imageLoader: ImageLoader

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            OnTheWakeLiveTheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()

                val prefs = getSharedPreferences("prefs", MODE_PRIVATE)
                val isAdmin = prefs.getString(Constants.PREFS_USER_ID, null) in ADMIN_IDS

                StandardScaffold(
                    navController = navController,
                    showBottomBar = navBackStackEntry?.destination?.route in listOf(
                        Screen.QueueScreen.route, Screen.ProfileScreen.route
                    ) && !isAdmin
                ) {
                    SetupNavGraph(navController = navController, imageLoader = imageLoader)
                }
            }
        }
    }
}