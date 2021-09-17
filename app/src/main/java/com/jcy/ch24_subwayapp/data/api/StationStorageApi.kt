package com.jcy.ch24_subwayapp.data.api

import com.google.firebase.storage.FirebaseStorage
import com.jcy.ch24_subwayapp.db.entity.StationEntity
import com.jcy.ch24_subwayapp.db.entity.SubwayEntity
import kotlinx.coroutines.tasks.await

class StationStorageApi(
    firebaseStorage: FirebaseStorage
) : StationApi{

    private val sheetReference = firebaseStorage.reference.child(STATION_DATA_FILE_NAME)
    override suspend fun getStationDataUpdatedTimeMillis(): Long =
        sheetReference.metadata.await().updatedTimeMillis
    /**
     coroutine을 쓰는 이유 중 하나는 리스너들을 해소시키기 위한것.
     await()를 통해 task가 완료 되면 task 반환
     **/

    override suspend fun getStationSubways(): List<Pair<StationEntity, SubwayEntity>> {
        val downloadSizeBytes = sheetReference.metadata.await().sizeBytes
        val byteArray = sheetReference.getBytes(downloadSizeBytes).await()

        return byteArray.decodeToString()
            .lines()  // 1002, 건대입구
            .drop(1)  //헤더 영역 날리기
            .map { it.split(",") }
            .map { StationEntity(it[1]) to SubwayEntity(it[0].toInt()) }
    }
    companion object{
        private const val STATION_DATA_FILE_NAME="station_data.csv"
    }
}