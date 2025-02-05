package com.example.equiptrack.ui.screens

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.QrCode
import androidx.compose.material.icons.outlined.QrCodeScanner
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemContentType
import com.example.equiptrack.data.dto.EquipmentShort
import com.example.equiptrack.ui.elements.ErrorItem
import com.example.equiptrack.ui.elements.LoadingItem
import com.example.equiptrack.ui.elements.LoadingView
import com.example.equiptrack.ui.elements.EquipmentItem
import com.example.equiptrack.ui.elements.FloatingAddButton
import com.example.equiptrack.viewmodels.AuthorizationVewModel
import com.example.equiptrack.viewmodels.EquipmentViewModel
import com.google.mlkit.vision.codescanner.GmsBarcodeScanner
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning
import kotlinx.coroutines.launch
import kotlin.time.Duration


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyEquipmentsScreen(
//    viewModel: EquipmentViewModel = hiltViewModel(),
    authViewModel: AuthorizationVewModel = hiltViewModel(),
    navHostController: NavHostController
) {
    val viewModel = hiltViewModel<EquipmentViewModel, EquipmentViewModel.EquipmentViewModelFactory> { factory ->
        factory.create(authViewModel.userIdState.value)
    }
    val context = LocalContext.current

    Scaffold(
        floatingActionButton = {
            FloatingAddButton(
                onClick = { scanQr(context, navHostController) },
                icon = Icons.Outlined.QrCodeScanner
            )
        }
    ) {
        val lazyEquipmentItems: LazyPagingItems<EquipmentShort> =
            viewModel.myEquipments.collectAsLazyPagingItems()

        val refreshScope = rememberCoroutineScope()
        var refreshing by remember { mutableStateOf(false) }
        val state = rememberPullToRefreshState()
        val refresh: () -> Unit = {
            refreshScope.launch {
                refreshing = true
                lazyEquipmentItems.refresh()
                refreshing = false
            }
        }

        PullToRefreshBox(
            state = state,
            isRefreshing = refreshing,
            onRefresh = refresh,
        ) {
            LazyColumn(Modifier) {

                item { Text("My equipments") }

                if (lazyEquipmentItems.loadState.refresh !is LoadState.Loading && lazyEquipmentItems.itemCount == 0) {
                    item { Text("no equipments available") }
                }
                items(
                    count = lazyEquipmentItems.itemCount,
//                    key = lazyEquipmentItems.itemKey { it },
                    contentType = lazyEquipmentItems.itemContentType { "EquipmentShortItem" }
//                    count = lazyEquipmentItems.itemCount
                ) {
                    EquipmentItem(
                        equipment = lazyEquipmentItems[it]!!,
                        navigate = { route: String -> navHostController.navigate(route) },
                        showReserved = false
                    )
                }
                lazyEquipmentItems.apply {
                    when {
                        loadState.refresh is LoadState.Loading -> {
                            item { LoadingView(modifier = Modifier.fillParentMaxSize()) }
                        }
                        loadState.append is LoadState.Loading -> {
                            item { LoadingItem() }
                        }
                        loadState.refresh is LoadState.Error -> {
                            val e = lazyEquipmentItems.loadState.refresh as LoadState.Error
                            if (lazyEquipmentItems.itemCount > 0) {
                                Toast.makeText(
                                    context,
                                    e.error.localizedMessage,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            if (lazyEquipmentItems.itemCount == 0) {
                                item {
                                    ErrorItem(
                                        message = e.error.localizedMessage!!,
                                        modifier = Modifier.fillParentMaxSize(),
                                        onClickRetry = { retry() }
                                    )
                                }
                            }
                        }
                        loadState.append is LoadState.Error -> {
                            val e = lazyEquipmentItems.loadState.append as LoadState.Error
                            item {
                                ErrorItem(
                                    message = e.error.localizedMessage!!,
                                    modifier = Modifier.fillParentMaxSize(),
                                    onClickRetry = { retry() }
                                )
                            }
                        }
                    }
                }
            }
        }

    }
}

fun scanQr(context: Context, navHostController: NavHostController) {
    val scanner = GmsBarcodeScanning.getClient(context)
    scanner.startScan()
        .addOnSuccessListener { barcode ->
            if (barcode.rawValue != null) {
                val value = barcode.rawValue!!
                val regex =
                    """^reserve/[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$""".toRegex()
                if (regex.matches(value)) {
                    navHostController.navigate(value)
                } else {
                    Toast.makeText(context,
                        "Error: Wrong QR format", Toast.LENGTH_LONG).show()
                }
            }
        }
        .addOnFailureListener { e ->
            Toast.makeText(context, "Error occured: ${e.message}", Toast.LENGTH_SHORT).show()
        }
}