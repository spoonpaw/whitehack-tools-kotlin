package com.netartisancollective.whitehacktools.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.netartisancollective.whitehacktools.model.Gear

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GearEditRow(
    gear: Gear,
    onSave: (Gear) -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    var name by remember { mutableStateOf(gear.name) }
    var quantityString by remember { mutableStateOf(gear.quantity.toString()) }
    var weight by remember { mutableStateOf(gear.weight) }
    var special by remember { mutableStateOf(gear.special) }
    var isEquipped by remember { mutableStateOf(gear.isEquipped) }
    var isStashed by remember { mutableStateOf(gear.isStashed) }
    var isMagical by remember { mutableStateOf(gear.isMagical) }
    var isCursed by remember { mutableStateOf(gear.isCursed) }
    var isContainer by remember { mutableStateOf(gear.isContainer) }
    var weightExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Name and Qty row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )

                OutlinedTextField(
                    value = quantityString,
                    onValueChange = { input ->
                        if (input.isEmpty() || (input.all { it.isDigit() } && input.toIntOrNull()?.let { it in 1..99 } == true)) {
                            quantityString = input
                        }
                    },
                    label = { Text("Qty") },
                    modifier = Modifier.width(80.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true
                )
            }

            // Weight dropdown
            ExposedDropdownMenuBox(
                expanded = weightExpanded,
                onExpandedChange = { weightExpanded = it }
            ) {
                OutlinedTextField(
                    value = weight,
                    onValueChange = { },
                    readOnly = true,
                    label = { Text("Weight") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = weightExpanded) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )

                ExposedDropdownMenu(
                    expanded = weightExpanded,
                    onDismissRequest = { weightExpanded = false }
                ) {
                    listOf("No size", "Minor", "Regular", "Heavy").forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                weight = option
                                weightExpanded = false
                            }
                        )
                    }
                }
            }

            // Special
            OutlinedTextField(
                value = special,
                onValueChange = { special = it },
                label = { Text("Special") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Status Toggles in a grid
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // First row
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Left cell (Equipped)
                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(
                                checked = isEquipped,
                                onCheckedChange = { checked ->
                                    isEquipped = checked
                                    if (checked) {
                                        isStashed = false
                                    }
                                }
                            )
                            Text("Equipped")
                        }
                    }
                    
                    // Right cell (Magical)
                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(
                                checked = isMagical,
                                onCheckedChange = { isMagical = it }
                            )
                            Text("Magical")
                        }
                    }
                }

                // Second row
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Left cell (Stashed)
                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(
                                checked = isStashed,
                                onCheckedChange = { checked ->
                                    isStashed = checked
                                    if (checked) {
                                        isEquipped = false
                                    }
                                }
                            )
                            Text("Stashed")
                        }
                    }
                    
                    // Right cell (Cursed)
                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(
                                checked = isCursed,
                                onCheckedChange = { isCursed = it }
                            )
                            Text("Cursed")
                        }
                    }
                }

                // Third row
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Left cell (Container)
                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(
                                checked = isContainer,
                                onCheckedChange = { isContainer = it }
                            )
                            Text("Container")
                        }
                    }
                    
                    // Right cell (empty for alignment)
                    Box(
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Action buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onCancel,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Cancel")
                }

                Button(
                    onClick = {
                        onSave(
                            gear.copy(
                                name = name,
                                quantity = quantityString.toIntOrNull() ?: 1,
                                weight = weight,
                                special = special,
                                isEquipped = isEquipped,
                                isStashed = isStashed,
                                isMagical = isMagical,
                                isCursed = isCursed,
                                isContainer = isContainer
                            )
                        )
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Save")
                }
            }
        }
    }
}
