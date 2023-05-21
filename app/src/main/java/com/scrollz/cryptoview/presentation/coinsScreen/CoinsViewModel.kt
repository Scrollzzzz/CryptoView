package com.scrollz.cryptoview.presentation.coinsScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.scrollz.cryptoview.domain.model.Coin
import com.scrollz.cryptoview.domain.use_case.UseCases
import com.scrollz.cryptoview.presentation.common.Status
import com.scrollz.cryptoview.utils.Resource
import com.scrollz.cryptoview.utils.contains
import com.scrollz.cryptoview.utils.toCoinView
import com.scrollz.cryptoview.utils.toLocalDateTime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import javax.inject.Inject

@HiltViewModel
class CoinsViewModel @Inject constructor(
    private val useCases: UseCases
):ViewModel() {

    private val _coinsState = MutableStateFlow(CoinsState())
    val coinsState = _coinsState.asStateFlow()

    private val _coins = MutableStateFlow<Resource<List<Coin>>>(Resource.Loading(emptyList()))
    private val _favorites = MutableStateFlow<List<String>>(emptyList())
    private val _filter = MutableStateFlow<Filter>(Filter.All)
    private val _searchText = MutableStateFlow("")

    private var getCoinsJob: Job? = null
    private var updateIconsJob: Job? = null
    private var iconsUpdated: Boolean = false

    init {
        getCoins()
        getFavorites()
        mergeFlows()
    }

    fun onEvent(event: CoinsEvent) {
        when(event) {
            is CoinsEvent.ChooseFilter -> {
                _filter.value = event.filter
            }
            is CoinsEvent.ChangeSearchText -> {
                _searchText.value = event.text
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
        iconsUpdated = false
        getCoinsJob?.cancel()
        updateIconsJob?.cancel()
        getCoinsJob = useCases.getCoins().onEach { result ->
            if (!iconsUpdated && result is Resource.Success) {
                result.data?.let { coins ->
                    viewModelScope.launch(Dispatchers.IO) {
                        useCases.updateCoinIcons(coins)
                    }
                    iconsUpdated = true
                }
            }
            _coins.value = result
        }.launchIn(viewModelScope + Dispatchers.IO)
    }

    private fun getFavorites() {
        useCases.getFavorites().onEach { result ->
            _favorites.value = result
        }.launchIn(viewModelScope + Dispatchers.IO)
    }

    private fun mergeFlows() {
        combine(
            _coins,
            _favorites,
            _filter,
            _searchText,
        ) { resource, favorites, filter, searchText ->
            val coinsList = resource.data?.map { it.toCoinView() } ?: emptyList()
            if (coinsList.isEmpty()) {
                _coinsState.value.copy(
                    status = if (resource is Resource.Loading) Status.Loading else Status.Error,
                    filter = filter,
                    searchText = searchText
                )
            }
            else {
                _coinsState.value.copy(
                    status = when(resource) {
                        is Resource.Success -> Status.Normal
                        is Resource.Loading -> Status.Loading
                        is Resource.Error -> Status.Error
                    },
                    coinsList = when(filter) {
                        is Filter.All -> coinsList
                        is Filter.Favorites -> coinsList.filter { it.id in favorites }
                    }.let { coins ->
                        if (searchText.isBlank()) { coins } else {
                            coins.filter { it.contains(searchText) }
                        }
                    },
                    filter = filter,
                    searchText = searchText,
                    lastUpdated = coinsList.first().lastUpdated.toLocalDateTime()
                )
            }
        }.onEach { newCoinsState ->
            _coinsState.value = newCoinsState
        }.launchIn(viewModelScope)
    }

}
