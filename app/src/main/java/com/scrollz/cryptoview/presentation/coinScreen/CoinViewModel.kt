package com.scrollz.cryptoview.presentation.coinScreen

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.scrollz.cryptoview.domain.use_case.UseCases
import com.scrollz.cryptoview.presentation.common.Status
import com.scrollz.cryptoview.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch
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

    private val _periodFilterState = MutableStateFlow<PeriodFilter>(PeriodFilter.Day)

    private val _coinState = MutableStateFlow(CoinState())
    val coinState = _coinState.asStateFlow()

    init {
        savedStateHandle.get<String>("coinID")?.let { id ->
            getCoin(id)
            getHistoricalTicks(id)
        }
    }

    fun onEvent(event: CoinEvent) {
        when(event) {
            is CoinEvent.ToggleFavorite -> {
                toggleFavorite()
            }
        }
    }

    private fun getCoin(id: String) {
        useCases.getDetailedCoin(id).combine(useCases.isCoinFavorite(id)) { result, isFavorite ->
            when(result) {
                is Resource.Success -> {
                    _coinState.value = _coinState.value.copy(
                        status = Status.Normal,
                        coin = result.data,
                        isFavorite = isFavorite
                    )
                }
                is Resource.Loading -> {
                    _coinState.value = _coinState.value.copy(
                        status = Status.Loading,
                    )
                }
                is Resource.Error -> {
                    if (result.data == null) {
                        _coinState.value = _coinState.value.copy(
                            status = Status.Error,
                        )
                    }
                    else {
                        _coinState.value = _coinState.value.copy(
                            status = Status.Normal,
                            coin = result.data,
                            isFavorite = isFavorite
                        )
                    }
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun getHistoricalTicks(id: String) {
        useCases.getHistoricalTicks(id).combine(_periodFilterState) { result, period ->
            when(result) {
                is Resource.Success -> {
                    result.data?.let { ticks ->
                        _coinState.value = _coinState.value.copy(
                            ticks = when(period) {
                                is PeriodFilter.Day -> ticks.day
                                is PeriodFilter.Week -> {
                                    val startTick = Instant
                                        .parse(ticks.year.maxOf { it.timestamp })
                                        .minus(7, DateTimeUnit.DAY, TimeZone.UTC)
                                    val t = ticks.year.filter { tick ->
                                        Instant.parse(tick.timestamp) > startTick
                                    }
                                    Log.e("chartV",  "${t.size}")
                                    t
                                }
                                is PeriodFilter.Month -> {
                                    val startTick = Instant
                                        .parse(ticks.year.maxOf { it.timestamp })
                                        .minus(DateTimeUnit.MONTH, TimeZone.UTC)
                                    val t = ticks.year.filter { tick ->
                                        Instant.parse(tick.timestamp) > startTick
                                    }
                                    Log.e("chartV",  "${t.size}")
                                    t
                                }
                                is PeriodFilter.Year -> ticks.year
                            }
                        )
                    }
                    Log.e("chart", "suc ${result.data}")
                }
                is Resource.Loading -> {
                    Log.e("chart", "load")
                }
                is Resource.Error -> {
                    result.data?.let { ticks ->
                        _coinState.value = _coinState.value.copy(
                            ticks = when(period) {
                                is PeriodFilter.Day -> ticks.day
                                is PeriodFilter.Week -> {
                                    val startTick = Instant
                                        .parse(ticks.year.maxOf { it.timestamp })
                                        .minus(7, DateTimeUnit.DAY, TimeZone.UTC)
                                    val t = ticks.year.filter { tick ->
                                        Instant.parse(tick.timestamp) > startTick
                                    }
                                    Log.e("chartV",  "$t")
                                    t
                                }
                                is PeriodFilter.Month -> {
                                    val startTick = Instant
                                        .parse(ticks.year.maxOf { it.timestamp })
                                        .minus(DateTimeUnit.MONTH, TimeZone.UTC)
                                    val t = ticks.year.filter { tick ->
                                        Instant.parse(tick.timestamp) > startTick
                                    }
                                    Log.e("chartV",  "$t")
                                    t
                                }
                                is PeriodFilter.Year -> ticks.year
                            }
                        )
                    }
                    Log.e("chart", "err ${result.error?.message}")
                }
            }
        }.launchIn(viewModelScope)
    }

    private fun toggleFavorite() {
        _coinState.value.coin?.id?.let { id ->
            viewModelScope.launch {
                useCases.toggleFavorite(id)
            }
        }
    }

}
