package com.example.equiptrack.viewmodels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.example.equiptrack.common.AuthEvent
import com.example.equiptrack.data.reposutory.AuthRepository
import com.example.equiptrack.data.reposutory.JwtTokenManager
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserState @Inject constructor (
    private var authRepository: AuthRepository,
    private var tokenManager: JwtTokenManager,
) {
    val _IdState = mutableStateOf("")
    val IdState: State<String> = _IdState

    private val _RoleState = mutableStateOf("")
    val RoleState: State<String> = _RoleState

    fun setId(value:String){
        _IdState.value = value
    }

    fun setRole(value:String){
        _RoleState.value = value
    }

    init {
        runBlocking {
            try {
                _IdState.value = tokenManager.getUserId() ?: ""
                _RoleState.value = tokenManager.getUserRole() ?: ""
                if (_IdState.value == "" || !authRepository.isAuthorized()) {
                    tokenManager.clearAllTokens()
                    _IdState.value = ""
                    _RoleState.value = tokenManager.getUserRole() ?: ""
                }
            } catch (_: Exception) {}
        }
    }

    fun logout() {
        _IdState.value = ""
        _RoleState.value = ""
    }
}