package com.scrollz.cryptoview.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_coin")

data class FavoriteCoin(
    @PrimaryKey val id: String
)
