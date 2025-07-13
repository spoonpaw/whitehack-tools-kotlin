package com.netartisancollective.whitehacktools.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.netartisancollective.whitehacktools.model.Armor
import com.netartisancollective.whitehacktools.model.ArmorData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArmorFormCard(
    armor: List<Armor>,
    onArmorChange: (List<Armor>) -> Unit,
    modifier: Modifier = Modifier
) {
    var editingArmor by remember { mutableStateOf<Armor?>(null) }
    var isAddingNew by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }

    SectionCard(
        title = "Armor",
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Show "No armor" text when list is empty
            if (armor.isEmpty() && !isAddingNew) {
                Text(
                    text = "No armor added",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    textAlign = TextAlign.Center
                )
            }

            // Existing armor
            armor.forEach { armorItem ->
                if (editingArmor?.id == armorItem.id) {
                    ArmorEditRow(
                        armor = armorItem,
                        onSave = { updatedArmor ->
                            onArmorChange(armor.map { 
                                if (it.id == updatedArmor.id) updatedArmor else it 
                            })
                            editingArmor = null
                        },
                        onCancel = {
                            editingArmor = null
                        }
                    )
                } else {
                    ArmorRow(
                        armor = armorItem,
                        onEdit = { editingArmor = armorItem },
                        onDelete = {
                            onArmorChange(armor.filterNot { it.id == armorItem.id })
                        }
                    )
                }
            }

            // Add new armor form
            if (isAddingNew) {
                ArmorEditRow(
                    armor = editingArmor ?: Armor(),
                    onSave = { newArmor ->
                        onArmorChange(armor + newArmor)
                        isAddingNew = false
                        editingArmor = null
                    },
                    onCancel = {
                        isAddingNew = false
                        editingArmor = null
                    }
                )
            }

            // Add armor button with dropdown
            if (!isAddingNew && editingArmor == null) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Button(
                        onClick = { expanded = true },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Add Armor")
                    }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier.fillMaxWidth(0.9f)
                    ) {
                        // Add predefined armor options
                        ArmorData.armors.forEach { predefinedArmor ->
                            DropdownMenuItem(
                                text = { Text(predefinedArmor.name) },
                                onClick = {
                                    expanded = false
                                    onArmorChange(armor + predefinedArmor.copy())
                                }
                            )
                        }
                        // Add divider before custom option
                        Divider()
                        // Custom armor option
                        DropdownMenuItem(
                            text = { Text("Custom Armor") },
                            onClick = {
                                expanded = false
                                isAddingNew = true
                                editingArmor = null
                            }
                        )
                    }
                }
            }
        }
    }
}
