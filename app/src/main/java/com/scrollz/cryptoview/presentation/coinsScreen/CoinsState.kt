package com.scrollz.cryptoview.presentation.coinsScreen

import com.scrollz.cryptoview.domain.model.CoinView
import com.scrollz.cryptoview.presentation.common.Status

data class CoinsState(
    val status: Status = Status.Loading,
    val coinsList: List<CoinView> = emptyList(),
    val filter: Filter = Filter.All,
    val isSearching: Boolean = false,
    val searchText: String = "",
    val lastUpdated: String = ""
)
