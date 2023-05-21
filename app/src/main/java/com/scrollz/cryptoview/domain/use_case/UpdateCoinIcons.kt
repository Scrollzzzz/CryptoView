package com.scrollz.cryptoview.domain.use_case

import android.content.SharedPreferences
import com.scrollz.cryptoview.domain.model.Coin
import com.scrollz.cryptoview.domain.repository.CryptoViewRepository
import com.scrollz.cryptoview.utils.shouldUpdateIcons
import javax.inject.Inject


class UpdateCoinIcons @Inject constructor(
    private val repository: CryptoViewRepository,
    private val sharedPreferences: SharedPreferences
) {
    suspend operator fun invoke(coins: List<Coin>) {
        val lastUpdate = sharedPreferences.getString("icons_last_update", "NULL") ?: "NULL"
        val iconsAreNulls = repository.getCoinIconsURLs().all { it == null }
        val currentTime = repository.getCurrentTime().plus('Z')
        if (lastUpdate == "NULL" || lastUpdate.shouldUpdateIcons(currentTime) || iconsAreNulls) {
            repository.updateCoinIcons(coins)
            sharedPreferences.edit().putString("icons_last_update", currentTime).apply()
        }
    }
}
