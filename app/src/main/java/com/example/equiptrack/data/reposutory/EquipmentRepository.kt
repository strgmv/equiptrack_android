package com.example.equiptrack.data.reposutory

import android.util.Log
import com.example.equiptrack.Constants
import com.example.equiptrack.data.dto.EquipmentCreateRequest
import com.example.equiptrack.data.dto.EquipmentDetailed
import com.example.equiptrack.data.dto.GetEquipmentsResponse
import com.example.equiptrack.data.dto.NetworkResult
import com.example.equiptrack.data.dto.ReservationInfoResponse
import com.example.equiptrack.data.dto.ReservationRequest
import com.example.equiptrack.service.EquipmentService
import kotlinx.datetime.Instant
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EquipmentRepository @Inject constructor(
    private val network: EquipmentService
) {
    suspend fun createEquipment(
        equip: EquipmentCreateRequest
    ): NetworkResult<EquipmentDetailed> {
        return handleApi { network.createEquipment(equip) }
    }

    suspend fun updateEquipment (
        id: String,
        data: EquipmentCreateRequest
    ): Response<ResponseBody> {
        return network.updateEquipment(EquipmentDetailed(
            id = id, name = data.name, shortDescription = data.shortDescription, fullDescription = data.fullDescription
        ))
    }

    suspend fun deleteEquipment(id: String): Response<ResponseBody> = network.deleteEquipment(id)

    suspend fun getEquipments(
        pageSize: Int = Constants.DEFAULT_PAGE_SIZE,
        pageNumber: Int = Constants.DEFAULT_PAGE_NUM,
    ): GetEquipmentsResponse {
        val res = network.getEquipments(
            pageSize,
            pageNumber,
        )
        return res
    }

    suspend fun getUserEquipments(
        userId: String,
        pageSize: Int = Constants.DEFAULT_PAGE_SIZE,
        pageNumber: Int = Constants.DEFAULT_PAGE_NUM,
    ): GetEquipmentsResponse {
        val res = network.getUserEquipments(
            pageSize,
            pageNumber,
            userId
        )
        return res
    }

    suspend fun getEquipmentInfoById(equipmentId: String) : NetworkResult<EquipmentDetailed> {
        return NetworkResult.Success(network.getEquipmentDetailed(equipmentId))
    }

    suspend fun getEquipmentReservationInfo(equipmentId: String) : NetworkResult<ReservationInfoResponse> {
//        return NetworkResult.Success(network.getReservationInfo(equipmentId))
        return handleApi { network.getReservationInfo(equipmentId) }
    }

    suspend fun reserveEquipment(equipmentId: String, start: Instant, end: Instant) : Response<ResponseBody> {
        return network.reserveEquipment(ReservationRequest(
            equipmentId,
            start,
            end
        ))
    }
}
