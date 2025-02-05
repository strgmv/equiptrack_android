package com.example.equiptrack.viewmodels

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.equiptrack.Constants
import com.example.equiptrack.data.dto.EquipmentShort
import com.example.equiptrack.data.dto.User
import com.example.equiptrack.data.reposutory.AuthRepository
import com.example.equiptrack.data.reposutory.UserRepository
import com.example.equiptrack.data.source.EquipmentDataSource
import com.example.equiptrack.data.source.UserDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ManageUsersViewModel
@Inject constructor(
    private var userRepository: UserRepository
) : ViewModel(), LifecycleObserver {

    val users: Flow<PagingData<User>> = Pager(
        PagingConfig(pageSize = Constants.DEFAULT_PAGE_SIZE)
    ) {
        UserDataSource(userRepository)
    }.flow.cachedIn(viewModelScope)

    fun deleteUser(id: String) {
        viewModelScope.launch {
            try {
                userRepository.deleteUser(id)
            } catch (e: Exception) {}
        }
    }
}