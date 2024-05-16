package core.presentation.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable

@Composable
actual fun OnTheWakeLiveTheme(typography: Typography, content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = DarkColors, typography = typography, content = content)
}