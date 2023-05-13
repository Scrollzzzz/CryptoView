package com.scrollz.cryptoview.utils

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus

fun timeForDayTicks(currentDateTime: String): String {
    return Instant.parse(currentDateTime.plus("Z"))
        .minus(DateTimeUnit.DAY, TimeZone.UTC)
        .plus(6, DateTimeUnit.MINUTE)
        .toString()
}

fun timeForYearTicks(currentDateTime: String): String {
    return Instant.parse(currentDateTime.plus("Z"))
        .minus(DateTimeUnit.YEAR, TimeZone.UTC)
        .plus(6, DateTimeUnit.MINUTE)
        .toString()
}