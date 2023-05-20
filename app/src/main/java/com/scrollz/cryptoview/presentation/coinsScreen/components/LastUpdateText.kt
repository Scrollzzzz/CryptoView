package com.scrollz.cryptoview.presentation.coinsScreen.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.scrollz.cryptoview.R

@Composable
fun LastUpdateText(
    modifier: Modifier = Modifier,
    lastUpdated: String
) {
    Text(
        modifier = modifier,
        text = if (lastUpdated.isEmpty()) ""
                else "${stringResource(R.string.last_update)}: $lastUpdated",
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSecondary.copy(alpha = 0.3f)
    )
}
