package com.example.equiptrack.data.source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.equiptrack.Constants
import com.example.equiptrack.data.dto.EquipmentShort
import com.example.equiptrack.data.dto.User
import com.example.equiptrack.data.reposutory.AuthRepository
import com.example.equiptrack.data.reposutory.EquipmentRepository
import com.example.equiptrack.data.reposutory.UserRepository

class UserDataSource constructor(
    private val repository: UserRepository,
) : PagingSource<Int, User>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, User> {
        return try {
            val page = params.key ?: Constants.DEFAULT_PAGE_NUM
            val userListResponse = repository.getUsers(
                    Constants.DEFAULT_PAGE_SIZE,
                    page
                )
            LoadResult.Page(
                data = userListResponse.users,
                prevKey = if (page <= 1) null else page - 1,
                nextKey = if (userListResponse.hasMore) userListResponse.page.plus(1) else null
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, User>): Int? {
        return 1
    }
}