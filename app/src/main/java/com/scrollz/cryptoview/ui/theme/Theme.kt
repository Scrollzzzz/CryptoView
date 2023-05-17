package com.scrollz.cryptoview.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val DarkColorScheme = darkColorScheme(
    background = DarkGrayD,
    onBackground = WhiteD,

    surface = GrayD,
    onSurface = WhiteD,

    primary = Green,
    inversePrimary = Red,
    onPrimary = DarkGrayD,

    secondary = LightGrayD,
    secondaryContainer = LightGrayD,
    onSecondaryContainer = BrightGrayD,
    onSecondary = BrightGrayD,

    tertiary = WhiteD,

    outline = BlackD
)

private val LightColorScheme = lightColorScheme(
    background = LightWhiteL,
    onBackground = DarkGrayL,

    surface = WhiteL,
    onSurface = DarkGrayL,

    primary = Green,
    inversePrimary = Red,
    onPrimary = LightWhiteL,

    secondary = HeavyWhiteL,
    secondaryContainer = MiddleWhiteL,
    onSecondaryContainer = GrayL,
    onSecondary = GrayL,

    tertiary = DarkGrayL,

    outline = LightGrayL
)

@Composable
fun CryptoViewTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val systemUiController = rememberSystemUiController()
    systemUiController.setStatusBarColor(if (darkTheme) DarkGrayD else LightWhiteL)
    systemUiController.setNavigationBarColor(if (darkTheme) DarkGrayD else LightWhiteL)

    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
