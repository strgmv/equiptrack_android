package com.example.equiptrack.ui.screens

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.padding
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
import com.example.equiptrack.data.dto.User
import com.example.equiptrack.ui.base.Route
import com.example.equiptrack.ui.elements.ErrorItem
import com.example.equiptrack.ui.elements.FloatingAddButton
import com.example.equiptrack.ui.elements.LoadingItem
import com.example.equiptrack.ui.elements.LoadingView
import com.example.equiptrack.ui.elements.UserItem
import com.example.equiptrack.viewmodels.ManageUsersViewModel
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageUsersScreen(
    navHostController: NavHostController,
    viewModel: ManageUsersViewModel = hiltViewModel(),
) {
    Scaffold(
        floatingActionButton = {
            FloatingAddButton(
                onClick = { navHostController.navigate(Route.RegisterUser.route) },
                icon = Icons.Outlined.Add
            )
        }
    ) {
        val lazyUserItems: LazyPagingItems<User> =
            viewModel.users.collectAsLazyPagingItems()
        val context = LocalContext.current
        val state = rememberPullToRefreshState()

        val refreshScope = rememberCoroutineScope()
        var refreshing by remember { mutableStateOf(false) }
        val refresh: () -> Unit = {
            refreshScope.launch {
                refreshing = true
                lazyUserItems.refresh()
                refreshing = false
            }
        }

        PullToRefreshBox(
            modifier = Modifier.padding(),
            isRefreshing = refreshing,
            onRefresh = refresh,
            state = state
        ) {
            LazyColumn(Modifier) {

                item { Text("Users") }

                if (lazyUserItems.loadState.refresh !is LoadState.Loading && lazyUserItems.itemCount == 0) {
                    item { Text("No users") }
                }
                items(
                    count = lazyUserItems.itemCount,
                    contentType = lazyUserItems.itemContentType { "UserItem" }
                ) {
                    UserItem (
                        user = lazyUserItems[it]!!,
                        navigateToEdit = { id: String -> navHostController.navigate("edit_user/%s".format(id)) },
                        delete = { id: String ->
                            viewModel.deleteUser(id)
                            refresh()
                        }
                    )
                }
                lazyUserItems.apply {
                    when {
                        loadState.refresh is LoadState.Loading -> {
                            item { LoadingView(modifier = Modifier.fillParentMaxSize()) }
                        }
                        loadState.append is LoadState.Loading -> {
                            item { LoadingItem() }
                        }
                        loadState.refresh is LoadState.Error -> {
                            val e = lazyUserItems.loadState.refresh as LoadState.Error
                            if (lazyUserItems.itemCount > 0) {
                                Toast.makeText(
                                    context,
                                    e.error.localizedMessage,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            if (lazyUserItems.itemCount == 0) {
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
                            val e = lazyUserItems.loadState.append as LoadState.Error
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