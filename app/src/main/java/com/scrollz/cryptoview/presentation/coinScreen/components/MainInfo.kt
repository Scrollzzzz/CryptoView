package com.scrollz.cryptoview.presentation.coinScreen.components

import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.scrollz.cryptoview.presentation.coinScreen.PeriodFilter
import com.scrollz.cryptoview.utils.toPercentFormat
import com.scrollz.cryptoview.utils.toPriceFormat

@Composable
fun MainInfo(
    name: String,
    symbol: String,
    rank: Int,
    iconUrl: String,
    type: String,
    price: Double,
    percentChange24h: Double,
    percentChange7d: Double,
    percentChange30d: Double,
    percentChange1y: Double,
    periodFilter: PeriodFilter
) {

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(32.dp),
        color = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            NameRow(
                name = name,
                iconUrl = iconUrl
            )
            Spacer(modifier = Modifier.height(16.dp))
            TagsRow(
                symbol = symbol,
                rank = rank,
                type = type
            )
            Spacer(modifier = Modifier.height(16.dp))
            Divider(thickness = 0.5.dp, color = MaterialTheme.colorScheme.outline)
            Spacer(modifier = Modifier.height(16.dp))
            PriceRow(
                price = price,
                percentChange = when(periodFilter) {
                    is PeriodFilter.Day -> percentChange24h
                    is PeriodFilter.Week -> percentChange7d
                    is PeriodFilter.Month -> percentChange30d
                    is PeriodFilter.Year -> percentChange1y
                }
            )
        }

    }
}

@Composable
fun NameRow(
    name: String,
    iconUrl: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        SubcomposeAsyncImage(
            modifier = Modifier.size(32.dp),
            model = ImageRequest.Builder(LocalContext.current)
                .data(iconUrl)
                .crossfade(true)
                .build(),
            contentDescription = null,
            loading = {
                Surface(
                    modifier = Modifier.size(32.dp),
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.surface
                ) {}
            },
            error = {
                Surface(
                    modifier = Modifier.size(32.dp),
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.surface
                ) {}
            },
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = name,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun TagsRow(
    symbol: String,
    rank: Int,
    type: String
) {
    val scrollState = rememberScrollState()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding()
            .horizontalScroll(scrollState),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        TagItem(text = symbol)
        Spacer(modifier = Modifier.width(4.dp))
        TagItem(text = "#$rank Rank")
        Spacer(modifier = Modifier.width(4.dp))
        TagItem(text = type)
    }
}

@Composable
fun TagItem(
    text: String
) {
    Surface(
        shape = RoundedCornerShape(25),
        color = MaterialTheme.colorScheme.secondaryContainer
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            text = text,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSecondaryContainer
        )
    }
}

@Composable
fun PriceRow(
    price: Double,
    percentChange: Double
) {

    val percentColor by animateColorAsState(
        targetValue = if (percentChange >= 0.0) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.inversePrimary,
        animationSpec = tween(200)
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = price.toPriceFormat(),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Surface(
            shape = RoundedCornerShape(25),
            color = percentColor
        ) {
            Row(
                modifier = Modifier.padding(start = 4.dp, end = 8.dp, top = 4.dp, bottom = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                Crossfade(
                    targetState = percentChange,
                    animationSpec = tween(200)
                ) { percentChange ->
                    Icon(
                        modifier = Modifier.size(18.dp),
                        imageVector = if (percentChange >= 0.0) Icons.Default.ArrowDropUp
                                        else Icons.Default.ArrowDropDown,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.background
                    )
                }
                Text(
                    text = percentChange.toPercentFormat(),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.background
                )
            }
        }
    }
}
