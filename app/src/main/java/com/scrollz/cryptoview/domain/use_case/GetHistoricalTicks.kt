package com.scrollz.cryptoview.domain.use_case

import com.scrollz.cryptoview.domain.model.HistoricalTicks
import com.scrollz.cryptoview.domain.repository.CryptoViewRepository
import com.scrollz.cryptoview.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetHistoricalTicks @Inject constructor(
    private val repository: CryptoViewRepository
) {
    operator fun invoke(id: String): Flow<Resource<HistoricalTicks>> {
        return repository.getHistoricalTicks(id)
    }
}
