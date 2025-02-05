package com.example.equiptrack.ui.screens

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
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
import com.example.equiptrack.ui.base.Route
import com.example.equiptrack.ui.elements.ErrorItem
import com.example.equiptrack.ui.elements.LoadingItem
import com.example.equiptrack.ui.elements.LoadingView
import com.example.equiptrack.ui.elements.EquipmentEditableItem
import com.example.equiptrack.ui.elements.FloatingAddButton
import com.example.equiptrack.viewmodels.EquipmentViewModel
import kotlinx.coroutines.launch

//@Composable
//fun ManageEquipmentsScreen() {
//    Text("ManageEquipmentsScreen")
//}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageEquipmentsScreen(
    navHostController: NavHostController

) {
    val viewModel = hiltViewModel<EquipmentViewModel, EquipmentViewModel.EquipmentViewModelFactory> { factory ->
        factory.create("")
    }
    Scaffold(
        floatingActionButton = { FloatingAddButton(
            onClick = { navHostController.navigate(Route.CreateEquipment.route) },
            icon = Icons.Outlined.Add) }
    ) {
        val lazyEquipmentItems: LazyPagingItems<EquipmentShort> =
            viewModel.equipments.collectAsLazyPagingItems()
        val context = LocalContext.current
        val state = rememberPullToRefreshState()

        val refreshScope = rememberCoroutineScope()
        var refreshing by remember { mutableStateOf(false) }
        val refresh: () -> Unit = {
            refreshScope.launch {
                refreshing = true
                lazyEquipmentItems.refresh()
                refreshing = false
            }
        }

        PullToRefreshBox(
            isRefreshing = refreshing,
            onRefresh = refresh,
            state = state
        ) {
            LazyColumn(Modifier) {

                item { Text("Available equipments") }

                if (lazyEquipmentItems.loadState.refresh !is LoadState.Loading && lazyEquipmentItems.itemCount == 0) {
                    item { Text("no equipments available") }
                }
                items(
                    count = lazyEquipmentItems.itemCount,
//                    key = lazyEquipmentItems.itemKey { it },
                    contentType = lazyEquipmentItems.itemContentType { "EquipmentShortItem" }
//                    count = lazyEquipmentItems.itemCount
                ) {
                    EquipmentEditableItem(
                        equipment = lazyEquipmentItems[it]!!,
                        navigateToEdit = { route: String -> navHostController.navigate("update_equip/%s".format(route)) },
                        delete = { id: String ->
                            viewModel.deleteEquipment(id)
                            refresh()
                        }
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