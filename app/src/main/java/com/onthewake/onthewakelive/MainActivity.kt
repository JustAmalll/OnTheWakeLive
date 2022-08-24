package com.onthewake.onthewakelive

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarDuration
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.pager.ExperimentalPagerApi
import dagger.hilt.android.AndroidEntryPoint
import com.onthewake.onthewakelive.navigation.SetupNavGraph
import com.onthewake.onthewakelive.ui.theme.OnTheWakeLiveTheme
import com.onthewake.onthewakelive.util.SnackBarAppState
import com.onthewake.onthewakelive.util.rememberSnackBarAppState

@ExperimentalPagerApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var navController: NavHostController

    @SuppressLint("UnusedMaterialScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OnTheWakeLiveTheme {

                val appState: SnackBarAppState = rememberSnackBarAppState()
                navController = rememberNavController()

                Scaffold(
                    scaffoldState = appState.scaffoldState
                ) {
                    SetupNavGraph(
                        navController = navController,
                        showSnackBar = { message ->
                            appState.showSnackBar(
                                message = message,
                                duration = SnackbarDuration.Short
                            )
                        }
                    )
                }
            }
        }
    }
}