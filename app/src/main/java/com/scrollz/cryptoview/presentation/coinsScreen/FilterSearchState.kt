package com.scrollz.cryptoview.presentation.coinsScreen

data class FilterSearchState(
    val filter: Filter = Filter.All,
    val searchText: String = ""
)
