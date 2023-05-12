package com.scrollz.cryptoview.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.ExperimentalMaterial3Api
import com.scrollz.cryptoview.presentation.navigation.Navigation
import com.scrollz.cryptoview.ui.theme.CryptoViewTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@ExperimentalMaterial3Api
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CryptoViewTheme {
                Navigation()
            }
        }
    }
}
