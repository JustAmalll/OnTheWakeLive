package com.onthewake.onthewakelive.util

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class SnackBarAppState(
    val hostState: SnackbarHostState,
    val snackBarScope: CoroutineScope
) {
    fun showSnackBar(message: String, duration: SnackbarDuration) {
        snackBarScope.launch {
            hostState.showSnackbar(
                message = message, duration = duration
            )
        }
    }
}

@Composable
fun rememberSnackBarAppState(
    hostState: SnackbarHostState = remember { SnackbarHostState() },
    navController: NavHostController = rememberNavController(),
    snackBarScope: CoroutineScope = rememberCoroutineScope()
) = remember(hostState, navController, snackBarScope) {
    SnackBarAppState(
        hostState = hostState,
        snackBarScope = snackBarScope
    )
}