package com.scrollz.cryptoview.presentation.coinScreen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.scrollz.cryptoview.domain.model.HistoricalTicks
import com.scrollz.cryptoview.domain.model.Notification
import com.scrollz.cryptoview.domain.model.Tick
import com.scrollz.cryptoview.domain.use_case.UseCases
import com.scrollz.cryptoview.presentation.common.Status
import com.scrollz.cryptoview.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import javax.inject.Inject

@HiltViewModel
class CoinViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val useCases: UseCases
): ViewModel() {

    private val _coinState = MutableStateFlow(CoinState())
    val coinState = _coinState.asStateFlow()

    private val _ticks = MutableStateFlow<Resource<HistoricalTicks>>(Resource.Loading(null))
    private val _periodFilter = MutableStateFlow<PeriodFilter>(PeriodFilter.Day)

    init {
        savedStateHandle.get<String>("coinID")?.let { id ->
            getCoinInfo(id)
            getHistoricalTicks(id)
            filterTicks()
        }
    }

    fun onEvent(event: CoinEvent) {
        when(event) {
            is CoinEvent.ToggleFavorite -> {
                toggleFavorite()
            }
            is CoinEvent.ChoosePeriodFilter -> {
                _periodFilter.value = event.periodFilter
            }
            is CoinEvent.EnableNotification -> {
                _coinState.value.coin?.id?.let { id ->
                    viewModelScope.launch {
                        useCases.enableNotification(
                            Notification(id, event.hour, event.minute)
                        )
                    }
                }
            }
            is CoinEvent.DisableNotification -> {
                _coinState.value.coin?.id?.let { id ->
                    viewModelScope.launch {
                        useCases.disableNotification(id)
                    }
                }
            }
            is CoinEvent.ShowNotificationDialog -> {
                _coinState.value = _coinState.value.copy(isNotificationDialogVisible = true)
            }
            is CoinEvent.CloseNotificationDialog -> {
                _coinState.value = _coinState.value.copy(isNotificationDialogVisible = false)
            }
        }
    }

    private fun getCoinInfo(id: String) {
        viewModelScope.launch {
            useCases.isCoinFavorite(id).onEach { isFavorite ->
                _coinState.value = _coinState.value.copy(isFavorite = isFavorite)
            }.launchIn(viewModelScope + Dispatchers.IO)
            useCases.getNotification(id).onEach { notification ->
                _coinState.value = _coinState.value.copy(notification = notification)
            }.launchIn(viewModelScope + Dispatchers.IO)
        }.invokeOnCompletion { cause ->
            when(cause) {
                null -> getCoin(id)
                is CancellationException -> {  }
                else -> _coinState.value = _coinState.value.copy(status = Status.Error)
            }
        }
    }

    private fun getCoin(id: String) {
        useCases.getDetailedCoin(id).onEach{ resource ->
            when(resource) {
                is Resource.Success -> {
                    _coinState.value = _coinState.value.copy(
                        status = Status.Normal,
                        coin = resource.data,
                    )
                }
                is Resource.Loading -> {
                    _coinState.value = _coinState.value.copy(
                        status = Status.Loading,
                    )
                }
                is Resource.Error -> {
                    if (resource.data == null) {
                        _coinState.value = _coinState.value.copy(
                            status = Status.Error,
                        )
                    }
                    else {
                        _coinState.value = _coinState.value.copy(
                            status = Status.Normal,
                            coin = resource.data,
                        )
                    }
                }
            }
        }.launchIn(viewModelScope + Dispatchers.IO)
    }

    private fun getHistoricalTicks(id: String) {
        useCases.getHistoricalTicks(id).onEach { ticks ->
            _ticks.value = ticks
        }.launchIn(viewModelScope + Dispatchers.IO)
    }

    private fun filterTicks() {
        combine(
            _ticks,
            _periodFilter
        ) { resource, periodFilter ->
            when(resource) {
                is Resource.Success -> {
                    val ticks = ticksOverPeriod(resource.data, periodFilter)
                    if (ticks.size < 7) {
                        _coinState.value.copy(chartStatus = Status.Error)
                    } else {
                        _coinState.value.copy(
                            chartStatus = Status.Normal,
                            periodFilter = periodFilter,
                            ticks = ticks
                        )
                    }
                }
                is Resource.Loading -> {
                    _coinState.value.copy(
                        chartStatus = Status.Loading
                    )
                }
                is Resource.Error -> {
                    val ticks = ticksOverPeriod(resource.data, periodFilter = periodFilter)
                    if (ticks.size < 6) {
                        _coinState.value.copy(chartStatus = Status.Error)
                    } else {
                        _coinState.value.copy(
                            chartStatus = Status.Normal,
                            periodFilter = periodFilter,
                            ticks = ticks
                        )
                    }
                }
            }
        }.onEach { newCoinState ->
            _coinState.value = newCoinState
        }.launchIn(viewModelScope)
    }

    private fun ticksOverPeriod(ticks: HistoricalTicks?, periodFilter: PeriodFilter): List<Tick> {
        return if (ticks == null) {
            emptyList()
        } else {
            when(periodFilter) {
                is PeriodFilter.Day -> ticks.day
                is PeriodFilter.Week -> {
                    val startTick = Instant
                        .parse(ticks.year.maxOf { it.timestamp })
                        .minus(7, DateTimeUnit.DAY, TimeZone.UTC)
                    ticks.year.filter { tick ->
                        Instant.parse(tick.timestamp) > startTick
                    }
                }
                is PeriodFilter.Month -> {
                    val startTick = Instant
                        .parse(ticks.year.maxOf { it.timestamp })
                        .minus(DateTimeUnit.MONTH, TimeZone.UTC)
                    ticks.year.filter { tick ->
                        Instant.parse(tick.timestamp) > startTick
                    }
                }
                is PeriodFilter.Year -> ticks.year
            }
        }
    }

    private fun toggleFavorite() {
        _coinState.value.coin?.id?.let { id ->
            viewModelScope.launch(Dispatchers.IO) {
                useCases.toggleFavorite(id)
            }
        }
    }

}
