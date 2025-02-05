package com.example.equiptrack.common


sealed class RegisterState {
    data object Loading : RegisterState()
    data object Success : RegisterState()
    data class Error(val message: String): RegisterState()
}