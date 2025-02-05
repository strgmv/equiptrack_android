package com.example.equiptrack.data.reposutory

import com.example.equiptrack.data.dto.Auth
import com.example.equiptrack.data.dto.LoginResponse
import com.example.equiptrack.data.dto.LogoutRequest
import com.example.equiptrack.service.AuthService
import com.example.equiptrack.service.AuthStatusService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val network: AuthService,
    private val statusService: AuthStatusService
) {
    suspend fun login(
        login: String,
        password: String
    ): LoginResponse = network.login(Auth(login, password))

    suspend fun isAuthorized() : Boolean {
        return when (statusService.checkAuthStatus().code()) {
            200 -> true
            else -> false
        }
    }

    suspend fun logout(id: String, token: String) {
        network.logout(LogoutRequest(id, token))
    }
}