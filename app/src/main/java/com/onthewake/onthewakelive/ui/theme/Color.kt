package com.onthewake.onthewakelive.ui.theme

import androidx.compose.material.Colors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Purple200 = Color(0xFF4285F4)
val Purple500 = Color(0xFF4285F4)
val Purple700 = Color(0xFF4285F4)
val Teal200 = Color(0xFF03DAC5)

val Primary = Color(0xFF45AAf2)

val darkThemeBgColor = Color(0xFF303030)
val darkThemeItemBgColor = Color(0xFF424242)

val Colors.BackgroundColor: Color
    @Composable
    get() = if (isLight) Color.White else darkThemeBgColor

val Colors.ItemBgColor: Color
    @Composable
    get() = if (isLight) Color.White else darkThemeItemBgColor
