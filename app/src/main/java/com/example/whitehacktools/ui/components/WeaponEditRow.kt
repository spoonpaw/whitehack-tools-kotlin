package com.example.whitehacktools.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.whitehacktools.model.Weapon
import kotlin.math.abs

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeaponEditRow(
    weapon: Weapon,
    onSave: (Weapon) -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    var name by remember { mutableStateOf(weapon.name) }
    var quantityString by remember { mutableStateOf(weapon.quantity.toString()) }
    var damage by remember { mutableStateOf(weapon.damage) }
    var weight by remember { mutableStateOf(weapon.weight) }
    var range by remember { mutableStateOf(weapon.range) }
    var rateOfFire by remember { mutableStateOf(weapon.rateOfFire) }
    var special by remember { mutableStateOf(weapon.special) }
    var isEquipped by remember { mutableStateOf(weapon.isEquipped) }
    var isStashed by remember { mutableStateOf(weapon.isStashed) }
    var isMagical by remember { mutableStateOf(weapon.isMagical) }
    var isCursed by remember { mutableStateOf(weapon.isCursed) }
    var modifierString by remember { mutableStateOf(weapon.bonus.toString()) }
    var modifierFieldFocused by remember { mutableStateOf(false) }
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
            // Name and Quantity
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

            // Status Toggles
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(horizontalAlignment = Alignment.Start) {
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
                
                Column(horizontalAlignment = Alignment.Start) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = isMagical,
                            onCheckedChange = { isMagical = it }
                        )
                        Text("Magical")
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked = isCursed,
                            onCheckedChange = { isCursed = it }
                        )
                        Text("Cursed")
                    }
                }
            }

            // Weight Dropdown
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

            // Combat Stats
            OutlinedTextField(
                value = damage,
                onValueChange = { damage = it },
                label = { Text("Damage") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = range,
                onValueChange = { range = it },
                label = { Text("Range") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = rateOfFire,
                onValueChange = { rateOfFire = it },
                label = { Text("Rate of Fire") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Special Properties
            OutlinedTextField(
                value = special,
                onValueChange = { special = it },
                label = { Text("Special Properties") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            // Modifier Field
            OutlinedTextField(
                value = modifierString,
                onValueChange = { input ->
                    // Allow empty string, minus sign, and numbers
                    if (input.isEmpty() || input == "-" || input.matches(Regex("^-?\\d*$"))) {
                        modifierString = input
                    }
                },
                label = { 
                    Text(if (modifierString.toIntOrNull()?.let { it < 0 } == true) "Penalty" else "Bonus") 
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged { focusState -> 
                        val wasFocused = modifierFieldFocused
                        modifierFieldFocused = focusState.isFocused
                        if (wasFocused && !focusState.isFocused && (modifierString.isEmpty() || modifierString == "-")) {
                            modifierString = "0"
                        }
                    },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true
            )

            // Action Buttons
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
                        // Ensure we have a valid number before saving
                        if (modifierString.isEmpty() || modifierString == "-") {
                            modifierString = "0"
                        }
                        
                        onSave(
                            weapon.copy(
                                name = name,
                                quantity = quantityString.toIntOrNull() ?: 1,
                                damage = damage,
                                weight = weight,
                                range = range,
                                rateOfFire = rateOfFire,
                                special = special,
                                isEquipped = isEquipped,
                                isStashed = isStashed,
                                isMagical = isMagical,
                                isCursed = isCursed,
                                bonus = modifierString.toIntOrNull() ?: 0
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
