package com.scrollz.cryptoview.domain.use_case

import com.scrollz.cryptoview.domain.repository.CryptoViewRepository
import com.scrollz.cryptoview.notification.AlarmScheduler
import javax.inject.Inject

class DisableNotification @Inject constructor(
    private val repository: CryptoViewRepository,
    private val alarmScheduler: AlarmScheduler
) {
    suspend operator fun invoke(id: String) {
        repository.deleteNotification(id)
        alarmScheduler.cancelAlarm(id)
    }
}
