package com.example.equiptrack.ui.screens

import android.icu.text.SimpleDateFormat
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DateRangePickerState
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.equiptrack.ui.theme.jetNewsSample.JetnewsTheme
import com.example.equiptrack.viewmodels.AuthorizationVewModel
import com.example.equiptrack.viewmodels.EquipmentViewModel
import com.example.equiptrack.viewmodels.ReservationViewModel
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReservationScreen(
    equipmentId: String,
//    reservationViewModel: ReservationViewModel = hiltViewModel()
) {
    val reservationViewModel = hiltViewModel<ReservationViewModel, ReservationViewModel.ReservationViewModelFactory> { factory ->
        factory.create(equipmentId)
    }

    val context = LocalContext.current
    var showDatePicker by remember { mutableStateOf(false) }
    var showStartTimePicker by remember { mutableStateOf(false) }
    var showEndTimePicker by remember { mutableStateOf(false) }

    val selectedDate =
        convertMillisToDate(reservationViewModel.startDateState.value) + " - " +
        convertMillisToDate(reservationViewModel.endDateState.value)
    val  startTime = convertPairToTime(reservationViewModel.startTimeState.value)
    val  endTime = convertPairToTime(reservationViewModel.endTimeState.value)



    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column (
            modifier = Modifier
        ) {
            OutlinedTextField(
                value = selectedDate,
                onValueChange = { },
                label = { Text("Select date range") },
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = { showDatePicker = !showDatePicker }) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = "Select dates"
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showStartTimePicker = true }
            ) {
                Text(
                    text = "Start time",
                )
                Text(
                    text = startTime,
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showEndTimePicker = true }
            ) {
                Text(
                    text = "End time",
                )
                Text(
                    text = endTime,
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    reservationViewModel.reserve()
                },
                shape = MaterialTheme.shapes.small,
//                colors = ,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    text = "Reserve",
                    textAlign = TextAlign.Center,
                    fontSize = 18.sp
                )
            }
        }

        if (showDatePicker) {
            DateRangePickerModal(
                onDateRangeSelected = onDateRangeSelectedLambda@{ dates: Pair<Long?, Long?> ->
                    val start = dates.first
                    val end = dates.second
                    if (start != null && end != null) {
                        reservationViewModel.setDates(start, end)
                        if (!reservationViewModel.datesSet.value) {
                            Toast.makeText(
                                context,
                                "select valid dates",
                                Toast.LENGTH_SHORT
                            ).show()
                            return@onDateRangeSelectedLambda false
                        }
                        return@onDateRangeSelectedLambda true
                    } else {
                        Toast.makeText(
                            context,
                            "select both dates",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    return@onDateRangeSelectedLambda false
                },
                onDismiss = { showDatePicker = false }
            )
        }

        if (showStartTimePicker) {
            DialTimePickerDialog(
                onDismiss = { showStartTimePicker = false },
                onConfirm = { state : TimePickerState ->
                    reservationViewModel.setStartTime(state.hour, state.minute)
                    showStartTimePicker = false
                }
            )
        }
        if (showEndTimePicker) {
            DialTimePickerDialog(
                onDismiss = { showEndTimePicker = false },
                onConfirm = { state : TimePickerState ->
                    reservationViewModel.setEndTime(state.hour, state.minute)
                    showEndTimePicker = false
                }
            )
        }
    }
}

fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return formatter.format(Date(millis))
}

fun convertPairToTime(time : Pair<Int, Int>): String {
    return "%02d:%02d".format(time.first, time.second)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateRangePickerModal(
    onDateRangeSelected: (Pair<Long?, Long?>) -> Boolean,
    onDismiss: () -> Unit
) {
    val dateRangePickerState = rememberDateRangePickerState()

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    if (onDateRangeSelected(
                        Pair(
                            dateRangePickerState.selectedStartDateMillis,
                            dateRangePickerState.selectedEndDateMillis
                        )
                    )) onDismiss()
                }
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    ) {
        DateRangePicker(
            state = dateRangePickerState,
            title = {
                Text(
                    text = "Select date range"
                )
            },
            showModeToggle = false,
            modifier = Modifier
                .fillMaxWidth()
                .height(500.dp)
                .padding(16.dp)
        )
    }
}

@Composable
@Preview
fun Prev() {
    JetnewsTheme {
        ReservationScreen("")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialTimePickerDialog(
    onConfirm: (TimePickerState) -> Unit,
    onDismiss: () -> Unit,
) {
    val currentTime = Calendar.getInstance()

    val timePickerState = rememberTimePickerState(
        initialHour = currentTime.get(Calendar.HOUR_OF_DAY),
        initialMinute = currentTime.get(Calendar.MINUTE),
        is24Hour = true,
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text("Dismiss")
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(timePickerState) }) {
                Text("OK")
            }
        },
        text = {
            TimePickerDialog(
                onDismiss = { onDismiss() },
                onConfirm = { onConfirm(timePickerState) }
            ) {
                TimePicker(
                    state = timePickerState,
                )
            }
        }
    )
}

@Composable
fun TimePickerDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    content: @Composable () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text("Dismiss")
            }
        },
        confirmButton = {
            TextButton(onClick = { onConfirm() }) {
                Text("OK")
            }
        },
        text = { content() }
    )
}