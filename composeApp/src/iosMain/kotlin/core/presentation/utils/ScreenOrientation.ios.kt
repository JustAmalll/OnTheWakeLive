package core.presentation.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@Composable
actual fun isPortraitOrientation(): Boolean = remember { true }