package com.scrollz.cryptoview.domain.use_case

import com.scrollz.cryptoview.domain.model.Coin
import com.scrollz.cryptoview.domain.repository.CryptoViewRepository
import com.scrollz.cryptoview.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCoins @Inject constructor(
    private val repository: CryptoViewRepository
) {
    operator fun invoke(): Flow<Resource<List<Coin>>> {
        return repository.getCoins()
    }
}
