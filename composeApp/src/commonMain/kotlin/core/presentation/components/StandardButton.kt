package core.presentation.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalResourceApi::class)
@Composable
fun StandardButton(
    modifier: Modifier = Modifier,
    text: String,
    isLoading: Boolean = false,
    loadingIndicatorSize: Dp = 24.dp,
    enabled: Boolean = true,
    colors: ButtonColors = ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary
    ),
    icon: DrawableResource? = null,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier.fillMaxWidth(),
        onClick = {
            if (!isLoading) {
                onClick()
            }
        },
        enabled = enabled,
        colors = colors,
        shape = RoundedCornerShape(size = 30.dp)
    ) {
        AnimatedContent(
            modifier = Modifier.padding(vertical = 8.dp),
            targetState = isLoading
        ) { loading ->
            if (loading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(loadingIndicatorSize),
                    strokeWidth = 2.dp,
                    color = colors.contentColor
                )
            } else {
                Box(modifier = if (icon != null) Modifier.fillMaxWidth() else Modifier) {
                    Text(
                        modifier = Modifier.align(Alignment.Center),
                        text = text,
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp
                    )
                    if (icon != null) {
                        Icon(
                            modifier = Modifier.align(Alignment.CenterEnd),
                            painter = painterResource(resource = icon),
                            contentDescription = null
                        )
                    }
                }
            }
        }
    }
}