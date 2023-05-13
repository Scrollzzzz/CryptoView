package com.scrollz.cryptoview.presentation.coinScreen.components

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import com.scrollz.cryptoview.presentation.coinScreen.CoinEvent
import com.scrollz.cryptoview.presentation.coinScreen.CoinState
import com.scrollz.cryptoview.presentation.common.ErrorScreen
import com.scrollz.cryptoview.presentation.common.LoadingBox
import com.scrollz.cryptoview.presentation.common.Status

@Composable
@ExperimentalMaterial3Api
fun CoinScreen(
    state: CoinState,
    onEvent: (CoinEvent) -> Unit,
    popBackStack: () -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    Crossfade(
        targetState = state.status,
        animationSpec = tween(700)
    ) { status ->
        when(status) {
            is Status.Loading -> {
                LoadingBox(backgroundColor = MaterialTheme.colorScheme.background)
            }
            is Status.Error -> {
                ErrorScreen()
            }
            is Status.Normal -> {
                if (state.coin == null) {
                    ErrorScreen()
                }
                else {
                    Scaffold(
//                        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
                        topBar = {
                            TopBar(
                                scrollBehavior = scrollBehavior,
                                isFavorite = state.isFavorite,
                                onFavoriteClick = { onEvent(CoinEvent.ToggleFavorite) },
                                popBackStack = popBackStack
                            )
                        }
                    ) { paddingValues ->
                        val scrollState = rememberScrollState()
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(paddingValues)
                                .padding(horizontal = 16.dp)
                                .verticalScroll(scrollState),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Top
                        ) {
                            MainInfo(
                                name = state.coin.name,
                                symbol = state.coin.symbol,
                                rank = state.coin.rank,
                                iconUrl = state.coin.iconUrl,
                                type = state.coin.type,
                                price = state.coin.price,
                                percentChange24h = state.coin.percentChange24h
                            )
                            if (state.ticks.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(16.dp))
                                PriceChart(ticks = state.ticks)
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                            MoreInfo(
                                marketCap = state.coin.marketCap,
                                marketCapChange24h = state.coin.marketCapChange24h,
                                volume24h = state.coin.volume24h,
                                volumeChange24h = state.coin.volumeChange24h,
                                priceATH = state.coin.priceATH,
                                percentFromATHPrice = state.coin.percentFromATHPrice,
                                circulatingSupply = state.coin.circulatingSupply,
                                totalSupply = state.coin.totalSupply,
                                maxSupply = state.coin.maxSupply
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                }
            }
        }
    }
}
