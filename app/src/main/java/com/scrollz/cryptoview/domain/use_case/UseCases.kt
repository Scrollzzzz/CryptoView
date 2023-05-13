package com.scrollz.cryptoview.domain.use_case

data class UseCases(
    val getCoins: GetCoins,
    val getDetailedCoin: GetDetailedCoin,
    val getHistoricalTicks: GetHistoricalTicks,
    val getFavorites: GetFavorites,
    val isCoinFavorite: IsCoinFavorite,
    val toggleFavorite: ToggleFavorite
)
