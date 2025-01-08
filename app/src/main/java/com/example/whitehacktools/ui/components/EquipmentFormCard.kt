package com.example.whitehacktools.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.whitehacktools.model.Gear
import com.example.whitehacktools.model.GearDataset

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EquipmentFormCard(
    gear: List<Gear>,
    onGearChange: (List<Gear>) -> Unit,
    modifier: Modifier = Modifier
) {
    var editingGear by remember { mutableStateOf<Gear?>(null) }
    var isAddingNew by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }

    SectionCard(
        title = "Equipment",
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Show "No equipment" text when list is empty
            if (gear.isEmpty() && !isAddingNew) {
                Text(
                    text = "No equipment added",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    textAlign = TextAlign.Center
                )
            }

            // Existing gear
            gear.forEach { gearItem ->
                if (editingGear?.id == gearItem.id) {
                    GearEditRow(
                        gear = gearItem,
                        onSave = { updatedGear ->
                            onGearChange(gear.map { 
                                if (it.id == updatedGear.id) updatedGear else it 
                            })
                            editingGear = null
                        },
                        onCancel = {
                            editingGear = null
                        }
                    )
                } else {
                    GearRow(
                        gear = gearItem,
                        onEdit = { editingGear = gearItem },
                        onDelete = {
                            onGearChange(gear.filterNot { it.id == gearItem.id })
                        }
                    )
                }
            }

            // Add new gear form
            if (isAddingNew) {
                GearEditRow(
                    gear = editingGear ?: Gear(),
                    onSave = { newGear ->
                        onGearChange(gear + newGear)
                        isAddingNew = false
                        editingGear = null
                    },
                    onCancel = {
                        isAddingNew = false
                        editingGear = null
                    }
                )
            }

            // Add gear button with dropdown
            if (!isAddingNew && editingGear == null) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Button(
                        onClick = { expanded = true },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Add Equipment")
                    }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier.fillMaxWidth(0.9f)
                    ) {
                        GearDataset.standardGear.forEach { standardGear ->
                            DropdownMenuItem(
                                text = { Text(standardGear.name) },
                                onClick = {
                                    expanded = false
                                    onGearChange(gear + standardGear.copy())
                                }
                            )
                        }
                        Divider()
                        DropdownMenuItem(
                            text = { Text("Custom Equipment") },
                            onClick = {
                                expanded = false
                                isAddingNew = true
                                editingGear = null
                            }
                        )
                    }
                }
            }
        }
    }
}
