package com.scrollz.cryptoview.domain.use_case

import com.scrollz.cryptoview.domain.repository.CryptoViewRepository
import javax.inject.Inject

class ToggleFavorite @Inject constructor(
    private val repository: CryptoViewRepository
) {
    suspend operator fun invoke(id: String) {
        repository.toggleFavorite(id)
    }
}
