package com.example.equiptrack.viewmodels

import android.media.audiofx.DynamicsProcessing.Eq
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.equiptrack.Constants
import com.example.equiptrack.data.dto.EquipmentDetailed
import com.example.equiptrack.data.dto.EquipmentShort
import com.example.equiptrack.data.dto.NetworkResult
import com.example.equiptrack.data.dto.ReservationInfoResponse
import com.example.equiptrack.data.reposutory.EquipmentRepository
import com.example.equiptrack.data.source.EquipmentDataSource
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel(assistedFactory = EquipmentViewModel.EquipmentViewModelFactory::class)
class EquipmentViewModel
@AssistedInject constructor(
    private var equipmentRepository: EquipmentRepository,
    @Assisted val userId: String
) : ViewModel(), LifecycleObserver {

    @AssistedFactory
    interface EquipmentViewModelFactory {
        fun create(userId: String): EquipmentViewModel
    }

    val equipments: Flow<PagingData<EquipmentShort>> = Pager(
        PagingConfig(pageSize = Constants.DEFAULT_PAGE_SIZE)
    ) {
        EquipmentDataSource(equipmentRepository)
    }.flow.cachedIn(viewModelScope)

    val myEquipments: Flow<PagingData<EquipmentShort>> = Pager(
        PagingConfig(pageSize = Constants.DEFAULT_PAGE_SIZE)
    ) {
        EquipmentDataSource(equipmentRepository, userId)
    }.flow.cachedIn(viewModelScope)

    private val _equipmentState:  MutableState<NetworkResult<EquipmentDetailed>> = mutableStateOf<NetworkResult<EquipmentDetailed>>(NetworkResult.Empty)
    val equipmentState: State<NetworkResult<EquipmentDetailed>> = _equipmentState

    private val _reservationState:  MutableState<NetworkResult<ReservationInfoResponse>> = mutableStateOf<NetworkResult<ReservationInfoResponse>>(NetworkResult.Empty)
    val reservationState: State<NetworkResult<ReservationInfoResponse>> = _reservationState

    fun fetchEquipmentInfo(equipmentId: String){
        viewModelScope.launch {
            _equipmentState.value = equipmentRepository.getEquipmentInfoById(equipmentId)
//            try {
//                _equipmentState.value = equipmentRepository.getEquipmentInfoById(equipmentId)
//            } catch (e: Exception) {
//                _equipmentState.value = NetworkResult.Error(e)
//            }
        }
    }

    fun fetchReservationInfo(equipmentId: String) {
        viewModelScope.launch {
            _reservationState.value = equipmentRepository.getEquipmentReservationInfo(equipmentId)
//            try {
//                _reservationState.value = equipmentRepository.getEquipmentReservationInfo(equipmentId)
//            } catch (e: Exception) {
//                _reservationState.value = NetworkResult.Error(e)
//            }
        }
    }

    fun deleteEquipment(id: String) {
        viewModelScope.launch {
            try {
                equipmentRepository.deleteEquipment(id)
            } catch (e: Exception) {}
        }
    }
}
