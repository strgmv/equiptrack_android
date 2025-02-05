package com.example.equiptrack.data.dto

import androidx.annotation.EmptySuper
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User (
    @SerialName("login")
    val login: String,
    @SerialName("user_id")
    val id: String,
    @SerialName("role")
    val role: String
)

@Serializable
data class GetUsersResponse(
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
    @SerialName("users")
    val users: List<User>
)

@Serializable
data class UserCreate (
    @SerialName("login")
    val login: String="",
    @SerialName("password")
    val password: String="",
    @SerialName("role")
    val role: String=""
)