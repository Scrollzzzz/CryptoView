package com.scrollz.cryptoview.presentation.coinsScreen.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.scrollz.cryptoview.presentation.coinsScreen.CoinsEvent
import com.scrollz.cryptoview.presentation.coinsScreen.CoinsState
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
    val scrollUp = { scope.launch {
        delay(50)
        lazyListState.scrollToItem(0)
    } }

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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            FilterRow(
                filter = state.filter,
                onFilterClick = { filter -> onEvent(CoinsEvent.ChooseFilter(filter)) }
            )
            Spacer(modifier = Modifier.height(16.dp))
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
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
                        color = Color.Black
                    )
                }
            }
        }
    }
}
