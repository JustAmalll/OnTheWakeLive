package com.onthewake.onthewakelive

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarDuration
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.onthewake.onthewakelive.core.presentation.StandardScaffold
import com.onthewake.onthewakelive.navigation.Screen
import com.onthewake.onthewakelive.navigation.SetupNavGraph
import com.onthewake.onthewakelive.ui.theme.OnTheWakeLiveTheme
import com.onthewake.onthewakelive.util.SnackBarAppState
import com.onthewake.onthewakelive.util.rememberSnackBarAppState
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@ExperimentalMaterial3Api
@ExperimentalPagerApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var imageLoader: coil.ImageLoader

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OnTheWakeLiveTheme {

                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()

                StandardScaffold(
                    modifier = Modifier.fillMaxSize(),
                    navController = navController,
                    showBottomBar = navBackStackEntry?.destination?.route in listOf(
                        Screen.QueueScreen.route,
                        Screen.ProfileScreen.route,
                    )
                ) {
                    SetupNavGraph(
                        navController = navController,
                        imageLoader = imageLoader
                    )
                }
            }
        }
    }
}