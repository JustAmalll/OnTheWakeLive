import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import auth.presentation.login.LoginAssembly
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.CurrentTab
import cafe.adriel.voyager.navigator.tab.Tab
import cafe.adriel.voyager.navigator.tab.TabNavigator
import core.presentation.MainViewModel
import core.presentation.MainViewModel.MainAction.NavigateToLoginScreen
import core.presentation.MainViewModel.MainAction.NavigateToQueueScreen
import core.presentation.MainViewModel.MainAction.NavigateToServerUnavailableScreen
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject
import queue.presentation.list.QueueTab
import server_unavailable.ServerUnavailableAssembly
import user_profile.presentation.UserProfileAssembly

@Composable
@Preview
fun App() {
    MaterialTheme {
        val viewModel: MainViewModel = koinInject()
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
        startScreen?.let { Navigator(it) }
    }
}

object MainScreen : Screen {

    @Composable
    override fun Content() {
        TabNavigator(tab = QueueTab) { tabNavigator ->
            Scaffold(
                bottomBar = {
                    NavigationBar {
                        TabNavigationItem(tab = QueueTab, tabNavigator = tabNavigator)
                        TabNavigationItem(tab = UserProfileAssembly, tabNavigator = tabNavigator)
                    }
                }
            ) {
                CurrentTab()
            }
        }
    }
}

@Composable
private fun RowScope.TabNavigationItem(tab: Tab, tabNavigator: TabNavigator) {
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