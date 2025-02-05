package com.example.equiptrack.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.equiptrack.R
import com.example.equiptrack.common.AuthEvent
import com.example.equiptrack.ui.base.Route
import com.example.equiptrack.viewmodels.AuthorizationVewModel
import com.example.equiptrack.viewmodels.EquipmentViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SettingsScreen(
    navHostController: NavHostController,
    viewModel: AuthorizationVewModel = hiltViewModel()
) {
    val context = LocalContext.current

    LaunchedEffect(true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is AuthEvent.SnackbarEvent -> {
                    Toast.makeText(
                        context,
                        event.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }

                is AuthEvent.NavigateEvent -> {
                    navHostController.navigate(event.route)
                }
            }
        }
    }

    Column {
        Text("settings")
        OutlinedButton(onClick = {
            viewModel.logout()
            navHostController.navigate(Route.Authorization.route) { popUpTo(0) }
        }) {
            Text(text = stringResource(id = R.string.logout))
        }
    }
}