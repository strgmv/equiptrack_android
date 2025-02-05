package com.example.equiptrack.common

sealed class AuthEvent {
    data class SnackbarEvent(val message : String) : AuthEvent()
    data class NavigateEvent(val route: String) : AuthEvent()
}