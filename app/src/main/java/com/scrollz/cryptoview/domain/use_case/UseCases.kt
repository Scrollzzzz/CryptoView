package com.scrollz.cryptoview.domain.use_case

data class UseCases(
    val getCoins: GetCoins,
    val getDetailedCoin: GetDetailedCoin,
    val getHistoricalTicks: GetHistoricalTicks,
    val getFavorites: GetFavorites,
    val isCoinFavorite: IsCoinFavorite,
    val toggleFavorite: ToggleFavorite,
    val enableNotification: EnableNotification,
    val disableNotification: DisableNotification,
    val getNotification: GetNotification,
    val updateCoinIcons: UpdateCoinIcons
)
