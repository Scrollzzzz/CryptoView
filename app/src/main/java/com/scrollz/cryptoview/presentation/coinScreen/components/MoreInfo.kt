package com.scrollz.cryptoview.presentation.coinScreen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.scrollz.cryptoview.R
import com.scrollz.cryptoview.utils.toPercentFormat
import com.scrollz.cryptoview.utils.toPriceFormat
import com.scrollz.cryptoview.utils.toSupplyFormat
import com.scrollz.cryptoview.utils.toValueFormat

@Composable
fun MoreInfo(
    marketCap: Double,
    marketCapChange24h: Double,
    volume24h: Double,
    volumeChange24h: Double,
    priceATH: Double,
    percentFromATHPrice: Double,
    circulatingSupply: Double,
    totalSupply: Double,
    maxSupply: Double
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
            Text(
                text = stringResource(R.string.more_info_market_cap),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            ValueChangeItem(value = marketCap, percentChange = marketCapChange24h)

            Spacer(modifier = Modifier.height(16.dp))
            Divider(thickness = 0.5.dp, color = MaterialTheme.colorScheme.outline)
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.more_info_volume),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            ValueChangeItem(value = volume24h, percentChange = volumeChange24h)

            Spacer(modifier = Modifier.height(16.dp))
            Divider(thickness = 0.5.dp, color = MaterialTheme.colorScheme.outline)
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.more_info_ath),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            ValueChangeItem(value = priceATH, percentChange = percentFromATHPrice, true)

            Spacer(modifier = Modifier.height(16.dp))
            Divider(thickness = 0.5.dp, color = MaterialTheme.colorScheme.outline)
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.more_info_supply),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            SupplyItem(
                name = stringResource(R.string.more_info_supply_circulating),
                value = circulatingSupply
            )
            Spacer(modifier = Modifier.height(4.dp))
            SupplyItem(
                name = stringResource(R.string.more_info_supply_total),
                value = totalSupply
            )
            Spacer(modifier = Modifier.height(4.dp))
            SupplyItem(
                name = stringResource(R.string.more_info_supply_max),
                value = maxSupply
            )
        }
    }
}

@Composable
fun ValueChangeItem(
    value: Double,
    percentChange: Double,
    priceFormat: Boolean = false
) {
    val percentColor = when {
        percentChange > 0.0 -> MaterialTheme.colorScheme.primary
        percentChange < 0.0 -> MaterialTheme.colorScheme.inversePrimary
        else -> MaterialTheme.colorScheme.onSecondary
    }
    val percentIcon = when {
        percentChange > 0.0 -> Icons.Default.ArrowDropUp
        percentChange < 0.0 -> Icons.Default.ArrowDropDown
        else -> Icons.Default.Remove
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = if (priceFormat) value.toPriceFormat() else value.toValueFormat(),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSecondary
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            Icon(
                modifier = Modifier.size(18.dp),
                imageVector = percentIcon,
                contentDescription = null,
                tint = percentColor
            )
            Text(
                text = percentChange.toPercentFormat(),
                style = MaterialTheme.typography.bodyMedium,
                color = percentColor
            )
        }
    }
}


@Composable
fun SupplyItem(
    name: String,
    value: Double
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = name,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSecondary
        )
        Text(
            text = value.toSupplyFormat(),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSecondary
        )
    }
}
