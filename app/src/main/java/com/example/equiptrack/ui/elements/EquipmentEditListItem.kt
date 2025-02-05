package com.example.equiptrack.ui.elements

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.ModeEdit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.equiptrack.R
import com.example.equiptrack.data.dto.EquipmentShort

@Composable
fun EquipmentEditableItem(
    equipment: EquipmentShort,
    navigateToEdit: (String) -> Unit,
    delete: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
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
            Row {
                Button(
                    onClick = {
                        navigateToEdit(equipment.id)
                    },
                    shape = MaterialTheme.shapes.extraSmall,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Icon(Icons.Outlined.ModeEdit, contentDescription = "Edit user")
                }

                Spacer(modifier = Modifier.width(5.dp))

                Button(
                    onClick = {
                        delete(equipment.id)
                    },
                    shape = MaterialTheme.shapes.extraSmall,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Icon(Icons.Outlined.Delete, contentDescription = "Delete user")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewEquipmentEditableItem() {
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