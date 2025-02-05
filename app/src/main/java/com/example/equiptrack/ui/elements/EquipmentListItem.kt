package com.example.equiptrack.ui.elements

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.equiptrack.R
import com.example.equiptrack.data.dto.EquipmentShort


@Composable
fun EquipmentItem(
    equipment: EquipmentShort,
    navigate: (String) -> Unit,
    showReserved: Boolean = true
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = {
                navigate("equipment/${equipment.id}")
            }),

//        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = equipment.name,
                style = MaterialTheme.typography.titleLarge .copy(
                    fontWeight = FontWeight.Bold
                )
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = equipment.shortDescription,
                style = MaterialTheme.typography.bodyMedium
            )
            if (showReserved) {
                Spacer(modifier = Modifier.height(8.dp))

                val statusColor = when (equipment.reserved) {
                    true -> MaterialTheme.colorScheme.error
                    false -> MaterialTheme.colorScheme.onSurfaceVariant
                    null -> Color.DarkGray
                }
                Text(
                    text = when (equipment.reserved) {
                        true -> stringResource(R.string.equip_reserved)
                        false -> stringResource(R.string.equip_available)
                        null -> stringResource(R.string.equip_available_err)
                    },
                    color = statusColor,
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewEquipmentItem() {
    MaterialTheme {
        EquipmentItem(
            equipment = EquipmentShort(
                name = "Thing",
                shortDescription = "A very useful thing.",
                reserved = true,
                id = ""
            ),
            navigate = {},
//            false
        )
    }
}
