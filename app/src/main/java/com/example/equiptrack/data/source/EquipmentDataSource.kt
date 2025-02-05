package com.example.equiptrack.data.source

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.equiptrack.Constants
import com.example.equiptrack.data.dto.EquipmentShort
import com.example.equiptrack.data.reposutory.EquipmentRepository

class EquipmentDataSource constructor(
    private val repository: EquipmentRepository,
    private val userId: String? = null
) : PagingSource<Int, EquipmentShort>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, EquipmentShort> {
        return try {
            val page = params.key ?: Constants.DEFAULT_PAGE_NUM
            val equipmentListResponse =
                if (userId != null) {
                    repository.getUserEquipments(
                        userId,
                        Constants.DEFAULT_PAGE_SIZE,
                        page
                    )
                } else repository.getEquipments(
                Constants.DEFAULT_PAGE_SIZE,
                page
            )
            LoadResult.Page(
                data = equipmentListResponse.equipments,
                prevKey = if (page <= 1) null else page - 1,
                nextKey = if (equipmentListResponse.hasMore) equipmentListResponse.page.plus(1) else null
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, EquipmentShort>): Int? {
        return 1
    }
}
