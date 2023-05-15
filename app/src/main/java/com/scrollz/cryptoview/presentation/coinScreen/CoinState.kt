package com.scrollz.cryptoview.presentation.coinScreen

import com.scrollz.cryptoview.domain.model.DetailedCoin
import com.scrollz.cryptoview.domain.model.Tick
import com.scrollz.cryptoview.presentation.common.Status

data class CoinState(
    val status: Status = Status.Loading,
    val chartStatus: Status = Status.Loading,
    val coin: DetailedCoin? = null,
    val isFavorite: Boolean = false,
    val ticks: List<Tick> = emptyList(),
    val periodFilter: PeriodFilter = PeriodFilter.Day
)
