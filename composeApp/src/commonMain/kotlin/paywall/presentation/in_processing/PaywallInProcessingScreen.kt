package paywall.presentation.in_processing

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
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
import onthewakelive.composeapp.generated.resources.ic_clock
import onthewakelive.composeapp.generated.resources.wakeboard_illustration
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject
import paywall.presentation.failure.PaywallFailureAssembly
import paywall.presentation.in_processing.PaywallInProcessingViewModel.PaywallInProcessingAction.NavigateToPaywallFailureScreen
import paywall.presentation.in_processing.PaywallInProcessingViewModel.PaywallInProcessingAction.NavigateToPaywallSuccessScreen
import paywall.presentation.success.PaywallSuccessAssembly

class PaywallInProcessingAssembly : Screen {

    @Composable
    override fun Content() {
        val viewModel: PaywallInProcessingViewModel = koinInject()
        val navigator = LocalNavigator.current

        LaunchedEffect(key1 = true) {
            viewModel.actions.collect { action ->
                when (action) {
                    NavigateToPaywallSuccessScreen -> navigator?.replace(PaywallSuccessAssembly())
                    NavigateToPaywallFailureScreen -> navigator?.replace(PaywallFailureAssembly())
                }
            }
        }
        PaywallInProcessingScreen()
    }
}

@OptIn(ExperimentalResourceApi::class, ExperimentalMaterial3Api::class)
@Composable
fun PaywallInProcessingScreen() {

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Заявка успешно оформлена!",
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
                    text = "Ваша заявка находится в обработке. Пожалуйста, ожидайте!",
                    color = Color.White,
                    fontFamily = StolzlFontFamily(),
                    fontWeight = FontWeight.Medium,
                    fontSize = 24.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 32.sp
                )
                Text(
                    modifier = Modifier
                        .padding(top = 30.dp)
                        .padding(horizontal = 32.dp),
                    text = "Ваша подписка будет активирована в течение нескольких минут",
                    color = Color.White,
                    fontFamily = StolzlFontFamily(),
                    fontWeight = FontWeight.Light,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center
                )
                StandardButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp),
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFD39B2E),
                        contentColor = Color.White
                    ),
                    text = "В обработке",
                    fontFamily = StolzlFontFamily(),
                    fontWeight = FontWeight.Medium,
                    innerPaddingValues = PaddingValues(vertical = 8.dp),
                    fontSize = 16.sp,
                    icon = Res.drawable.ic_clock
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
                modifier = Modifier.scale(1.2f),
                painter = painterResource(resource = Res.drawable.wakeboard_illustration),
                contentDescription = null
            )
        }
    }
}