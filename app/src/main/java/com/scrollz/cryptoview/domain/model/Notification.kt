package com.scrollz.cryptoview.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notification")

data class Notification(
    @PrimaryKey val coinID: String,
    val hour: Int,
    val minute: Int
)
