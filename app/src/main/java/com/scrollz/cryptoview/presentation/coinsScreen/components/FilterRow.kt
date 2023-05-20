package com.scrollz.cryptoview.presentation.coinsScreen.components

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.scrollz.cryptoview.R
import com.scrollz.cryptoview.presentation.coinsScreen.Filter

@Composable
@ExperimentalMaterial3Api
fun FilterRow(
    filter: Filter,
    onFilterClick: (Filter) -> Unit
) {
    val scrollState = rememberScrollState()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .horizontalScroll(scrollState),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        FilterItem(
            text = stringResource(R.string.filter_all),
            selected = filter == Filter.All,
            onFilterClick = { onFilterClick(Filter.All) }
        )
        Spacer(modifier = Modifier.width(8.dp))
        FilterItem(
            text = stringResource(R.string.filter_favorites),
            selected = filter == Filter.Favorites,
            onFilterClick = { onFilterClick(Filter.Favorites) }
        )
    }
}

@Composable
fun FilterItem(
    text: String,
    selected: Boolean,
    onFilterClick: () -> Unit,
) {
    val surfaceColor by animateColorAsState(
        targetValue = if (selected)
            MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.secondary,
        animationSpec = tween(200)
    )
    val textColor by animateColorAsState(
        targetValue = if (selected)
            MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.onBackground,
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
