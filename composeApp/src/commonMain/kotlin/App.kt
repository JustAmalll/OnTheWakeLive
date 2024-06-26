import admin_panel.AdminPanelAssembly
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import coil3.ImageLoader
import coil3.annotation.ExperimentalCoilApi
import coil3.compose.setSingletonImageLoaderFactory
import coil3.memory.MemoryCache
import coil3.request.CachePolicy
import coil3.util.DebugLogger
import core.presentation.MainEvent.OnMainScreenAppeared
import core.presentation.MainViewModel
import core.presentation.components.SplashLoadingScreen
import core.presentation.ui.theme.OnTheWakeLiveTheme
import core.presentation.utils.isPortraitOrientation
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject
import queue.presentation.list.QueueTab
import user_profile.presentation.profile.UserProfileTab

val LocalIsUserAdmin = compositionLocalOf { false }
val LocalUserId = compositionLocalOf<Int?> { null }

@OptIn(ExperimentalCoilApi::class)
@Composable
@Preview
fun App() {
    setSingletonImageLoaderFactory { context ->
        ImageLoader.Builder(context)
            .diskCachePolicy(policy = CachePolicy.DISABLED)
            .memoryCachePolicy(policy = CachePolicy.ENABLED)
            .memoryCache {
                MemoryCache.Builder()
                    .maxSizePercent(context, percent = 0.25)
                    .weakReferencesEnabled(true)
                    .build()
            }
            .logger(DebugLogger())
            .build()
    }

    OnTheWakeLiveTheme {
        val viewModel: MainViewModel = koinInject()
        val state by viewModel.state.collectAsState()

        CompositionLocalProvider(
            LocalIsUserAdmin provides state.isUserAdmin,
            LocalUserId provides state.userId
        ) {
            Surface {
                state.startScreen?.let { Navigator(it) }

                if (state.startScreen == null) {
                    SplashLoadingScreen()
                }
            }
        }
    }
}

object MainScreen : Screen {

    @Composable
    override fun Content() {
        val viewModel: MainViewModel = koinInject()
        val isPortraitOrientation = isPortraitOrientation()
        val isUserAdmin = LocalIsUserAdmin.current

        LaunchedEffect(key1 = true) {
            viewModel.onEvent(OnMainScreenAppeared)
        }

        TabNavigator(tab = QueueTab) {
            Scaffold(
                content = {
                    Box(
                        modifier = Modifier.padding(
                            bottom = if (isPortraitOrientation) 80.dp else 0.dp
                        )
                    ) {
                        CurrentTab()
                    }
                },
                bottomBar = {
                    if (isPortraitOrientation) {
                        NavigationBar {
                            TabNavigationItem(tab = QueueTab)

                            if (isUserAdmin) {
                                TabNavigationItem(tab = AdminPanelAssembly)
                            } else {
                                TabNavigationItem(tab = UserProfileTab)
                            }
                        }
                    }
                }
            )
        }
    }
}

@Composable
private fun RowScope.TabNavigationItem(tab: Tab) {
    val tabNavigator = LocalTabNavigator.current

    NavigationBarItem(
        selected = tabNavigator.current == tab,
        onClick = { tabNavigator.current = tab },
        label = { Text(text = tab.options.title) },
        icon = {
            tab.options.icon?.let {
                Icon(painter = it, contentDescription = null)
            }
        }
    )
}