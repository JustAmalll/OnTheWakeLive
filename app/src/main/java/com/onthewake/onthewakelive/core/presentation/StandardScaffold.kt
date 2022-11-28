package com.onthewake.onthewakelive.core.presentation

import android.annotation.SuppressLint
import android.content.SharedPreferences
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.navigation.NavHostController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.onthewake.onthewakelive.core.domain.modules.BottomNavItem
import com.onthewake.onthewakelive.navigation.Screen
import com.onthewake.onthewakelive.util.Constants
import com.onthewake.onthewakelive.util.Constants.ADMIN_IDS

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@ExperimentalMaterial3Api
@ExperimentalPagerApi
@Composable
fun StandardScaffold(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    showBottomBar: Boolean = true,
    bottomNavItems: List<BottomNavItem> = listOf(
        BottomNavItem(
            route = Screen.QueueScreen.route,
            icon = Icons.Default.Home,
            contentDescription = "Queue"
        ),
        BottomNavItem(
            route = Screen.ProfileScreen.route,
            icon = Icons.Default.Person,
            contentDescription = "Profile"
        )
    ),
    prefs: SharedPreferences,
    topBar: @Composable () -> Unit = {},
    content: @Composable () -> Unit
) {

    val userId = prefs.getString(Constants.PREFS_USER_ID, null)
    val haptic = LocalHapticFeedback.current

    Scaffold(
        topBar = { topBar() },
        bottomBar = {
            if (showBottomBar) {
                if (userId !in ADMIN_IDS) NavigationBar {
                    bottomNavItems.forEachIndexed { _, item ->
                        NavigationBarItem(
                            icon = {
                                item.icon?.let {
                                    Icon(
                                        imageVector = it,
                                        contentDescription = item.contentDescription
                                    )
                                }
                            },
                            label = { item.contentDescription?.let { Text(text = it) } },
                            selected = navController.currentDestination?.route?.startsWith(
                                item.route
                            ) == true,
                            onClick = {
                                haptic.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                if (navController.currentDestination?.route != item.route) {
                                    navController.navigate(item.route) { launchSingleTop = true }
                                }
                            }
                        )
                    }
                }
            }
        },
        modifier = modifier
    ) {
        content()
    }
}