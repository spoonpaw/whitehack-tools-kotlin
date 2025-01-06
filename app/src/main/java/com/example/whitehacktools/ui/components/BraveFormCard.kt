package com.example.whitehacktools.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.whitehacktools.model.BraveAbilities
import com.example.whitehacktools.model.BraveQuirk
import com.example.whitehacktools.model.BraveQuirkSlot
import com.example.whitehacktools.utilities.AdvancementTables

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BraveFormCard(
    characterClass: String,
    level: Int,
    braveAbilities: BraveAbilities,
    onBraveAbilitiesChanged: (BraveAbilities) -> Unit,
    modifier: Modifier = Modifier
) {
    if (characterClass == "Brave") {
        val availableSlots = remember(level) {
            when {
                level < 4 -> 1
                level < 7 -> 2
                level < 10 -> 3
                else -> 4
            }
        }

        // Initialize or resize quirks list if needed
        LaunchedEffect(availableSlots) {
            if (braveAbilities.quirkSlots.size < 4) {
                // Create a new list with 4 slots, copying over existing values
                val newQuirks = List(4) { index ->
                    if (index < braveAbilities.quirkSlots.size) {
                        braveAbilities.quirkSlots[index]
                    } else {
                        BraveQuirkSlot(null, "")
                    }
                }
                onBraveAbilitiesChanged(braveAbilities.copy(quirkSlots = newQuirks))
            }
        }

        SectionCard(
            title = "The Brave",
            modifier = modifier
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // Comeback Dice Section
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Comeback Dice",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Medium
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Number of Dice:",
                                style = MaterialTheme.typography.bodyMedium
                            )
                            
                            var textFieldValue by remember(braveAbilities.comebackDice) { 
                                mutableStateOf(if (braveAbilities.comebackDice == 0) "" else braveAbilities.comebackDice.toString()) 
                            }
                            
                            OutlinedTextField(
                                value = textFieldValue,
                                onValueChange = { newValue ->
                                    // Only allow numeric input
                                    if (newValue.isEmpty() || newValue.all { it.isDigit() }) {
                                        textFieldValue = newValue
                                        if (newValue.isNotEmpty()) {
                                            val newDice = newValue.toIntOrNull() ?: return@OutlinedTextField
                                            onBraveAbilitiesChanged(braveAbilities.copy(comebackDice = newDice))
                                        }
                                    }
                                },
                                modifier = Modifier
                                    .width(80.dp)
                                    .onFocusChanged { focusState ->
                                        if (!focusState.isFocused && textFieldValue.isEmpty()) {
                                            textFieldValue = "0"
                                            onBraveAbilitiesChanged(braveAbilities.copy(comebackDice = 0))
                                        }
                                    },
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Number
                                ),
                                singleLine = true,
                                label = { Text("Dice") }
                            )
                        }
                    }
                }

                // Say No Power Section
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Say No Power",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Medium
                            )
                            
                            Switch(
                                checked = braveAbilities.hasSayNoPower,
                                onCheckedChange = { isEnabled ->
                                    onBraveAbilitiesChanged(braveAbilities.copy(hasSayNoPower = isEnabled))
                                }
                            )
                            
                            Text(
                                text = if (braveAbilities.hasSayNoPower) "Available" else "Used",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }

                // Quirks Section
                Text(
                    text = "Quirks",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                repeat(availableSlots) { index ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "Quirk ${index + 1}",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Medium
                            )

                            val currentQuirk = braveAbilities.quirkSlots.getOrNull(index)?.quirk
                            var expanded by remember { mutableStateOf(false) }
                            
                            ExposedDropdownMenuBox(
                                expanded = expanded,
                                onExpandedChange = { expanded = it },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                OutlinedTextField(
                                    value = currentQuirk?.displayName ?: "",
                                    onValueChange = { },
                                    readOnly = true,
                                    label = { Text("Select a Quirk") },
                                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .menuAnchor()
                                )

                                ExposedDropdownMenu(
                                    expanded = expanded,
                                    onDismissRequest = { expanded = false }
                                ) {
                                    BraveQuirk.values().filter { quirk ->
                                        // Include if it's either the current quirk for this slot or not used in any slot
                                        quirk == currentQuirk || !braveAbilities.quirkSlots.take(availableSlots).any { slot -> slot.quirk == quirk }
                                    }.forEach { quirk ->
                                        DropdownMenuItem(
                                            text = {
                                                Column {
                                                    Text(quirk.displayName)
                                                    Text(
                                                        text = quirk.description,
                                                        style = MaterialTheme.typography.bodySmall,
                                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                                    )
                                                }
                                            },
                                            onClick = {
                                                onBraveAbilitiesChanged(braveAbilities.setQuirk(
                                                    index, 
                                                    quirk, 
                                                    braveAbilities.quirkSlots.getOrNull(index)?.protectedAllyName ?: ""
                                                ))
                                                expanded = false
                                            }
                                        )
                                    }
                                }
                            }

                            if (currentQuirk != null) {
                                Text(
                                    text = currentQuirk.description,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                )
                            }

                            if (currentQuirk == BraveQuirk.PROTECT_ALLY) {
                                OutlinedTextField(
                                    value = braveAbilities.quirkSlots.getOrNull(index)?.protectedAllyName ?: "",
                                    onValueChange = { newName ->
                                        onBraveAbilitiesChanged(braveAbilities.setQuirk(index, currentQuirk, newName))
                                    },
                                    label = { Text("Protected Ally Name") },
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
