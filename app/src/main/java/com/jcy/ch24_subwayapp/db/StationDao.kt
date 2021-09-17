package com.jcy.ch24_subwayapp.db

import androidx.room.*
import com.jcy.ch24_subwayapp.db.entity.StationEntity
import com.jcy.ch24_subwayapp.db.entity.StationSubwayCrossRefEntity
import com.jcy.ch24_subwayapp.db.entity.StationWithSubwaysEntity
import com.jcy.ch24_subwayapp.db.entity.SubwayEntity
import kotlinx.coroutines.flow.Flow


@Dao
interface StationDao {

    @Transaction
    @Query("SELECT * FROM StationEntity")
    fun getStationWithSubways(): Flow<List<StationWithSubwaysEntity>>
    /**return 값을 Flow로 감싸는 것은 List<StationWithSubwaysEntity>를 Observable한 형태로 만들겠다라는 것**/

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStations(station: List<StationEntity>) //지하철 역 목록 저장

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubways(subways: List<SubwayEntity>) // 노선들 저장

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCrossReferences(reference: List<StationSubwayCrossRefEntity>) //역과 노선의 상관관계

    /**
     * Insert, Update, Delete와 같은 것들은 Transaction이 내부적으로 처리된다.
     * 때문에 @Query를 사용할 때는 수동으로 @Transaction을 붙여줘야 한다.
     **/
}