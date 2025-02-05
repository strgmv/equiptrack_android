package com.example.equiptrack.data.dto

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EquipmentDetailed(
    @SerialName("equipment_id")
    val id: String,
    @SerialName("name")
    val name: String,
    @SerialName("short_description")
    val shortDescription: String,
    @SerialName("full_description")
    val fullDescription: String,
)

@Serializable
data class EquipmentShort(
    @SerialName("equipment_id")
    val id: String,
    @SerialName("name")
    val name: String,
    @SerialName("short_description")
    val shortDescription: String,
    @SerialName("reserved")
    val reserved: Boolean?
)

@Serializable
data class EquipmentCreateRequest(
    @SerialName("name")
    val name: String,
    @SerialName("short_description")
    val shortDescription: String,
    @SerialName("full_description")
    val fullDescription: String,
)

@Serializable
data class EquipmentList(
    @SerialName("equipments")
    val equipments: List<EquipmentShort>
)

@Serializable
data class GetEquipmentsResponse(
    @SerialName("total_count")
    val totalCount: Int,
    @SerialName("total_pages")
    val totalPages: Int,
    @SerialName("page")
    val page: Int,
    @SerialName("size")
    val size: Int,
    @SerialName("has_more")
    val hasMore: Boolean,
    @SerialName("equipments")
    val equipments: List<EquipmentShort>
)

@Serializable
data class ReservationInfo (
    @SerialName("reservation_start")
    val reservationStart: Instant,
    @SerialName("reservation_end")
    val reservationEnd: Instant,
)

@Serializable
data class ReservationInfoResponse (
    @SerialName("amount")
    val totalCount: Int,
    @SerialName("data")
    val data: List<ReservationInfo>,
)

@Serializable
data class ReservationRequest (
    @SerialName("equipment_id")
    val equipmentId: String,
    @SerialName("reservation_start")
    val reservationStart: Instant,
    @SerialName("reservation_end")
    val reservationEnd: Instant,
)