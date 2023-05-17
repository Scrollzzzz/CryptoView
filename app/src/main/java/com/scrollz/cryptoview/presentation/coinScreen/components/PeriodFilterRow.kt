package com.scrollz.cryptoview.presentation.coinScreen.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.scrollz.cryptoview.presentation.coinScreen.PeriodFilter

@Composable
@ExperimentalMaterial3Api
fun PeriodFilterRow(
    periodFilter: PeriodFilter,
    onFilterClick: (PeriodFilter) -> Unit
) {
    val scrollState = rememberScrollState()

    Surface(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(32.dp),
        color = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .horizontalScroll(scrollState),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            FilterItem(
                text = "Day",
                selected = periodFilter == PeriodFilter.Day,
                onFilterClick = { onFilterClick(PeriodFilter.Day) }
            )
            Spacer(modifier = Modifier.width(8.dp))
            FilterItem(
                text = "Week",
                selected = periodFilter == PeriodFilter.Week,
                onFilterClick = { onFilterClick(PeriodFilter.Week) }
            )
            Spacer(modifier = Modifier.width(8.dp))
            FilterItem(
                text = "Month",
                selected = periodFilter == PeriodFilter.Month,
                onFilterClick = { onFilterClick(PeriodFilter.Month) }
            )
            Spacer(modifier = Modifier.width(8.dp))
            FilterItem(
                text = "Year",
                selected = periodFilter == PeriodFilter.Year,
                onFilterClick = { onFilterClick(PeriodFilter.Year) }
            )
        }
    }
}

@Composable
fun FilterItem(
    text: String,
    selected: Boolean,
    onFilterClick: () -> Unit
) {
    val surfaceColor by animateColorAsState(
        targetValue = if (selected) MaterialTheme.colorScheme.tertiary
                        else MaterialTheme.colorScheme.secondaryContainer,
        animationSpec = tween(200)
    )
    val textColor by animateColorAsState(
        targetValue = if (selected) MaterialTheme.colorScheme.background
                        else MaterialTheme.colorScheme.onBackground,
        animationSpec = tween(200)
    )

    Surface(
        modifier = Modifier
            .clip(RoundedCornerShape(25))
            .clickable(
                enabled = !selected,
                onClick = onFilterClick
            ),
        shape = RoundedCornerShape(25),
        color = surfaceColor
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
            text = text,
            style = MaterialTheme.typography.labelLarge,
            color = textColor
        )
    }
}