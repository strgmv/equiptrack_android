package com.example.equiptrack.viewmodels

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.equiptrack.data.dto.NetworkResult
import com.example.equiptrack.data.dto.ReservationRequest
import com.example.equiptrack.data.reposutory.EquipmentRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.plus
import kotlinx.datetime.todayIn

@HiltViewModel(assistedFactory = ReservationViewModel.ReservationViewModelFactory::class)
class ReservationViewModel @AssistedInject constructor(
    private var equipmentRepository: EquipmentRepository,
    @Assisted val equipmentId: String
) : ViewModel(), LifecycleObserver {

    @AssistedFactory
    interface ReservationViewModelFactory {
        fun create(equipmentId: String): ReservationViewModel
    }


    private val clock: Clock = Clock.System
    private val tz = TimeZone.currentSystemDefault()

    private val _startDateState = mutableStateOf(clock.todayIn(tz).atStartOfDayIn(tz).toEpochMilliseconds())
    val startDateState: State<Long> = _startDateState

    private val _endDateState = mutableStateOf(clock.todayIn(tz).atStartOfDayIn(tz).toEpochMilliseconds())
    val endDateState: State<Long> = _endDateState

    private val _datesSet = mutableStateOf(true)
    val datesSet : State<Boolean> = _datesSet

    fun setDates(startDate : Long, endDate : Long) {
        if (startDate < clock.todayIn(tz).atStartOfDayIn(tz).toEpochMilliseconds()
            || endDate < startDate) {
            _datesSet.value = false
        } else {
            _startDateState.value = startDate
            _endDateState.value = endDate
            _datesSet.value = true
        }
    }

//    pair <hours, minutes>
    private val _startTimeState = mutableStateOf(Pair(0, 0))
    val startTimeState: State<Pair<Int, Int>> = _startTimeState

    private val _endTimeState = mutableStateOf(Pair(0, 0))
    val endTimeState: State<Pair<Int, Int>> = _endTimeState

    private val _timeSet = mutableStateOf(true)
    val timeSet : State<Boolean> = _timeSet

    fun setStartTime(hours : Int, minutes: Int) {
        if (isValidTime(hours, minutes)) {
            _startTimeState.value = Pair(hours, minutes)
            _timeSet.value = true
        } else {
            _timeSet.value = false
        }
    }

    fun setEndTime(hours : Int, minutes: Int) {
        if (isValidTime(hours, minutes)) {
            _endTimeState.value = Pair(hours, minutes)
            _timeSet.value = true
        } else {
            _timeSet.value = false
        }
    }

    fun reserve() {
        val startInstant = Instant.fromEpochMilliseconds(_startDateState.value)
            .plus(_startTimeState.value.first, DateTimeUnit.HOUR)
            .plus(_startTimeState.value.second, DateTimeUnit.MINUTE)
        val endInstant = Instant.fromEpochMilliseconds(_endDateState.value)
            .plus(_endTimeState.value.first, DateTimeUnit.HOUR)
            .plus(_endTimeState.value.second, DateTimeUnit.MINUTE)

        viewModelScope.launch {
            try {
                equipmentRepository.reserveEquipment(equipmentId, startInstant, endInstant)
            } catch (e: Exception) {

            }
        }
    }


    private fun isValidTime(hours : Int, minutes: Int) : Boolean {
        return !(hours < 0 || hours > 23
                || minutes < 0 || minutes > 59)
    }


}