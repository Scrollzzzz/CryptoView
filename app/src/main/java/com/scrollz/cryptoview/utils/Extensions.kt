package com.scrollz.cryptoview.utils

import com.scrollz.cryptoview.domain.model.dto.CoinDTO
import com.scrollz.cryptoview.domain.model.Coin
import com.scrollz.cryptoview.domain.model.CoinView
import com.scrollz.cryptoview.domain.model.DetailedCoin
import com.scrollz.cryptoview.domain.model.dto.CoinInfoDTO
import kotlin.math.abs

fun Double.toPercentFormat(): String {
    return "%.2f".format(abs(this)).replace(',', '.').plus('%')
}

fun Double.toPriceFormat(): String {
    return if (this.toInt() != 0) {
        val str = "$".plus("%.2f".format(this).replace(',', '.'))
        val stringBuilder = StringBuilder(str)
        for (i in str.length - 6 downTo 2 step 3) {
            stringBuilder.insert(i, ',')
        }
        return stringBuilder.toString()
    }
    else {
        val fraction = this.toString().drop(2)
        val end = fraction.indexOfAny(charArrayOf('1', '2', '3', '4', '5', '6', '7', '8', '9')) + 4
        if (end > fraction.lastIndex) {
            "$0.".plus(fraction)
        }
        else {
            "$0.".plus(fraction.removeRange(end..fraction.lastIndex))
        }
    }
}

fun Double.toValueFormat(): String {
    val str = this.toLong().toString()
    val stringBuilder = StringBuilder(str)
    for (i in str.length - 3 downTo 1 step 3) {
        stringBuilder.insert(i, ',')
    }
    return "$".plus(stringBuilder.toString())
}

fun Double.toSupplyFormat(): String {
    val supply = this.toLong()
    return if (supply == 0L) {
        "--"
    }
    else {
        val str = supply.toString()
        val stringBuilder = StringBuilder(str)
        for (i in str.length - 3 downTo 1 step 3) {
            stringBuilder.insert(i, ',')
        }
        stringBuilder.toString()
    }
}

fun CoinDTO.toCoin(iconUrl: String?): Coin {
    return Coin(
        id = id,
        name = name,
        symbol = symbol,
        rank = rank,
        price = quotes.usd.price,
        percentChange24h = quotes.usd.percentChange24h,
        iconUrl = iconUrl
    )
}

fun Coin.toCoinView(): CoinView {
    return CoinView(
        id = id,
        name = name,
        symbol = symbol,
        rank = rank,
        price = price.toPriceFormat(),
        percentChange24h = percentChange24h.toPercentFormat(),
        iconUrl = iconUrl,
        isPercentPositive = percentChange24h >= 0.0
    )
}

fun createDetailedCoin(coin: CoinDTO, coinInfo: CoinInfoDTO): DetailedCoin {
    return DetailedCoin(
        id = coin.id,
        name = coin.name,
        symbol = coin.symbol,
        rank = coin.rank,
        iconUrl = coinInfo.logo,
        type = coinInfo.type,
        price = coin.quotes.usd.price,
        percentChange1h = coin.quotes.usd.percentChange1h,
        percentChange12h = coin.quotes.usd.percentChange12h,
        percentChange24h = coin.quotes.usd.percentChange24h,
        percentChange7d = coin.quotes.usd.percentChange7d,
        percentChange30d = coin.quotes.usd.percentChange30d,
        percentChange1y = coin.quotes.usd.percentChange1y,
        circulatingSupply = coin.circulatingSupply,
        totalSupply = coin.totalSupply,
        maxSupply = coin.maxSupply,
        volume24h = coin.quotes.usd.volume24h,
        volumeChange24h = coin.quotes.usd.volume24hChange24h,
        marketCap = coin.quotes.usd.marketCap,
        marketCapChange24h = coin.quotes.usd.marketCapChange24h,
        priceATH = coin.quotes.usd.athPrice,
        percentFromATHPrice = coin.quotes.usd.percentFromPriceAth,
        last_updated = coin.lastUpdated
    )
}

fun CoinView.contains(query: String) = this.name.contains(query, ignoreCase = true) or
                                        this.symbol.contains(query, ignoreCase = true)
