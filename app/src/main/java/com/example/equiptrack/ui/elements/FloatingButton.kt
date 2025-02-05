package com.example.equiptrack.ui.elements

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun FloatingAddButton(
    onClick: () -> Unit,
    icon: ImageVector) {
    SmallFloatingActionButton(
        onClick = { onClick() },
        shape = MaterialTheme.shapes.small,
        containerColor = MaterialTheme.colorScheme.secondaryContainer,
        contentColor = MaterialTheme.colorScheme.secondary
    ) {
        Icon(icon, "")
    }
}

@Composable
fun FloatingQRButton(onClick: () -> Unit) {
    SmallFloatingActionButton(
        onClick = { onClick() },
        shape = MaterialTheme.shapes.small,
        containerColor = MaterialTheme.colorScheme.secondaryContainer,
        contentColor = MaterialTheme.colorScheme.secondary
    ) {
        Icon(Icons.Filled.QrCode, "Small floating action button.")
    }
}