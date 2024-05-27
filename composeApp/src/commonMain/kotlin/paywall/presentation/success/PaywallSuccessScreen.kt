package paywall.presentation.success

import MainScreen
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import core.presentation.components.StandardButton
import core.presentation.ui.theme.StolzlFontFamily
import onthewakelive.composeapp.generated.resources.Res
import onthewakelive.composeapp.generated.resources.ic_success
import onthewakelive.composeapp.generated.resources.join_queue
import onthewakelive.composeapp.generated.resources.subscription_congrats
import onthewakelive.composeapp.generated.resources.subscription_success
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

class PaywallSuccessAssembly : Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.current
        PaywallSuccessScreen(onJoinToQueueClicked = { navigator?.replaceAll(MainScreen) })
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalResourceApi::class)
@Composable
private fun PaywallSuccessScreen(onJoinToQueueClicked: () -> Unit) {

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(Res.string.subscription_success),
                        fontFamily = StolzlFontFamily(),
                        fontWeight = FontWeight.Normal,
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1D1B20),
                    titleContentColor = Color.White
                )
            )
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .navigationBarsPadding()
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 40.dp)
            ) {
                Text(
                    text = stringResource(Res.string.subscription_congrats),
                    color = Color.White,
                    fontFamily = StolzlFontFamily(),
                    fontWeight = FontWeight.Medium,
                    fontSize = 24.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 32.sp
                )
                StandardButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 70.dp),
                    onClick = onJoinToQueueClicked,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF2E7AD3),
                        contentColor = Color.White
                    ),
                    text = stringResource(Res.string.join_queue)
                )
            }
        },
        containerColor = Color(0xFF1D1B20)
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(resource = Res.drawable.ic_success),
                contentDescription = null
            )
        }
    }
}