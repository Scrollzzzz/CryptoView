package com.scrollz.cryptoview.presentation.coinScreen

sealed class CoinEvent {
    data class ChoosePeriodFilter(val periodFilter: PeriodFilter): CoinEvent()
    object ToggleFavorite: CoinEvent()
    object EnableNotification: CoinEvent()
    object DisableNotification: CoinEvent()
}
