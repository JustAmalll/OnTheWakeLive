import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import auth.presentation.login.LoginAssembly
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import com.benasher44.uuid.Uuid
import core.presentation.MainEvent.OnMainScreenAppeared
import core.presentation.MainViewModel
import core.presentation.MainViewModel.MainAction.NavigateToLoginScreen
import core.presentation.MainViewModel.MainAction.NavigateToQueueScreen
import core.presentation.MainViewModel.MainAction.NavigateToServerUnavailableScreen
import core.presentation.ui.theme.OnTheWakeLiveTheme
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject
import queue.presentation.list.QueueTab
import server_unavailable.ServerUnavailableAssembly
import user_profile.presentation.profile.UserProfileTab

val LocalSnackBarHostState = compositionLocalOf<(String) -> Unit> {
    error("No SnackBarHostState provided")
}

val LocalIsUserAdmin = compositionLocalOf { false }
val LocalUserId = compositionLocalOf<Uuid?> { null }

@Composable
@Preview
fun App() {
    OnTheWakeLiveTheme {
        val viewModel: MainViewModel = koinInject()
        val state by viewModel.state.collectAsState()

        var startScreen: Screen? by remember { mutableStateOf(null) }

        LaunchedEffect(key1 = Unit) {
            viewModel.actions.collect { action ->
                startScreen = when (action) {
                    NavigateToLoginScreen -> LoginAssembly()
                    NavigateToQueueScreen -> MainScreen
                    NavigateToServerUnavailableScreen -> ServerUnavailableAssembly()
                }
            }
        }

        CompositionLocalProvider(
            LocalIsUserAdmin provides state.isUserAdmin,
            LocalUserId provides state.userId
        ) {
            startScreen?.let { Navigator(it) }
        }
    }
}

object MainScreen : Screen {

    @Composable
    override fun Content() {
        val viewModel: MainViewModel = koinInject()

        val scope = rememberCoroutineScope()
        val snackBarHostState = remember { SnackbarHostState() }

        LaunchedEffect(key1 = true) {
            viewModel.onEvent(OnMainScreenAppeared)
        }

        CompositionLocalProvider(
            LocalSnackBarHostState provides {
                scope.launch { snackBarHostState.showSnackbar(message = it) }
            }
        ) {
            TabNavigator(tab = QueueTab) {
                Scaffold(
                    content = { CurrentTab() },
                    bottomBar = {
                        NavigationBar {
                            TabNavigationItem(tab = QueueTab)
                            TabNavigationItem(tab = UserProfileTab)
                        }
                    }
                )
            }
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