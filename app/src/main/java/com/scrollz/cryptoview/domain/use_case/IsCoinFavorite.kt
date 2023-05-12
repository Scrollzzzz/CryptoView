package com.scrollz.cryptoview.domain.use_case

import com.scrollz.cryptoview.domain.repository.CryptoViewRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class IsCoinFavorite @Inject constructor(
    private val repository: CryptoViewRepository
) {
    operator fun invoke(id: String): Flow<Boolean> {
        return repository.isCoinFavorite(id)
    }
}
