package com.example.equiptrack.service

import com.example.equiptrack.data.dto.GetUsersResponse
import com.example.equiptrack.data.dto.NetworkResult
import com.example.equiptrack.data.dto.User
import com.example.equiptrack.data.dto.UserCreate
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface UserService {
    @GET("auth/all")
    suspend fun getUsers(
        @Query("size") pageSize: Int,
        @Query("page") pageNumber: Int,
    ): GetUsersResponse

    @DELETE("auth/{user_id}")
    suspend fun deleteUser(@Path("user_id") id: String): Response<ResponseBody>

    @POST("auth/register")
    suspend fun registerUser(@Body user: UserCreate): Response<ResponseBody>
}