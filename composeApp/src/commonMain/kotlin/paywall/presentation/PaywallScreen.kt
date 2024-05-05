package paywall.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
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
import core.presentation.ui.theme.StolzlFontFamily
import core.presentation.utils.rememberImagePickerLauncher
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.koin.compose.koinInject

class PaywallAssembly : Screen {

    @Composable
    override fun Content() {
        val viewModel: PaywallViewModel = koinInject()
        val state by viewModel.state.collectAsState()

        PaywallScreen(state = state, onEvent = viewModel::onEvent)
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
private fun PaywallScreen(
    state: PaywallState,
    onEvent: (PaywallEvent) -> Unit
) {
    val imagePicker = rememberImagePickerLauncher {
        onEvent(PaywallEvent.OnReceiptSelected(receipt = it))
    }

    Scaffold { paddingValues ->
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
                    text = "Чтобы добавиться в очередь необходимо оформить подписку",
                    fontFamily = StolzlFontFamily(),
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.onSurface,
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
                            text = "С подпиской вы можете легко добавиться в очередь онлайн, избегая необходимости физического присутствия до начала вашего времени катания. Организуйте свои посещения без длительных ожиданий.",
                            fontWeight = FontWeight.Normal,
                            fontSize = 14.sp
                        )
                    }
                    Row(modifier = Modifier.padding(top = 20.dp)) {
                        Text(text = "🌊", fontSize = 18.sp)

                        Text(
                            modifier = Modifier.padding(start = 8.dp),
                            text = "Оформите подписку один раз и наслаждайтесь неограниченным доступом на весь сезон катания без дополнительных платежей. ",
                            fontWeight = FontWeight.Normal,
                            fontSize = 14.sp
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
                    text = "Единоразовая подписка на сезон, не требующая продления",
                    fontFamily = StolzlFontFamily(),
                    fontWeight = FontWeight.Normal,
                    color = MaterialTheme.colorScheme.onSurface,
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
                        color = MaterialTheme.colorScheme.onSurface,
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
                                append("500")
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
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 18.sp
                    )
                    Text(
                        modifier = Modifier.padding(top = 4.dp),
                        text = "Амаль Н.",
                        fontFamily = StolzlFontFamily(),
                        fontWeight = FontWeight.Light,
                        color = MaterialTheme.colorScheme.onSurface,
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
                shape = RoundedCornerShape(size = 30.dp)
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
                        text = if (state.receipt == null) {
                            "Загрузите квитанцию"
                        } else {
                            "Готово"
                        },
                        fontFamily = StolzlFontFamily(),
                        fontWeight = FontWeight.Light,
                        color = MaterialTheme.colorScheme.onSurface,
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
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
                onClick = { onEvent(PaywallEvent.OnSubmitClicked) },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7AD3))
            ) {
                Text(
                    modifier = Modifier.padding(vertical = 6.dp),
                    text = "Отправить",
                    fontFamily = StolzlFontFamily(),
                    fontWeight = FontWeight.Normal,
                    color = Color.White
                )
            }
        }
    }
}
