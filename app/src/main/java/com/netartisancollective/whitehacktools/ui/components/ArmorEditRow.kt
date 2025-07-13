package com.netartisancollective.whitehacktools.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.netartisancollective.whitehacktools.model.Armor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArmorEditRow(
    armor: Armor,
    onSave: (Armor) -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    var name by remember { mutableStateOf(armor.name) }
    var quantityString by remember { mutableStateOf(armor.quantity.toString()) }
    var df by remember { mutableStateOf(armor.df.toString()) }
    var weight by remember { mutableStateOf(armor.weight.toString()) }
    var special by remember { mutableStateOf(armor.special) }
    var isShield by remember { mutableStateOf(armor.isShield) }
    var isEquipped by remember { mutableStateOf(armor.isEquipped) }
    var isStashed by remember { mutableStateOf(armor.isStashed) }
    var isMagical by remember { mutableStateOf(armor.isMagical) }
    var isCursed by remember { mutableStateOf(armor.isCursed) }
    var modifierString by remember { mutableStateOf(armor.bonus.toString()) }
    var modifierFieldFocused by remember { mutableStateOf(false) }

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

            // Stats in vertical stack
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Defense
                OutlinedTextField(
                    value = df,
                    onValueChange = { input ->
                        if (input.isEmpty() || input.all { it.isDigit() }) {
                            df = input
                        }
                    },
                    label = { Text("Defense") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                // Weight
                OutlinedTextField(
                    value = weight,
                    onValueChange = { input ->
                        if (input.isEmpty() || input.all { it.isDigit() }) {
                            weight = input
                        }
                    },
                    label = { Text("Weight") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                // Modifier
                OutlinedTextField(
                    value = modifierString,
                    onValueChange = { input ->
                        if (input.isEmpty() || input == "-" || input.toIntOrNull() != null) {
                            modifierString = input
                        }
                    },
                    label = { Text("Modifier") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .onFocusChanged { focusState ->
                            if (!focusState.isFocused && modifierString == "-") {
                                modifierString = "0"
                            }
                            modifierFieldFocused = focusState.isFocused
                        },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                // Special
                OutlinedTextField(
                    value = special,
                    onValueChange = { special = it },
                    label = { Text("Special") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }

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
                    // Left cell (Shield)
                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(
                                checked = isShield,
                                onCheckedChange = { isShield = it }
                            )
                            Text("Shield")
                        }
                    }
                    
                    // Empty right cell
                    Box(
                        modifier = Modifier.weight(1f)
                    ) { }
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
                        val bonus = modifierString.toIntOrNull() ?: 0
                        onSave(armor.copy(
                            name = name,
                            quantity = quantityString.toIntOrNull() ?: 1,
                            df = df.toIntOrNull() ?: 0,
                            weight = weight.toIntOrNull() ?: 1,
                            special = special,
                            isShield = isShield,
                            isEquipped = isEquipped,
                            isStashed = isStashed,
                            isMagical = isMagical,
                            isCursed = isCursed,
                            bonus = bonus
                        ))
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Save")
                }
            }
        }
    }
}
