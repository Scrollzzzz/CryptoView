package com.scrollz.cryptoview.presentation.coinsScreen

sealed class Filter {
    object All: Filter()
    object Favorites: Filter()
}
