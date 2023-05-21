package com.scrollz.cryptoview.presentation.coinsScreen.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.scrollz.cryptoview.presentation.coinsScreen.CoinsEvent
import com.scrollz.cryptoview.presentation.coinsScreen.CoinsState
import com.scrollz.cryptoview.presentation.common.Status
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
@ExperimentalMaterial3Api
fun CoinsScreen(
    state: CoinsState,
    onEvent: (CoinsEvent) -> Unit,
    onCoinClick: (String) -> Unit
) {
    val scope = rememberCoroutineScope()
    val lazyListState = rememberLazyListState()
    val scrollUp = remember { { scope.launch {
        delay(50)
        lazyListState.scrollToItem(0)
    } } }
    val isLoading by remember(state.status) {
        derivedStateOf { state.status == Status.Loading }
    }
    val swipeRefreshState = rememberSwipeRefreshState(
        isRefreshing = isLoading
    )

    Scaffold(
        topBar = {
            TopBar(
                isSearching = state.isSearching,
                searchText = state.searchText,
                scrollUp = { scrollUp() },
                toggleSearch = { onEvent(CoinsEvent.ToggleSearch) },
                onSearchTextChange = { text -> onEvent(CoinsEvent.ChangeSearchText(text))}
            )
        }
    ) { paddingValues ->
        SwipeRefresh(
            state = swipeRefreshState,
            onRefresh = { onEvent(CoinsEvent.Refresh) },
            indicator = { swipeRefreshState, dp ->
                if (!isLoading) {
                    SwipeRefreshIndicator(
                        state = swipeRefreshState,
                        refreshTriggerDistance = dp,
                        fade = false,
                        backgroundColor = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.onBackground
                    )
                }
            },
            indicatorPadding = PaddingValues(paddingValues.calculateTopPadding() + 48.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                FilterRow(
                    filter = state.filter,
                    onFilterClick = { filter ->
                        onEvent(CoinsEvent.ChooseFilter(filter))
                        scrollUp()
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
                if (isLoading) {
                    LinearProgressIndicator(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(0.5.dp),
                        color = MaterialTheme.colorScheme.onBackground,
                        trackColor = MaterialTheme.colorScheme.background,
                        strokeCap = StrokeCap.Round
                    )
                }
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1.0f, fill = true),
                    state = lazyListState
                ) {
                    items(
                        items = state.coinsList,
                        key = { coin -> coin.rank }
                    ) { coin ->
                        CoinItem(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable(
                                    onClick = { onCoinClick(coin.id) }
                                ),
                            coin = coin
                        )
                        Divider(
                            modifier = Modifier.padding(start = 64.dp, end = 16.dp),
                            thickness = 0.5.dp,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                LastUpdateText(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    lastUpdated = state.lastUpdated
                )
            }
        }
    }
}
