package com.example.equiptrack.viewmodels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.equiptrack.common.RegisterState
import com.example.equiptrack.data.dto.EquipmentCreateRequest
import com.example.equiptrack.data.dto.NetworkResult
import com.example.equiptrack.data.reposutory.EquipmentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateEquipmentViewModel
@Inject constructor(
    private var equipmentRepository: EquipmentRepository
) : ViewModel(), LifecycleObserver {
    val registerEvent = MutableSharedFlow<RegisterState>()
    private val _equipment = mutableStateOf(EquipmentCreateRequest("", "", ""))
    val equipment: State<EquipmentCreateRequest> = _equipment

    fun setName(value:String){
        _equipment.value = _equipment.value.copy(name = value)
    }

    fun setShortDescription(value:String){
        _equipment.value = _equipment.value.copy(shortDescription = value)
    }

    fun setFullDescription(value:String){
        _equipment.value = _equipment.value.copy(fullDescription = value)
    }

    fun create() {
        viewModelScope.launch {
            when (val response = equipmentRepository.createEquipment(_equipment.value)) {
                is NetworkResult.Error -> registerEvent.emit(RegisterState.Error(response.message.toString()))
                is NetworkResult.Success -> registerEvent.emit(RegisterState.Success)
                else -> {}
            }
        }
    }

    fun loadInfo(id: String) {
        viewModelScope.launch {
            when(val response = equipmentRepository.getEquipmentInfoById(id)) {
                is NetworkResult.Error -> registerEvent.emit(RegisterState.Error("Can not get equipment info"))
                is NetworkResult.Success -> {
                    _equipment.value = _equipment.value.copy(
                        name = response.data.name,
                        shortDescription = response.data.shortDescription,
                        fullDescription = response.data.fullDescription
                    )
                }
                else -> {}
            }
        }
    }

    fun updateInfo(id: String) {
        viewModelScope.launch {
            if (equipmentRepository.updateEquipment(id, _equipment.value).isSuccessful) {
                registerEvent.emit(RegisterState.Success)
            } else {
                registerEvent.emit(RegisterState.Error("Some error occurred"))
            }
        }
    }
}