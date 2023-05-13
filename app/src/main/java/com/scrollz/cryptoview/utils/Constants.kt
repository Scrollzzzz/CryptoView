package com.scrollz.cryptoview.utils

object Constants {
    const val COIN_API_BASE_URL = "https://rest.coinapi.io/v1/"
    const val COINPAPRIKA_API_BASE_URL = "https://api.coinpaprika.com/v1/"
    const val TIME_API_BASE_URL = "https://www.timeapi.io/api/"
}

enum class Interval(val value: String) {
    ONE_HOUR("1h"),
    ONE_DAY("1d")
}

enum class Period(val value: String) {
    DAY("day"),
    YEAR("year")
}
