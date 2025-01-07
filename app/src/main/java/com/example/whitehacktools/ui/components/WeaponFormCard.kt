package com.example.whitehacktools.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.whitehacktools.model.Weapon
import com.example.whitehacktools.model.WeaponDataset

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeaponFormCard(
    weapons: List<Weapon>,
    onWeaponsChange: (List<Weapon>) -> Unit,
    modifier: Modifier = Modifier
) {
    var editingWeapon by remember { mutableStateOf<Weapon?>(null) }
    var isAddingNew by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }

    SectionCard(
        title = "Weapons",
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Show "No weapons" text when list is empty
            if (weapons.isEmpty() && !isAddingNew) {
                Text(
                    text = "No weapons added",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    textAlign = TextAlign.Center
                )
            }

            // Existing weapons
            weapons.forEach { weapon ->
                if (editingWeapon?.id == weapon.id) {
                    WeaponEditRow(
                        weapon = weapon,
                        onSave = { updatedWeapon ->
                            onWeaponsChange(weapons.map { 
                                if (it.id == updatedWeapon.id) updatedWeapon else it 
                            })
                            editingWeapon = null
                        },
                        onCancel = {
                            editingWeapon = null
                        }
                    )
                } else {
                    WeaponRow(
                        weapon = weapon,
                        onEdit = { editingWeapon = weapon },
                        onDelete = {
                            onWeaponsChange(weapons.filterNot { it.id == weapon.id })
                        }
                    )
                }
            }

            // Add new weapon form
            if (isAddingNew) {
                WeaponEditRow(
                    weapon = editingWeapon ?: Weapon(),
                    onSave = { newWeapon ->
                        onWeaponsChange(weapons + newWeapon)
                        isAddingNew = false
                        editingWeapon = null
                    },
                    onCancel = {
                        isAddingNew = false
                        editingWeapon = null
                    }
                )
            }

            // Add weapon button with dropdown
            if (!isAddingNew && editingWeapon == null) {
                Box(modifier = Modifier.fillMaxWidth()) {
                    Button(
                        onClick = { expanded = true },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Add Weapon")
                    }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier.fillMaxWidth(0.9f)
                    ) {
                        WeaponDataset.standardWeapons.forEach { weapon ->
                            DropdownMenuItem(
                                text = { Text(weapon.name) },
                                onClick = {
                                    expanded = false
                                    onWeaponsChange(weapons + weapon.copy())
                                }
                            )
                        }
                        Divider()
                        DropdownMenuItem(
                            text = { Text("Custom Weapon") },
                            onClick = {
                                expanded = false
                                isAddingNew = true
                                editingWeapon = null
                            }
                        )
                    }
                }
            }
        }
    }
}
