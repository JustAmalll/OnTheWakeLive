package core.presentation.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import onthewakelive.composeapp.generated.resources.Res
import onthewakelive.composeapp.generated.resources.stolzl_bold
import onthewakelive.composeapp.generated.resources.stolzl_book
import onthewakelive.composeapp.generated.resources.stolzl_light
import onthewakelive.composeapp.generated.resources.stolzl_medium
import onthewakelive.composeapp.generated.resources.stolzl_regular
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.Font

val DarkColors = darkColorScheme(
    primary = md_theme_dark_primary,
    onPrimary = md_theme_dark_onPrimary,
    primaryContainer = md_theme_dark_primaryContainer,
    onPrimaryContainer = md_theme_dark_onPrimaryContainer,
    secondary = md_theme_dark_secondary,
    onSecondary = md_theme_dark_onSecondary,
    secondaryContainer = md_theme_dark_secondaryContainer,
    onSecondaryContainer = md_theme_dark_onSecondaryContainer,
    tertiary = md_theme_dark_tertiary,
    onTertiary = md_theme_dark_onTertiary,
    tertiaryContainer = md_theme_dark_tertiaryContainer,
    onTertiaryContainer = md_theme_dark_onTertiaryContainer,
    error = md_theme_dark_error,
    errorContainer = md_theme_dark_errorContainer,
    onError = md_theme_dark_onError,
    onErrorContainer = md_theme_dark_onErrorContainer,
    background = md_theme_dark_background,
    onBackground = md_theme_dark_onBackground,
    surface = md_theme_dark_surface,
    onSurface = md_theme_dark_onSurface,
    surfaceVariant = md_theme_dark_surfaceVariant,
    onSurfaceVariant = md_theme_dark_onSurfaceVariant,
    outline = md_theme_dark_outline,
    inverseOnSurface = md_theme_dark_inverseOnSurface,
    inverseSurface = md_theme_dark_inverseSurface,
    inversePrimary = md_theme_dark_inversePrimary,
    surfaceTint = md_theme_dark_surfaceTint,
    outlineVariant = md_theme_dark_outlineVariant,
    scrim = md_theme_dark_scrim
)

@OptIn(ExperimentalResourceApi::class)
@Composable
fun StolzlFontFamily() = FontFamily(
    Font(Res.font.stolzl_light, weight = FontWeight.Light),
    Font(Res.font.stolzl_book, weight = FontWeight.Normal),
    Font(Res.font.stolzl_regular, weight = FontWeight.Medium),
    Font(Res.font.stolzl_medium, weight = FontWeight.SemiBold),
    Font(Res.font.stolzl_bold, weight = FontWeight.Bold)
)

@Composable
expect fun OnTheWakeLiveTheme(
    typography: Typography = Typography(
        displayLarge = MaterialTheme.typography.displayLarge.copy(fontFamily = StolzlFontFamily()),
        displayMedium = MaterialTheme.typography.displayMedium.copy(fontFamily = StolzlFontFamily()),
        displaySmall = MaterialTheme.typography.displaySmall.copy(fontFamily = StolzlFontFamily()),
        headlineLarge = MaterialTheme.typography.headlineLarge.copy(fontFamily = StolzlFontFamily()),
        headlineMedium = MaterialTheme.typography.headlineMedium.copy(fontFamily = StolzlFontFamily()),
        headlineSmall = MaterialTheme.typography.headlineSmall.copy(fontFamily = StolzlFontFamily()),
        titleLarge = MaterialTheme.typography.titleLarge.copy(fontFamily = StolzlFontFamily()),
        titleMedium = MaterialTheme.typography.titleMedium.copy(fontFamily = StolzlFontFamily()),
        titleSmall = MaterialTheme.typography.titleSmall.copy(fontFamily = StolzlFontFamily()),
        bodyLarge = MaterialTheme.typography.bodyLarge.copy(fontFamily = StolzlFontFamily()),
        bodyMedium = MaterialTheme.typography.bodyMedium.copy(fontFamily = StolzlFontFamily()),
        bodySmall = MaterialTheme.typography.bodySmall.copy(fontFamily = StolzlFontFamily()),
        labelLarge = MaterialTheme.typography.labelLarge.copy(fontFamily = StolzlFontFamily()),
        labelMedium = MaterialTheme.typography.labelMedium.copy(fontFamily = StolzlFontFamily()),
        labelSmall = MaterialTheme.typography.labelSmall.copy(fontFamily = StolzlFontFamily())
    ),
    content: @Composable () -> Unit
)