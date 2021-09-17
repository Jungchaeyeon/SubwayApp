package com.jcy.ch24_subwayapp.db.entity

import androidx.room.Entity

@Entity(primaryKeys = ["stationName","subwayId"])
data class StationSubwayCrossRefEntity(
    val stationName: String,
    val subwayId: Int
)
