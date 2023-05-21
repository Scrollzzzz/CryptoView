package com.scrollz.cryptoview.utils

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime

fun String.timeForDayTicks(): String {
    return Instant.parse(this.plus("Z"))
        .minus(DateTimeUnit.DAY, TimeZone.UTC)
        .plus(6, DateTimeUnit.MINUTE)
        .toString()
}

fun String.timeForYearTicks(): String {
    return Instant.parse(this.plus("Z"))
        .minus(DateTimeUnit.YEAR, TimeZone.UTC)
        .plus(6, DateTimeUnit.MINUTE)
        .toString()
}

fun String.toLocalDateTime(): String {
    return Instant.parse(this)
        .toLocalDateTime(TimeZone.currentSystemDefault())
        .toString()
        .replace('T', ' ')
}

fun String.shouldUpdateIcons(currentTime: String): Boolean {
    return Instant.parse(currentTime)
        .minus(24, DateTimeUnit.HOUR) >= Instant.parse(this)
}
