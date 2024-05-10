package core.presentation.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun StandardButton(
    modifier: Modifier = Modifier,
    text: String,
    isLoading: Boolean = false,
    loadingIndicatorSize: Dp = 24.dp,
    innerPaddingValues: PaddingValues = PaddingValues(),
    enabled: Boolean = true,
    fontWeight: FontWeight? = null,
    fontFamily: FontFamily? = null,
    colors: ButtonColors = ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary
    ),
    onClick: () -> Unit
) {
    Button(
        onClick = {
            if (!isLoading) {
                onClick()
            }
        },
        modifier = modifier,
        enabled = enabled,
        colors = colors
    ) {
        AnimatedContent(
            modifier = Modifier.padding(paddingValues = innerPaddingValues),
            targetState = isLoading
        ) { loading ->
            if (loading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(loadingIndicatorSize),
                    strokeWidth = 2.dp,
                    color = colors.contentColor
                )
            } else {
                Text(
                    text = text,
                    fontWeight = fontWeight,
                    fontFamily = fontFamily
                )
            }
        }
    }
}