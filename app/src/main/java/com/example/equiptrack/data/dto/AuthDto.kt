package com.example.equiptrack.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Auth(
    @SerialName("login")
    val login: String,
    @SerialName("password")
    val password: String,
)

@Serializable
data class RefreshRequest (
    @SerialName("refresh_token")
    val token: String,
    @SerialName("user_id")
    val id: String,
)

@Serializable
data class LoginResponse(
    @SerialName("user")
    val user: User,
    @SerialName("access_token")
    val accessToken: String,
    @SerialName("refresh_token")
    val refreshToken: String
)

@Serializable
data class LogoutRequest(
    @SerialName("user_id")
    val id: String,
    @SerialName("refresh_token")
    val refreshToken: String
)