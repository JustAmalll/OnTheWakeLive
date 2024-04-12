package server_unavailable

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import onthewakelive.composeapp.generated.resources.Res
import onthewakelive.composeapp.generated.resources.server_unavailable
import onthewakelive.composeapp.generated.resources.server_unavailable_subtitle
import onthewakelive.composeapp.generated.resources.server_unavailable_title
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

class ServerUnavailableAssembly : Screen {

    @Composable
    override fun Content() {
        ServerUnavailableScreen()
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
private fun ServerUnavailableScreen() {
    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(resource = Res.drawable.server_unavailable),
                contentDescription = null
            )
            Text(
                text = stringResource(resource = Res.string.server_unavailable_title),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.error,
                textAlign = TextAlign.Center
            )
            Text(
                modifier = Modifier.padding(top = 12.dp),
                text = stringResource(resource = Res.string.server_unavailable_subtitle),
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.error.copy(alpha = 0.9f),
                textAlign = TextAlign.Center
            )
        }
    }
}