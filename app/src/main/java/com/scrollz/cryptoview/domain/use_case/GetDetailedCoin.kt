package com.scrollz.cryptoview.domain.use_case

import com.scrollz.cryptoview.domain.model.DetailedCoin
import com.scrollz.cryptoview.domain.repository.CryptoViewRepository
import com.scrollz.cryptoview.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDetailedCoin @Inject constructor(
    private val repository: CryptoViewRepository
) {
    operator fun invoke(id: String): Flow<Resource<DetailedCoin?>> {
        return repository.getDetailedCoin(id)
    }
}
