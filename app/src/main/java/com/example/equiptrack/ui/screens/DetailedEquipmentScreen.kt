package com.example.equiptrack.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.QrCode
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.equiptrack.R
import com.example.equiptrack.data.dto.EquipmentDetailed
import com.example.equiptrack.data.dto.NetworkResult
import com.example.equiptrack.data.dto.ReservationInfo
import com.example.equiptrack.ui.base.Route
import com.example.equiptrack.ui.elements.ErrorItem
import com.example.equiptrack.ui.elements.FloatingAddButton
import com.example.equiptrack.ui.elements.LoadingView
import com.example.equiptrack.ui.theme.jetNewsSample.JetnewsTheme
import com.example.equiptrack.viewmodels.EquipmentViewModel
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format.char
import kotlinx.datetime.toLocalDateTime

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailedEquipmentScreen(
    navHostController: NavHostController,
//    viewModel: EquipmentViewModel = hiltViewModel(),
    equipmentId: String
) {
//    Text("equip details of $equipmentId")
    val viewModel = hiltViewModel<EquipmentViewModel, EquipmentViewModel.EquipmentViewModelFactory> { factory ->
        factory.create("")
    }

    Scaffold (
        floatingActionButton = {
            FloatingAddButton(
                onClick = { navHostController.navigate("qr/$equipmentId") },
                icon = Icons.Outlined.QrCode
            )
        }
    ) {
        val equipInfo = viewModel.equipmentState.value
        val reservationInfo = viewModel.reservationState.value

        val refreshScope = rememberCoroutineScope()
        var refreshing by remember { mutableStateOf(false) }
        val refresh: () -> Unit = {
            refreshing = true
            refreshScope.launch {
                viewModel.fetchEquipmentInfo(equipmentId)
                viewModel.fetchReservationInfo(equipmentId)
                refreshing = false
            }
        }

        val state = rememberPullToRefreshState()

        PullToRefreshBox(
            modifier = Modifier.fillMaxSize(),
//            .scrollable(orientation = Orientation.Vertical, state = rememberScrollState()),
            state = state,
            isRefreshing = refreshing,
            onRefresh = refresh,
        ) {

            when (equipInfo) {
                is NetworkResult.Progress -> LoadingView(modifier = Modifier.fillMaxSize())
                is NetworkResult.Success -> {
                    if (reservationInfo is NetworkResult.Success) {
//                    equipmentContent(
//                        equipment = equipInfo.data,
//                        navHostController = navHostController,
//                        reservations = reservationInfo.data.data
//                    )
                        EquipmentDetailContent(
                            equipment = equipInfo.data,
                            reservations = reservationInfo.data.data
                        )
                    } else {
                        EquipmentDetailContent(
                            equipment = equipInfo.data,
                            reservations = emptyList()
                        )
//                    equipmentContent(
//                        equipment = equipInfo.data,
//                        navHostController = navHostController,
//                        reservations = emptyList()
//                    )
                    }
                    Button(
                        onClick = { navHostController.navigate("reserve/$equipmentId") },
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(16.dp),
                        shape = MaterialTheme.shapes.small,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    ) {
                        Text(text = stringResource(id = R.string.reserve_btn))
                    }
                }

                is NetworkResult.Error -> ErrorItem(
                    message = equipInfo.message.toString(),
                    modifier = Modifier.fillMaxSize()
                ) {
                    viewModel.fetchEquipmentInfo(equipmentId)
                    viewModel.fetchReservationInfo(equipmentId)
                }

                NetworkResult.Empty -> {
                    viewModel.fetchEquipmentInfo(equipmentId)
                    viewModel.fetchReservationInfo(equipmentId)
                }
            }
        }
    }
}

@Composable
fun EquipmentDetailContent(
    equipment: EquipmentDetailed,
    reservations: List<ReservationInfo>,
    modifier: Modifier = Modifier,
) {
    LazyColumn(modifier = modifier.padding(16.dp)) {
        item {
            Text(
                text = equipment.name,
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = equipment.shortDescription,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = equipment.fullDescription,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(16.dp))
        }

        if (reservations.isNotEmpty()) {
            item {
                Text(
                    text = stringResource(id = R.string.reservation_sub),
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                )

                Spacer(modifier = Modifier.height(8.dp))
            }

            items(reservations) { info ->
                Text(
                    text = "From: ${formatDateTime(info.reservationStart.toLocalDateTime(TimeZone.currentSystemDefault()))}\n" +
                            "To: ${formatDateTime(info.reservationEnd.toLocalDateTime(TimeZone.currentSystemDefault()))}",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewEquipmentDetailContent() {
    JetnewsTheme {
        EquipmentDetailContent(
            equipment = EquipmentDetailed(
                id = "",
                name = "Advanced Drill",
                shortDescription = "High-power drill for heavy-duty tasks.",
                fullDescription = "This drill offers advanced performance with multiple settings and high torque. Perfect for construction projects.",
            ),
            reservations = listOf(
                ReservationInfo(
                    reservationStart = Instant.parse("2024-12-23T09:58:22.91491Z"),
                    reservationEnd = Instant.parse("2024-12-23T09:58:22.91491Z")
                ),
//                ReservationInfo(
//                    reservationStart = Instant.parse("2024-12-21T00:00"),
//                    reservationEnd = Instant.parse("2024-12-30T19:00")
//                )
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewEmptyResEquipmentDetailContent() {
    JetnewsTheme {
        EquipmentDetailContent(
            equipment = EquipmentDetailed(
                id = "",
                name = "Advanced Drill",
                shortDescription = "High-power drill for heavy-duty tasks.",
                fullDescription = "This drill offers advanced performance with multiple settings and high torque. Perfect for construction projects.",
            ),
            reservations = emptyList()
        )
    }
}



fun formatDateTime(time: LocalDateTime): String {
    val format = LocalDateTime.Format {
        year()
        char('-')
        monthNumber()
        char('-')
        dayOfMonth()

        char(' ')

        hour()
        char(':')
        minute()
    }

    return format.format(time)
}