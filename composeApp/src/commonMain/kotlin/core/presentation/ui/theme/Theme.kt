package core.presentation.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
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
    primaryContainer = md_theme_dark_primary,
    onPrimaryContainer = md_theme_dark_onPrimary,
    secondary = md_theme_dark_secondary,
    onSecondary = md_theme_dark_onSecondary,
    secondaryContainer = secondary,
    onSecondaryContainer = md_theme_dark_onSecondary,
    tertiary = md_theme_dark_primary,
    onTertiary = md_theme_dark_onPrimary,
    tertiaryContainer = md_theme_dark_primary,
    onTertiaryContainer = md_theme_dark_onPrimary,
    error = md_theme_dark_error,
    errorContainer = md_theme_dark_error,
    onError = md_theme_dark_onError,
    onErrorContainer = md_theme_dark_onError,
    background = md_theme_dark_background,
    onBackground = md_theme_dark_onBackground,
    surface = md_theme_dark_background,
    onSurface = md_theme_dark_onBackground,
    surfaceVariant = md_theme_dark_background,
    onSurfaceVariant = md_theme_dark_onBackground,
    outline = md_theme_dark_primary,
    outlineVariant = md_theme_dark_primary
)

fun Modifier.gradientBackground(radius: Dp = 0.dp) = drawBehind {
    val shadowOutline = RoundedCornerShape(size = radius)
        .createOutline(size, layoutDirection, this)

    val paint = Paint()
    paint.color = Color.White.copy(alpha = 0.15f)

    drawIntoCanvas { canvas ->
        canvas.save()
        canvas.translate(0f, -1.5.dp.toPx())
        canvas.drawOutline(shadowOutline, paint)
        canvas.restore()
    }
}.background(
    brush = Brush.linearGradient(listOf(Color(0xFF27252A), Color(0xFF302E33))),
    shape = RoundedCornerShape(size = radius)
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