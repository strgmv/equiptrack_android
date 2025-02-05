package com.example.equiptrack.service

import com.example.equiptrack.data.dto.Auth
import com.example.equiptrack.data.dto.LoginResponse
import com.example.equiptrack.data.dto.LogoutRequest
import com.example.equiptrack.data.dto.RefreshRequest
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthService {
    @POST("auth/login")
    suspend fun login(
        @Body auth: Auth,
    ): LoginResponse

    @POST("auth/refresh")
    suspend fun refreshToken(
        @Body req: RefreshRequest,
    ): Response<LoginResponse>

    @POST("auth/logout")
    suspend fun logout(
        @Body req: LogoutRequest
    )
}

interface AuthStatusService {
    @GET("auth/status")
    suspend fun checkAuthStatus(): Response<ResponseBody>
}