package com.jcy.ch24_subwayapp.data.api

import com.jcy.ch24_subwayapp.db.entity.StationEntity
import com.jcy.ch24_subwayapp.db.entity.SubwayEntity

interface StationApi {
    suspend fun getStationDataUpdatedTimeMillis(): Long

    suspend fun getStationSubways(): List<Pair<StationEntity, SubwayEntity>>
}