package com.scrollz.cryptoview.presentation.coinScreen

sealed class PeriodFilter {
    object Day : PeriodFilter()
    object Week : PeriodFilter()
    object Month : PeriodFilter()
    object Year : PeriodFilter()
}
