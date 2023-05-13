package com.scrollz.cryptoview.domain.model

data class HistoricalTicks(
    val day: List<Tick>,
    val year: List<Tick>
)
