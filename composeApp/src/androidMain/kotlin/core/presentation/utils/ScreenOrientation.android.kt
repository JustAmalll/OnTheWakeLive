package core.presentation.utils

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration

@Composable
actual fun isPortraitOrientation(): Boolean {
    val orientation = LocalConfiguration.current.orientation
    return remember(orientation) { orientation == Configuration.ORIENTATION_PORTRAIT }
}