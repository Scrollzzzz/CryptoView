package com.scrollz.cryptoview.presentation.coinsScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.scrollz.cryptoview.domain.use_case.UseCases
import com.scrollz.cryptoview.presentation.common.Status
import com.scrollz.cryptoview.utils.Resource
import com.scrollz.cryptoview.utils.contains
import com.scrollz.cryptoview.utils.toCoinView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import javax.inject.Inject

@HiltViewModel
class CoinsViewModel @Inject constructor(
    private val useCases: UseCases
):ViewModel() {

    private val _filterSearchState = MutableStateFlow(FilterSearchState())

    private val _coinsState = MutableStateFlow(CoinsState())
    val coinsState = _coinsState.asStateFlow()

    init {
        getCoins()
    }

    fun onEvent(event: CoinsEvent) {
        when(event) {
            is CoinsEvent.ChooseFilter -> {
                _coinsState.value = _coinsState.value.copy(
                    filter = event.filter
                )
                _filterSearchState.value = _filterSearchState.value.copy(
                    filter = event.filter
                )
            }
            is CoinsEvent.ChangeSearchText -> {
                _coinsState.value = _coinsState.value.copy(
                    searchText = event.text
                )
                _filterSearchState.value = _filterSearchState.value.copy(
                    searchText = event.text
                )
            }
            is CoinsEvent.ToggleSearch -> {
                _coinsState.value = _coinsState.value.copy(
                    isSearching = !_coinsState.value.isSearching
                )
            }
            is CoinsEvent.Refresh -> {
                getCoins()
            }
        }
    }

    private fun getCoins() {
        useCases.getCoins().combine(useCases.getFavorites()) { result, favorites -> result to favorites }
            .combine(_filterSearchState) { (result, favorites), filterSearch ->
            when (result) {
                is Resource.Success -> {
                    val coinsList = result.data?.map { it.toCoinView() } ?: emptyList()
                    if (coinsList.isEmpty()) {
                        _coinsState.value = _coinsState.value.copy(
                            status = Status.Error,
                        )
                    }
                    else {
                        _coinsState.value = _coinsState.value.copy(
                            status = Status.Normal,
                            coinsList = when(filterSearch.filter) {
                                is Filter.All -> coinsList
                                is Filter.Favorites -> coinsList.filter { it.id in favorites }
                            }.let { coins ->
                                if (filterSearch.searchText.isBlank()) { coins } else {
                                    coins.filter { it.contains(filterSearch.searchText) }
                                }
                            }
                        )
                    }
                }
                is Resource.Loading -> {
                    _coinsState.value = _coinsState.value.copy(
                        status = Status.Loading
                    )
                }
                is Resource.Error -> {
                    val coinsList = result.data?.map { it.toCoinView() } ?: emptyList()
                    if (coinsList.isEmpty()) {
                        _coinsState.value = _coinsState.value.copy(
                            status = Status.Error,
                        )
                    }
                    else {
                        _coinsState.value = _coinsState.value.copy(
                            status = Status.Normal,
                            coinsList = when(filterSearch.filter) {
                                is Filter.All -> coinsList
                                is Filter.Favorites -> coinsList.filter { it.id in favorites }
                            }.let { coins ->
                                if (filterSearch.searchText.isBlank()) { coins } else {
                                    coins.filter { it.contains(filterSearch.searchText) }
                                }
                            }
                        )
                    }
                }
            }
        }.launchIn(viewModelScope) + Dispatchers.IO
    }

}
