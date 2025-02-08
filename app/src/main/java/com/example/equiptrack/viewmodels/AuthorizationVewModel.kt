package com.example.equiptrack.viewmodels

import android.content.res.Resources
import android.util.Log
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.equiptrack.R
import com.example.equiptrack.common.AuthEvent
import com.example.equiptrack.data.reposutory.AuthRepository
import com.example.equiptrack.data.reposutory.JwtTokenManager
import com.example.equiptrack.ui.base.Route
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class AuthorizationVewModel
@Inject constructor(
    private var authRepository: AuthRepository,
    private var tokenManager: JwtTokenManager,
    val userState: UserState
) : ViewModel(), LifecycleObserver {

    private val  _eventFlow = MutableSharedFlow<AuthEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val _loginState = mutableStateOf("")
    val loginState: State<String> = _loginState

    fun setLogin(value:String){
        _loginState.value = value
    }

    private val _passwordState = mutableStateOf("")
    val passwordState: State<String> = _passwordState

    fun setPassword(value:String){
        _passwordState.value = value
    }

    fun login() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = authRepository.login(
                    loginState.value,
                    passwordState.value
                )
                tokenManager.saveAccessJwt(response.accessToken)
                tokenManager.saveRefreshJwt(response.refreshToken)
                tokenManager.saveUserId(response.user.id)
                tokenManager.saveUserRole(response.user.role)

                userState.setRole(response.user.role)
                userState.setId(response.user.id)

                _eventFlow.emit(
                    AuthEvent.NavigateEvent(Route.MyEquipments.route)
                )
            } catch (e: Exception) {
                _eventFlow.emit(AuthEvent.SnackbarEvent(e.message ?: "Unknown error occurred"))
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            try {
                val id = tokenManager.getUserId()
                val token = tokenManager.getRefreshJwt()
                if (id == null || token == null) {
                    _eventFlow.emit(AuthEvent.SnackbarEvent("Try reopen app"))
                    return@launch
                }
                authRepository.logout(id, token)

                tokenManager.clearAllTokens()

                userState.logout()

            } catch (e: Exception) {
                _eventFlow.emit(AuthEvent.SnackbarEvent(e.message ?: "Unknown error occurred"))
            }
        }
    }
}