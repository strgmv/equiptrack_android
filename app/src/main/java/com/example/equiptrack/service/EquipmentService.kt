package com.example.equiptrack.service

import com.example.equiptrack.data.dto.EquipmentCreateRequest
import com.example.equiptrack.data.dto.EquipmentDetailed
import com.example.equiptrack.data.dto.GetEquipmentsResponse
import com.example.equiptrack.data.dto.ReservationInfoResponse
import com.example.equiptrack.data.dto.ReservationRequest
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface EquipmentService {
    @GET("equipment/{id}")
    suspend fun getEquipmentDetailed(@Path("id") equipmentId: String): EquipmentDetailed

    @GET("equipment")
    suspend fun getEquipments(
        @Query("size") pageSize: Int,
        @Query("page") pageNumber: Int,
    ): GetEquipmentsResponse

    @GET("equipment")
    suspend fun getUserEquipments(
        @Query("size") pageSize: Int,
        @Query("page") pageNumber: Int,
        @Query("user_id") userId: String
    ): GetEquipmentsResponse

    @GET("equipment/reservations_info/{id}")
    suspend fun getReservationInfo(
        @Path("id") equipmentId: String
    ): Response<ReservationInfoResponse>

    @POST("equipment/create")
    suspend fun  createEquipment(@Body equipment: EquipmentCreateRequest): Response<EquipmentDetailed>

    @DELETE("equipment/{id}")
    suspend fun  deleteEquipment(@Path("id") equipmentId: String): Response<ResponseBody>

    @POST("equipment/reserve")
    suspend fun reserveEquipment(@Body info: ReservationRequest): Response<ResponseBody>

    @PUT("equipment/update")
    suspend fun updateEquipment(@Body info: EquipmentDetailed): Response<ResponseBody>
}