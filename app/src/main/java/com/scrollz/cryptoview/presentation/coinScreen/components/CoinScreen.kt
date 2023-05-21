package com.scrollz.cryptoview.presentation.coinScreen.components

import android.Manifest
import android.app.Activity
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshIndicator
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.scrollz.cryptoview.R
import com.scrollz.cryptoview.presentation.coinScreen.CoinEvent
import com.scrollz.cryptoview.presentation.coinScreen.CoinState
import com.scrollz.cryptoview.presentation.coinsScreen.components.LastUpdateText
import com.scrollz.cryptoview.presentation.common.ErrorBox
import com.scrollz.cryptoview.presentation.common.LoadingBox
import com.scrollz.cryptoview.presentation.common.Status
import kotlinx.coroutines.launch

@Composable
@ExperimentalMaterial3Api
fun CoinScreen(
    state: CoinState,
    onEvent: (CoinEvent) -> Unit,
    popBackStack: () -> Unit,
) {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val isLoading by remember(state.status) {
        derivedStateOf { state.status == Status.Loading }
    }
    val swipeRefreshState = rememberSwipeRefreshState(
        isRefreshing = isLoading
    )

    val notificationPermissionRequestText = stringResource(R.string.notification_permission_request)
    val notificationPermissionResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                onEvent(CoinEvent.ShowNotificationDialog)
            } else {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(
                    context as Activity,
                    Manifest.permission.POST_NOTIFICATIONS
                )) {
                    scope.launch {
                        snackbarHostState.showSnackbar(notificationPermissionRequestText)
                    }
                }
            }
        }
    )

    val showNotificationDialog = remember { {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            notificationPermissionResultLauncher.launch(
                Manifest.permission.POST_NOTIFICATIONS
            )
        } else {
            onEvent(CoinEvent.ShowNotificationDialog)
        }
    } }

    val errorScreen: @Composable () -> Unit = {
        ErrorBox(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .background(MaterialTheme.colorScheme.background),
            text = {
                Text(
                    text = stringResource(R.string.error_coin_page_message),
                    style = MaterialTheme.typography.titleMedium.copy(
                        textAlign = TextAlign.Center
                    ),
                    color = MaterialTheme.colorScheme.onBackground
                )
            },
            button = {
                Button(
                    onClick = { onEvent(CoinEvent.Refresh) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.onBackground,
                        contentColor = MaterialTheme.colorScheme.background
                    )
                ) {
                    Text(
                        modifier = Modifier.padding(vertical = 6.dp),
                        text = stringResource(R.string.error_button),
                        style = MaterialTheme.typography.displayMedium
                    )
                }
            }
        )
    }

    Crossfade(
        targetState = state.status,
        animationSpec = tween(700)
    ) { status ->
        when(status) {
            is Status.Loading -> {
                LoadingBox(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                )
            }
            is Status.Error -> {
                errorScreen()
            }
            is Status.Normal -> {
                if (state.coin == null) {
                    errorScreen()
                }
                else {
                    val timePickerState = rememberTimePickerState(
                        state.notification?.hour ?: 0,
                        state.notification?.minute ?: 0,
                        is24Hour = true
                    )

                    NotificationDialog(
                        isNotificationDialogVisible = state.isNotificationDialogVisible,
                        isNotificationOn = state.notification != null,
                        timePickerState = timePickerState,
                        closeDialog = { onEvent(CoinEvent.CloseNotificationDialog) },
                        disableNotification = { onEvent(CoinEvent.DisableNotification) },
                        enableNotification = {
                            onEvent(CoinEvent.EnableNotification(
                                timePickerState.hour,
                                timePickerState.minute
                            ))
                        }
                    )

                    Scaffold(
                        topBar = {
                            TopBar(
                                isFavorite = state.isFavorite,
                                isNotificationOn = state.notification != null,
                                showNotificationDialog = showNotificationDialog,
                                onFavoriteClick = { onEvent(CoinEvent.ToggleFavorite) },
                                popBackStack = popBackStack
                            )
                        },
                        snackbarHost = {
                            SnackbarHost(
                                hostState = snackbarHostState,
                                snackbar = {
                                    Snackbar(
                                        modifier = Modifier.padding(16.dp),
                                        shape = RoundedCornerShape(24.dp),
                                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                                        contentColor = MaterialTheme.colorScheme.onBackground,
                                    ) {
                                        Text(
                                            modifier = Modifier.padding(bottom = 8.dp),
                                            text = it.visuals.message,
                                            style = MaterialTheme.typography.bodyLarge,
                                            color = MaterialTheme.colorScheme.onBackground
                                        )
                                    }
                                }
                            )
                        }
                    ) { paddingValues ->
                        SwipeRefresh(
                            state = swipeRefreshState,
                            onRefresh = { onEvent(CoinEvent.Refresh) },
                            indicator = { swipeRefreshState, dp ->
                                SwipeRefreshIndicator(
                                    state = swipeRefreshState,
                                    refreshTriggerDistance = dp,
                                    fade = false,
                                    backgroundColor = MaterialTheme.colorScheme.secondaryContainer,
                                    contentColor = MaterialTheme.colorScheme.onBackground
                                )
                            },
                            indicatorPadding = PaddingValues(paddingValues.calculateTopPadding())
                        ) {
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
                                    percentChange24h = state.coin.percentChange24h,
                                    percentChange7d = state.coin.percentChange7d,
                                    percentChange30d = state.coin.percentChange30d,
                                    percentChange1y = state.coin.percentChange1y,
                                    periodFilter = state.periodFilter
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                PeriodFilterRow(
                                    periodFilter = state.periodFilter,
                                    onFilterClick = { periodFilter ->
                                        onEvent(CoinEvent.ChoosePeriodFilter(periodFilter))
                                    }
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                PriceChart(
                                    status = state.chartStatus,
                                    ticks = state.ticks,
                                    onRefreshClick = { onEvent(CoinEvent.RefreshChart) }
                                )
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
                                Spacer(modifier = Modifier.height(12.dp))
                                LastUpdateText(
                                    lastUpdated = state.coin.lastUpdated
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
