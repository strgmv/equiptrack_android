package com.example.equiptrack.data.reposutory

import com.example.equiptrack.Constants
import com.example.equiptrack.data.dto.GetEquipmentsResponse
import com.example.equiptrack.data.dto.GetUsersResponse
import com.example.equiptrack.data.dto.NetworkResult
import com.example.equiptrack.data.dto.User
import com.example.equiptrack.data.dto.UserCreate
import com.example.equiptrack.service.EquipmentService
import com.example.equiptrack.service.UserService
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor(
    private val network: UserService
) {
    suspend fun getUsers(
        pageSize: Int = Constants.DEFAULT_PAGE_SIZE,
        pageNumber: Int = Constants.DEFAULT_PAGE_NUM,
    ): GetUsersResponse {
        val res = network.getUsers(
            pageSize,
            pageNumber,
        )
        return res
    }

    suspend fun deleteUser(id: String): Response<ResponseBody> = network.deleteUser(id)

    suspend fun registerUser(user: UserCreate):  NetworkResult<ResponseBody> {
        return handleApi { network.registerUser(user) }
    }
}