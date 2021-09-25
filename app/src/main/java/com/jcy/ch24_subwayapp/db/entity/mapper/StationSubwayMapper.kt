package com.jcy.ch24_subwayapp.db.entity.mapper

import com.jcy.ch24_subwayapp.db.entity.StationEntity
import com.jcy.ch24_subwayapp.db.entity.StationWithSubwaysEntity
import com.jcy.ch24_subwayapp.db.entity.SubwayEntity
import com.jcy.ch24_subwayapp.domain.Station
import com.jcy.ch24_subwayapp.domain.Subway

fun StationWithSubwaysEntity.toStation() = Station(
    name = station.stationName,
    isFavorited = station.isFavorited,
    connectedSubways = subways.toSubways()
)
fun Station.toStationEntity() =
    StationEntity(
        stationName = name,
        isFavorited = isFavorited,
    )

fun List<StationWithSubwaysEntity>.toStations() = map { it.toStation() }

fun List<SubwayEntity>.toSubways(): List<Subway> = map { Subway.findById(it.subwayId) }