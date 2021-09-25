package com.jcy.ch24_subwayapp.data.repository

import com.jcy.ch24_subwayapp.data.api.StationApi
import com.jcy.ch24_subwayapp.data.api.StationArrivalsApi
import com.jcy.ch24_subwayapp.data.api.response.mapper.toArrivalInformation
import com.jcy.ch24_subwayapp.db.StationDao
import com.jcy.ch24_subwayapp.db.entity.StationSubwayCrossRefEntity
import com.jcy.ch24_subwayapp.db.entity.mapper.toStationEntity
import com.jcy.ch24_subwayapp.db.entity.mapper.toStations
import com.jcy.ch24_subwayapp.domain.ArrivalInformation
import com.jcy.ch24_subwayapp.domain.Station
import com.jcy.ch24_subwayapp.preference.PreferenceManager
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.lang.RuntimeException

class StationRepositoryImpl(
    private val stationArrivalsApi: StationArrivalsApi,
    private val stationApi: StationApi,
    private val stationDao: StationDao,
    private val preferenceManager: PreferenceManager,
    private val dispatcher: CoroutineDispatcher
) : StationRepository{
    override val stations: Flow<List<Station>> =
            stationDao.getStationWithSubways()
                .distinctUntilChanged()
                .map { stations -> stations.toStations().sortedByDescending { it.isFavorited } }
                .flowOn(dispatcher)


    override suspend fun refreshStations() = withContext(dispatcher){
        val fileUpdatedTimeMillis = stationApi.getStationDataUpdatedTimeMillis()
        val lastDatabaseUpdatedTimeMillis = preferenceManager.getLong(KEY_LAST_DATABASE_UPDATED_TIME_MILLIS)

        if(lastDatabaseUpdatedTimeMillis == null || fileUpdatedTimeMillis > lastDatabaseUpdatedTimeMillis){
            val stationSubways = stationApi.getStationSubways()
            stationDao.insertStations(stationSubways.map{it.first})
            stationDao.insertSubways(stationSubways.map { it.second })
            stationDao.insertCrossReferences(
                stationSubways.map { (station,subway)->
                    StationSubwayCrossRefEntity(
                        station.stationName,
                        subway.subwayId
                    )
                }
            )
            preferenceManager.putLong(KEY_LAST_DATABASE_UPDATED_TIME_MILLIS,fileUpdatedTimeMillis)
        }
    }

    override suspend fun getStationArrivals(stationName: String): List<ArrivalInformation> = withContext(dispatcher) {
        stationArrivalsApi.getRealtimeStationArrivals(stationName)
            .body()
            ?.realtimeArrivalList
            ?.toArrivalInformation()
            ?.distinctBy{it.direction}
            ?.sortedBy{it.subway}
            ?: throw RuntimeException("도착 정보를 불러오는 데에 실패했습니다.")
    }

    override suspend fun updateStation(station: Station) = withContext(dispatcher) {
        stationDao.updateStation(station.toStationEntity())
    }

    companion object{
        private const val KEY_LAST_DATABASE_UPDATED_TIME_MILLIS ="KEY_LAST_DATABASE_UPDATED_TIME_MILLIS"
    }
}