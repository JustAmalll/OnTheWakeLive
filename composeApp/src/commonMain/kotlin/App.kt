import admin_panel.AdminPanelAssembly
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBarDefaults.windowInsets
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import core.presentation.ui.theme.gradientBackground
import core.presentation.utils.clickableWithoutIndication
import core.presentation.utils.isPortraitOrientation
import dev.chrisbanes.haze.HazeDefaults
import dev.chrisbanes.haze.HazeState
import dev.chrisbanes.haze.haze
import dev.chrisbanes.haze.hazeChild
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject
import queue.presentation.list.QueueTab
import statistics.StatisticsAssembly
import user_profile.presentation.UserProfileTab

val LocalIsUserAdmin = compositionLocalOf { false }
val LocalUserId = compositionLocalOf<Int?> { null }
val LocalToggleBackgroundBlur = compositionLocalOf<() -> Unit> {
    error("LocalToggleBackgroundBlur LocalHazeState not present")
}

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
        val hazeState = remember { HazeState() }
        var showBackgroundBlur by remember { mutableStateOf(false) }

        if (showBackgroundBlur) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .hazeChild(state = hazeState)
            )
        }

        CompositionLocalProvider(
            LocalIsUserAdmin provides state.isUserAdmin,
            LocalUserId provides state.userId,
            LocalToggleBackgroundBlur provides { showBackgroundBlur = !showBackgroundBlur }
        ) {
            Surface(
                modifier = Modifier.haze(
                    state = hazeState,
                    style = HazeDefaults.style(blurRadius = 3.dp, noiseFactor = 0f)
                )
            ) {
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
                        Box(
                            modifier = Modifier
                                .windowInsetsPadding(windowInsets)
                                .clip(shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp))
                                .background(color = MaterialTheme.colorScheme.background),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(all = 16.dp)
                                    .gradientBackground(radius = 42.dp)
                                    .padding(vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                TabNavigationItem(tab = QueueTab)
                                TabNavigationItem(tab = StatisticsAssembly)

                                if (isUserAdmin) {
                                    TabNavigationItem(tab = AdminPanelAssembly)
                                } else {
                                    TabNavigationItem(tab = UserProfileTab)
                                }
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

    Column(
        modifier = Modifier
            .weight(1f)
            .clickableWithoutIndication { tabNavigator.current = tab },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        tab.options.icon?.let {
            Icon(
                modifier = Modifier.size(26.dp),
                painter = it,
                contentDescription = null,
                tint = if (tabNavigator.current == tab) {
                    MaterialTheme.colorScheme.primary
                } else {
                    Color.White
                }
            )
        }
        Text(
            text = tab.options.title,
            fontSize = 11.sp,
            lineHeight = 6.sp,
            fontWeight = FontWeight.Normal,
            color = if (tabNavigator.current == tab) {
                MaterialTheme.colorScheme.primary
            } else {
                Color.White
            }
        )
    }
}