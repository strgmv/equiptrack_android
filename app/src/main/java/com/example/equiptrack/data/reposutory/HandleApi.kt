package com.example.equiptrack.data.reposutory

import com.example.equiptrack.data.dto.NetworkResult
import retrofit2.HttpException
import retrofit2.Response

suspend fun <T : Any> handleApi(
    execute: suspend () -> Response<T>
): NetworkResult<T> {
    return try {
        val response = execute()
        val body = response.body()
        if (response.isSuccessful && body != null) {
            NetworkResult.Success(body)
        } else {
            NetworkResult.Error(code = response.code(), message = response.message())
        }
    } catch (e: HttpException) {
        NetworkResult.Error(code = e.code(), message = e.message())
    } catch (e: Throwable) {
        NetworkResult.Error(code = 0, message = e.message)
    }
}