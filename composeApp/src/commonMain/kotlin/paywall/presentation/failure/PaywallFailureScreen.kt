package paywall.presentation.failure

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import core.presentation.components.StandardButton
import core.presentation.ui.theme.StolzlFontFamily
import core.presentation.utils.OpenTelegramUtil
import core.utils.Constants.TELEGRAM_SUPPORT_ID
import onthewakelive.composeapp.generated.resources.Res
import onthewakelive.composeapp.generated.resources.ic_failure
import onthewakelive.composeapp.generated.resources.payment_contact_support
import onthewakelive.composeapp.generated.resources.payment_error
import onthewakelive.composeapp.generated.resources.payment_issue
import onthewakelive.composeapp.generated.resources.payment_report_issue
import onthewakelive.composeapp.generated.resources.payment_retry
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject

class PaywallFailureAssembly : Screen {

    @Composable
    override fun Content() {
        val openTelegramUtil: OpenTelegramUtil = koinInject()

        PaywallFailureScreen(
            onFeedbackClicked = { openTelegramUtil.open(telegramId = TELEGRAM_SUPPORT_ID) }
        )
    }
}

@OptIn(ExperimentalResourceApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun PaywallFailureScreen(
    onFeedbackClicked: () -> Unit
) {

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = stringResource(Res.string.payment_error),
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
                    text = stringResource(Res.string.payment_issue),
                    color = Color.White,
                    fontFamily = StolzlFontFamily(),
                    fontWeight = FontWeight.Medium,
                    fontSize = 24.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 32.sp
                )
                Text(
                    modifier = Modifier
                        .padding(top = 34.dp)
                        .padding(horizontal = 32.dp),
                    text = stringResource(Res.string.payment_contact_support),
                    color = Color.White,
                    fontFamily = StolzlFontFamily(),
                    fontWeight = FontWeight.Light,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp
                )
                StandardButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp),
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF2E7AD3),
                        contentColor = Color.White
                    ),
                    text = stringResource(Res.string.payment_retry),
                    fontFamily = StolzlFontFamily(),
                    fontWeight = FontWeight.Medium,
                    innerPaddingValues = PaddingValues(vertical = 8.dp),
                    fontSize = 16.sp
                )
                Text(
                    modifier = Modifier
                        .padding(top = 18.dp)
                        .align(Alignment.CenterHorizontally)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = onFeedbackClicked
                        ),
                    text = stringResource(Res.string.payment_report_issue),
                    textDecoration = TextDecoration.Underline,
                    fontFamily = StolzlFontFamily(),
                    fontWeight = FontWeight.Medium,
                    color = Color.White,
                    fontSize = 17.sp
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
                painter = painterResource(resource = Res.drawable.ic_failure),
                contentDescription = null
            )
        }
    }
}
