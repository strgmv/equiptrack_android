package com.example.equiptrack

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import com.example.equiptrack.ui.base.EquipTrackNavHost
import com.example.equiptrack.ui.theme.jetNewsSample.JetnewsTheme

@Composable
fun EtApp(onToggleTheme: () -> Unit, darkTheme: Boolean) {
    JetnewsTheme ( darkTheme ) {
        Surface(color = MaterialTheme.colorScheme.background) {
            EquipTrackNavHost()
        }
    }
}