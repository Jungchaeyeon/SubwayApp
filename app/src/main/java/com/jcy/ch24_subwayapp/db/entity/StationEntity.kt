package com.jcy.ch24_subwayapp.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class StationEntity (
    @PrimaryKey val stationName: String,
    val isFavorited: Boolean = false
        )