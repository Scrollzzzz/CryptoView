package com.scrollz.cryptoview.presentation.navigation

import android.content.Intent
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.scrollz.cryptoview.presentation.coinScreen.CoinViewModel
import com.scrollz.cryptoview.presentation.coinScreen.components.CoinScreen
import com.scrollz.cryptoview.presentation.coinsScreen.CoinsViewModel
import com.scrollz.cryptoview.presentation.coinsScreen.components.CoinsScreen

@Composable
@ExperimentalMaterial3Api
fun Navigation() {

    val navController = rememberNavController()

    Surface(
        color = MaterialTheme.colorScheme.background
    ) {
        NavHost(
            navController = navController,
            startDestination = Screen.CoinsListScreen.route,
        ) {
            composable(
                route = Screen.CoinsListScreen.route
            ) {
                val viewModel = hiltViewModel<CoinsViewModel>()
                val state by viewModel.coinsState.collectAsState()
                CoinsScreen(
                    state = state,
                    onEvent = viewModel::onEvent,
                    onCoinClick = { coinID ->
                        navController.navigate(Screen.CoinScreen.route + "/$coinID")
                    }
                )
            }
            composable(
                route = Screen.CoinScreen.route + "/{coinID}",
                arguments = listOf(
                    navArgument(
                        name = "coinID"
                    ) {
                        type = NavType.StringType
                    },
                ),
                deepLinks = listOf(
                    navDeepLink {
                        uriPattern = "https://rtuitlab.dev/crypto/{coinID}"
                        action = Intent.ACTION_VIEW
                    },
                    navDeepLink {
                        uriPattern = "https://com.scrollz.cryptoview/coin/{coinID}"
                        action = Intent.ACTION_VIEW
                    }
                )
            ) {
                val viewModel = hiltViewModel<CoinViewModel>()
                val state by viewModel.coinState.collectAsState()
                CoinScreen(
                    state = state,
                    onEvent = viewModel::onEvent,
                    popBackStack = { navController.popBackStack() }
                )
            }
        }
    }
}
