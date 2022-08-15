package com.onthewake.onthewakelive.util

import androidx.compose.material.ScaffoldState
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class SnackBarAppState(
    val scaffoldState: ScaffoldState,
    val snackBarScope: CoroutineScope
) {
    fun showSnackBar(message: String, duration: SnackbarDuration) {
        snackBarScope.launch {
            scaffoldState.snackbarHostState.showSnackbar(
                message = message, duration = duration
            )
        }
    }
}

@Composable
fun rememberSnackBarAppState(
    scaffoldState: ScaffoldState = rememberScaffoldState(
        snackbarHostState = remember { SnackbarHostState() }
    ),
    navController: NavHostController = rememberNavController(),
    snackBarScope: CoroutineScope = rememberCoroutineScope()
) = remember(scaffoldState, navController, snackBarScope) {
    SnackBarAppState(
        scaffoldState = scaffoldState,
        snackBarScope = snackBarScope
    )
}