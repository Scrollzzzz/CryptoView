package com.scrollz.cryptoview.presentation.coinScreen

sealed class CoinEvent {
    data class ChoosePeriodFilter(val periodFilter: PeriodFilter): CoinEvent()
    data class EnableNotification(val hour: Int, val minute: Int): CoinEvent()
    object ToggleFavorite: CoinEvent()
    object DisableNotification: CoinEvent()
    object ShowNotificationDialog: CoinEvent()
    object CloseNotificationDialog: CoinEvent()
}
