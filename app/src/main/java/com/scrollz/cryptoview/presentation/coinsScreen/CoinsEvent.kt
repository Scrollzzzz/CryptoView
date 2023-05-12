package com.scrollz.cryptoview.presentation.coinsScreen

sealed class CoinsEvent {
    data class ChooseFilter(val filter: Filter): CoinsEvent()
    data class ChangeSearchText(val text: String): CoinsEvent()
    object ToggleSearch: CoinsEvent()
}
