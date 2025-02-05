package com.example.equiptrack.data.dto

sealed class NetworkResult<out T : Any> {
    data class Success<out T : Any>(val data: T) : NetworkResult<T>()
    data class Error(val code: Int, val message: String?) : NetworkResult<Nothing>()
    object Progress : NetworkResult<Nothing>()
    object Empty: NetworkResult<Nothing>()
}