package com.example.equiptrack.viewmodels

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.equiptrack.common.RegisterState
import com.example.equiptrack.data.dto.NetworkResult
import com.example.equiptrack.data.dto.UserCreate
import com.example.equiptrack.data.reposutory.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateUserViewModel
@Inject constructor(
    private var userRepository: UserRepository
) : ViewModel(), LifecycleObserver {
    val registerEvent = MutableSharedFlow<RegisterState>()
    private val _user = mutableStateOf(UserCreate("", "", ""))
    val user: State<UserCreate> = _user

    fun setLogin(value:String){
        _user.value = _user.value.copy(login = value)
    }

    fun setPassword(value:String){
        _user.value = _user.value.copy(password = value)
    }

    fun register(isAdmin: Boolean) {
        viewModelScope.launch {
            _user.value = _user.value.copy(role = if (isAdmin) "admin" else "user")
            when (val response = userRepository.registerUser(_user.value)) {
                is NetworkResult.Error -> registerEvent.emit(RegisterState.Error("This login is already taken"))
                is NetworkResult.Success -> registerEvent.emit(RegisterState.Success)
                else -> {}
            }
//            try {
//                _user.value = _user.value.copy(role = if (isAdmin) "admin" else "user")
//                userRepository.registerUser(_user.value)
//                registerEvent.emit(RegisterState.Success)
//            } catch (e: Exception) {
//                Log.e("err", e.message ?: "")
//                registerEvent.emit(RegisterState.Error(e.message ?: ""))
//            }
        }
    }
}