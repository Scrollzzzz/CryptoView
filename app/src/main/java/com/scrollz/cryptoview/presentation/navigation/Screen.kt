package com.scrollz.cryptoview.presentation.navigation

sealed class Screen(val route: String) {
    object CoinsListScreen: Screen(route = "coins_list")
    object CoinScreen: Screen(route = "coin")
}
