package com.example.equiptrack.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.equiptrack.common.RegisterState
import com.example.equiptrack.viewmodels.CreateEquipmentViewModel

@Composable
fun CreateEquipmentScreen(
    navHostController: NavHostController,
    viewModel: CreateEquipmentViewModel = hiltViewModel(),
    updateId: String = ""
) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        if (updateId != "") {
            viewModel.loadInfo(updateId)
        }
    }

    LaunchedEffect(key1 = true) {
        viewModel.registerEvent.collect { event ->
            when (event) {
                is RegisterState.Success -> {
                    Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
                    navHostController.popBackStack()
                }
                is RegisterState.Error -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_LONG).show()
                }

                else -> {}
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = viewModel.equipment.value.name,
                onValueChange = {
                    viewModel.setName(it)
                },
                placeholder = {
                    Text(text = "Name")
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                ),
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = viewModel.equipment.value.shortDescription,
                onValueChange = {
                    viewModel.setShortDescription(it)
                },
                placeholder = {
                    Text(text = "Short description")
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                ),
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = viewModel.equipment.value.fullDescription,
                onValueChange = {
                    viewModel.setFullDescription(it)
                },
                placeholder = {
                    Text(text = "Full description")
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                ),
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (updateId == "") {
                        viewModel.create()
                    } else {
                        viewModel.updateInfo(updateId)
                    }
                },
                shape = MaterialTheme.shapes.small,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    text = if (updateId == "") "Create equipment" else "Update Equipment Info",
                    textAlign = TextAlign.Center,
                    fontSize = 18.sp
                )
            }
        }
    }
}