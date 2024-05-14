package paywall.presentation.form

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import core.presentation.components.StandardButton
import core.presentation.ui.theme.StolzlFontFamily
import core.presentation.utils.rememberImagePickerLauncher
import onthewakelive.composeapp.generated.resources.Res
import onthewakelive.composeapp.generated.resources.one_time_seasonal_subscription
import onthewakelive.composeapp.generated.resources.ready
import onthewakelive.composeapp.generated.resources.send
import onthewakelive.composeapp.generated.resources.subscription_benefit_1
import onthewakelive.composeapp.generated.resources.subscription_benefit_2
import onthewakelive.composeapp.generated.resources.subscription_required
import onthewakelive.composeapp.generated.resources.upload_receipt
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.koinInject
import paywall.presentation.form.PaywallViewModel.PaywallAction.NavigateToPaywallInProcessScreen
import paywall.presentation.form.PaywallViewModel.PaywallAction.ShowError
import paywall.presentation.in_processing.PaywallInProcessingAssembly

class PaywallAssembly : Screen {

    @Composable
    override fun Content() {
        val viewModel: PaywallViewModel = koinInject()
        val state by viewModel.state.collectAsState()
        val navigator = LocalNavigator.current
        val snackBarHostState = remember { SnackbarHostState() }

        LaunchedEffect(key1 = true) {
            viewModel.actions.collect { action ->
                when (action) {
                    NavigateToPaywallInProcessScreen -> navigator?.replaceAll(
                        PaywallInProcessingAssembly()
                    )

                    is ShowError -> snackBarHostState.showSnackbar(message = action.errorMessage)
                }
            }
        }

        PaywallScreen(
            state = state,
            snackBarHostState = snackBarHostState,
            onEvent = viewModel::onEvent
        )
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
private fun PaywallScreen(
    state: PaywallState,
    snackBarHostState: SnackbarHostState,
    onEvent: (PaywallEvent) -> Unit
) {
    val imagePicker = rememberImagePickerLauncher {
        onEvent(PaywallEvent.OnReceiptSelected(receipt = it))
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) },
        containerColor = Color(0xFF1D1B20)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(vertical = 30.dp, horizontal = 24.dp)
        ) {
            Row(
                modifier = Modifier.height(intrinsicSize = IntrinsicSize.Max),
                verticalAlignment = Alignment.CenterVertically
            ) {
                VerticalDivider(
                    modifier = Modifier
                        .fillMaxHeight()
                        .clip(shape = MaterialTheme.shapes.extraSmall),
                    thickness = 4.dp,
                    color = Color(0xFF2E7AD3)
                )
                Text(
                    modifier = Modifier.padding(start = 20.dp),
                    text = stringResource(Res.string.subscription_required),
                    fontFamily = StolzlFontFamily(),
                    fontWeight = FontWeight.Normal,
                    color = Color.White,
                    fontSize = 19.sp
                )
            }
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 28.dp),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(Color(0xFF302E33), Color(0xFF27252A)),
                                start = Offset(0f, Float.POSITIVE_INFINITY),
                                end = Offset(Float.POSITIVE_INFINITY, 0f)
                            )
                        )
                        .padding(all = 16.dp)
                ) {
                    Row {
                        Text(text = "🌊", fontSize = 20.sp)

                        Text(
                            modifier = Modifier.padding(start = 8.dp),
                            text = stringResource(Res.string.subscription_benefit_1),
                            fontWeight = FontWeight.Normal,
                            fontSize = 14.sp,
                            color = Color.White
                        )
                    }
                    Row(modifier = Modifier.padding(top = 20.dp)) {
                        Text(text = "🌊", fontSize = 20.sp)

                        Text(
                            modifier = Modifier.padding(start = 8.dp),
                            text = stringResource(Res.string.subscription_benefit_2),
                            fontWeight = FontWeight.Normal,
                            fontSize = 14.sp,
                            color = Color.White
                        )
                    }
                }
            }
            Row(
                modifier = Modifier
                    .height(intrinsicSize = IntrinsicSize.Max)
                    .padding(top = 24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                VerticalDivider(
                    modifier = Modifier
                        .fillMaxHeight()
                        .clip(shape = MaterialTheme.shapes.extraSmall),
                    thickness = 4.dp,
                    color = Color(0xFF2E7AD3)
                )
                Text(
                    modifier = Modifier.padding(start = 20.dp),
                    text = stringResource(Res.string.one_time_seasonal_subscription),
                    fontFamily = StolzlFontFamily(),
                    fontWeight = FontWeight.Normal,
                    color = Color.White,
                    fontSize = 19.sp
                )
            }
            Card(modifier = Modifier.padding(top = 24.dp)) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(Color(0xFF302E33), Color(0xFF27252A)),
                                start = Offset(0f, Float.POSITIVE_INFINITY),
                                end = Offset(Float.POSITIVE_INFINITY, 0f)
                            )
                        )
                        .padding(all = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Mbank",
                        fontFamily = StolzlFontFamily(),
                        fontWeight = FontWeight.Normal,
                        color = Color.White,
                        fontSize = 20.sp
                    )
                    Text(
                        modifier = Modifier.padding(vertical = 12.dp),
                        text = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    brush = Brush.verticalGradient(
                                        colors = listOf(
                                            Color(0xFF009C4D),
                                            Color(0xFF008D6B),
                                            Color(0xFF007E8B)
                                        )
                                    )
                                )
                            ) {
                                append("600")
                            }
                            withStyle(style = SpanStyle(color = Color(0xFFFABF00))) {
                                append("с.")
                            }
                        },
                        fontSize = 62.sp,
                        fontFamily = StolzlFontFamily(),
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "+996 555 92 44 99",
                        fontFamily = StolzlFontFamily(),
                        fontWeight = FontWeight.Normal,
                        color = Color.White,
                        fontSize = 18.sp
                    )
                    Text(
                        modifier = Modifier.padding(top = 4.dp),
                        text = "Амаль Н.",
                        fontFamily = StolzlFontFamily(),
                        fontWeight = FontWeight.Light,
                        color = Color.White,
                        fontSize = 14.sp
                    )
                }
            }
            OutlinedCard(
                modifier = Modifier.padding(top = 24.dp),
                border = BorderStroke(
                    width = 1.dp,
                    color = if (state.receipt == null) {
                        Color(0xFF2E7AD3)
                    } else {
                        Color(0xFF019C4E)
                    }
                ),
                shape = RoundedCornerShape(size = 30.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.White
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 14.dp)
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() },
                            onClick = imagePicker::launch
                        ),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = stringResource(
                            if (state.receipt == null) {
                                Res.string.upload_receipt
                            } else {
                                Res.string.ready
                            }
                        ),
                        fontFamily = StolzlFontFamily(),
                        fontWeight = FontWeight.Light,
                        fontSize = 14.sp
                    )
                    Icon(
                        imageVector = if (state.receipt == null) {
                            Icons.Default.Upload
                        } else {
                            Icons.Default.Check
                        },
                        tint = if (state.receipt == null) {
                            Color.White
                        } else {
                            Color(0xFF019C4E)
                        },
                        contentDescription = null
                    )
                }
            }
            StandardButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                onClick = { onEvent(PaywallEvent.OnSubmitClicked) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2E7AD3),
                    contentColor = Color.White
                ),
                enabled = state.receipt != null,
                isLoading = state.isLoading,
                text = stringResource(Res.string.send),
                fontFamily = StolzlFontFamily(),
                fontWeight = FontWeight.Normal,
                innerPaddingValues = PaddingValues(vertical = 8.dp)
            )
        }
    }
}
