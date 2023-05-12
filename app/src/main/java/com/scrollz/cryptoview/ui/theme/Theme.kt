package com.scrollz.cryptoview.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val DarkColorScheme = darkColorScheme(
    background = DarkGray,
    onBackground = White,

    surface = Gray,
    onSurface = White,

    secondaryContainer = LightGray,
    onSecondaryContainer = BrightGray,
    onSecondary = BrightGray,

    primary = Gold,
    onPrimary = DarkGray,

    scrim = Scream
)

private val LightColorScheme = lightColorScheme(
    primary = White,
    secondary = White,
    tertiary = White

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun CryptoViewTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val systemUiController = rememberSystemUiController()
    systemUiController.setStatusBarColor(if (darkTheme) DarkGray else White)
    systemUiController.setNavigationBarColor(if (darkTheme) DarkGray else White)

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
