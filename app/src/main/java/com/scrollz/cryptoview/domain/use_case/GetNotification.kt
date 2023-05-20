package com.scrollz.cryptoview.domain.use_case

import com.scrollz.cryptoview.domain.model.Notification
import com.scrollz.cryptoview.domain.repository.CryptoViewRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetNotification @Inject constructor(
    private val repository: CryptoViewRepository
) {
    operator fun invoke(coinID: String): Flow<Notification?> {
        return repository.getNotification(coinID)
    }
}
