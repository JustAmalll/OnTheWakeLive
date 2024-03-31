import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import di.appModule
import di.queueModule
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.KoinApplication
import queue.presentation.list.QueueAssembly

@Composable
@Preview
fun App() {
    KoinApplication(application = { modules(queueModule + appModule) }) {
        MaterialTheme {
            Navigator(QueueAssembly())
        }
    }
}