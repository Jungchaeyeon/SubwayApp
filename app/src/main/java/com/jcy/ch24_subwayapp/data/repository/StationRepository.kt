package com.jcy.ch24_subwayapp.data.repository

import com.jcy.ch24_subwayapp.domain.ArrivalInformation
import com.jcy.ch24_subwayapp.domain.Station
import kotlinx.coroutines.flow.Flow

interface StationRepository {
    val stations: Flow<List<Station>>

    suspend fun refreshStations()

    suspend fun getStationArrivals(stationName: String): List<ArrivalInformation>

    suspend fun updateStation(station: Station)
}