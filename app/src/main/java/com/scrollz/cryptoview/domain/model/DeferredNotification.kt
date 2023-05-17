package com.scrollz.cryptoview.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "deferred_notification")

data class DeferredNotification(
    @PrimaryKey val coinID: String
)
