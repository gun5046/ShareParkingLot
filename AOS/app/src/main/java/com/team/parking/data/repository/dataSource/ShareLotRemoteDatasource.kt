package com.team.parking.data.repository.dataSource

import com.team.parking.data.model.day.DayRequest
import com.team.parking.data.model.parkinglot.ParkingLotResponse
import com.team.parking.data.model.parkinglot.ShareLotRequest
import com.team.parking.data.model.parkinglot.ShareLotResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface ShareLotRemoteDatasource {
    suspend fun deleteShareLot(parkId: Long) : Response<Unit>
    suspend fun getShareLotDetail(parkId: Long) : Response<ParkingLotResponse>
    suspend fun getShareLotList(userId: Long) : Response<List<ShareLotResponse>>
    suspend fun postShareLot(
        saveDto: ShareLotRequest,
        files: List<MultipartBody.Part>
    ) : Response<Long>
    suspend fun postSaveDay(
        daySaveDtos: DayRequest,
        parkId: Long
    ) : Response<Unit>
    suspend fun postUpdateDay(
        daySaveDtos: DayRequest,
        parkId: Long
    ) : Response<Unit>
}