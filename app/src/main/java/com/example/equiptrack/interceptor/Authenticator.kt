package com.example.equiptrack.interceptor

import com.example.equiptrack.data.dto.RefreshRequest
import com.example.equiptrack.data.reposutory.JwtTokenManager
import com.example.equiptrack.service.AuthService
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import javax.inject.Inject

class AuthAuthenticator @Inject constructor(
    private val tokenManager: JwtTokenManager,
    private val refreshTokenService: AuthService
) : Authenticator {
    companion object {
        const val HEADER_AUTHORIZATION = "Authorization"
    }
    override fun authenticate(route: Route?, response: Response): Request? {
        var token = runBlocking {
            tokenManager.getRefreshJwt()
        }
        val id = runBlocking {
            tokenManager.getUserId()
        }
        if (token == null || id == null) {
            return null
        }
        val newToken = synchronized(this) {
            val newSessionResponse = runBlocking { refreshTokenService.refreshToken(RefreshRequest(token, id)) }
            if (newSessionResponse.isSuccessful) {
                newSessionResponse.body()?.let { body ->
                    runBlocking {
                        tokenManager.saveUserId(body.user.id)
                        tokenManager.saveRefreshJwt(body.refreshToken)
                        tokenManager.saveAccessJwt(body.accessToken)
                    }
                    body.accessToken
                }
            } else {
                null
            }

        }
        return if (newToken != null && newToken != "") response.request.newBuilder()
            .header(HEADER_AUTHORIZATION, newToken)
            .build()
        else null
//        val currentToken = runBlocking {
//            tokenManager.getAccessJwt()
//        }
//        synchronized(this) {
//            val updatedToken = runBlocking {
//                tokenManager.getAccessJwt()
//            }
//            val token = if (currentToken != updatedToken) updatedToken else {
//                val refreshToken = tokenManager.getRefreshJwt()
//                val newSessionResponse = runBlocking { refreshTokenService.refreshToken(tokenManager.getRefreshJwt()) }
//                if (newSessionResponse.isSuccessful && newSessionResponse.body() != null) {
//                    newSessionResponse.body()?.let { body ->
//                        runBlocking {
//                            tokenManager.saveAccessJwt(body.accessToken)
//                            tokenManager.saveRefreshJwt(body.refreshToken)
//                        }
//                        body.accessToken
//                    }
//                } else null
//            }
//            return if (token != null) response.request.newBuilder()
//                .header(HEADER_AUTHORIZATION, "$TOKEN_TYPE $token")
//                .build() else null
//        }
    }
}