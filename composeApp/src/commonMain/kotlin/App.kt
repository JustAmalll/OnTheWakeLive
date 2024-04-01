import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import auth.presentation.login.LoginAssembly
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator
import core.presentation.MainViewModel
import core.presentation.MainViewModel.MainAction.NavigateToLoginScreen
import core.presentation.MainViewModel.MainAction.NavigateToQueueScreen
import core.presentation.MainViewModel.MainAction.NavigateToServerUnavailableScreen
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject
import queue.presentation.list.QueueAssembly

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
                    NavigateToQueueScreen -> QueueAssembly
                    NavigateToServerUnavailableScreen -> LoginAssembly()
                }
            }
        }
        startScreen?.let { Navigator(it) }
    }
}