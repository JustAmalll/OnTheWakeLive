package com.onthewake.onthewakelive.feature_splash.presentation

import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.onthewake.onthewakelive.R
import com.onthewake.onthewakelive.feature_auth.domain.models.AuthResult
import com.onthewake.onthewakelive.navigation.Screen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun SplashScreen(
    navController: NavHostController,
    viewModel: SplashViewModel = hiltViewModel()
) {
    val scale = remember { Animatable(0f) }
    val overshootInterpolator = remember { OvershootInterpolator(2f) }

    val systemUiController = rememberSystemUiController()
    val darkTheme = isSystemInDarkTheme()
    val backgroundColor = MaterialTheme.colorScheme.background

    SideEffect {
        systemUiController.setSystemBarsColor(
            color = backgroundColor, darkIcons = !darkTheme
        )
    }

    LaunchedEffect(key1 = true) {
        withContext(Dispatchers.Main) {
            scale.animateTo(
                targetValue = 0.5f,
                animationSpec = tween(
                    durationMillis = 500,
                    easing = { overshootInterpolator.getInterpolation(it) }
                )
            )
        }
    }
    LaunchedEffect(key1 = true) {
        viewModel.authResults.collect { result ->
            when (result) {
                is AuthResult.Authorized -> {
                    navController.navigate(Screen.QueueScreen.route) {
                        popUpTo(Screen.SplashScreen.route) { inclusive = true }
                    }
                }
                is AuthResult.Unauthorized -> {
                    navController.navigate(Screen.LoginScreen.route) {
                        popUpTo(Screen.SplashScreen.route) { inclusive = true }
                    }
                }
                else -> {}
            }
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center,
    ) {
        Image(
            modifier = Modifier
                .size(600.dp)
                .scale(scale.value),
            painter = painterResource(
                id = if (darkTheme) R.drawable.logo_white else R.drawable.logo_black
            ),
            contentDescription = stringResource(id = R.string.logo)
        )
    }
}