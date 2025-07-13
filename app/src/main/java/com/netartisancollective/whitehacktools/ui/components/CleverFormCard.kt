package com.netartisancollective.whitehacktools.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.netartisancollective.whitehacktools.model.*
import com.netartisancollective.whitehacktools.utilities.AdvancementTables

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CleverFormCard(
    characterClass: String,
    level: Int,
    cleverKnackOptions: CleverKnackOptions,
    onCleverKnackOptionsChanged: (CleverKnackOptions) -> Unit,
    modifier: Modifier = Modifier
) {
    if (characterClass == "Clever") {
        val availableSlots = remember(level) {
            AdvancementTables.stats("Clever", level).slots
        }

        // Initialize or resize knacks list if needed
        LaunchedEffect(availableSlots) {
            if (cleverKnackOptions.slots.size < 4) {
                val newSlots = List(4) { index ->
                    if (index < cleverKnackOptions.slots.size) {
                        cleverKnackOptions.slots[index]
                    } else {
                        CleverKnackSlot(null, false)
                    }
                }
                onCleverKnackOptionsChanged(CleverKnackOptions(slots = newSlots))
            }
        }

        SectionCard(
            title = "The Clever",
            modifier = modifier
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Unorthodox Bonus Switch
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Used Unorthodox Bonus:",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Switch(
                        checked = cleverKnackOptions.hasUsedUnorthodoxBonus,
                        onCheckedChange = { checked ->
                            onCleverKnackOptionsChanged(cleverKnackOptions.copy(hasUsedUnorthodoxBonus = checked))
                        }
                    )
                }

                // Knacks
                Text(
                    text = "Knacks",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                cleverKnackOptions.slots.take(availableSlots).forEachIndexed { index, slot ->
                    var expanded by remember { mutableStateOf(false) }

                    Column {
                        ExposedDropdownMenuBox(
                            expanded = expanded,
                            onExpandedChange = { expanded = it }
                        ) {
                            OutlinedTextField(
                                value = slot.knack?.displayName ?: "Select a Knack",
                                onValueChange = {},
                                readOnly = true,
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                                modifier = Modifier
                                    .menuAnchor()
                                    .fillMaxWidth()
                            )

                            ExposedDropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                CleverKnack.values().forEach { knack ->
                                    if (!isKnackActive(cleverKnackOptions, knack) || knack == slot.knack) {
                                        DropdownMenuItem(
                                            text = { Text(knack.displayName) },
                                            onClick = {
                                                val newSlots = cleverKnackOptions.slots.toMutableList()
                                                newSlots[index] = CleverKnackSlot(knack = knack, hasUsedCombatDie = false)
                                                onCleverKnackOptionsChanged(CleverKnackOptions(
                                                    slots = newSlots,
                                                    hasUsedUnorthodoxBonus = cleverKnackOptions.hasUsedUnorthodoxBonus
                                                ))
                                                expanded = false
                                            }
                                        )
                                    }
                                }
                                // Add option to clear selection
                                DropdownMenuItem(
                                    text = { Text("Clear Selection") },
                                    onClick = {
                                        val newSlots = cleverKnackOptions.slots.toMutableList()
                                        newSlots[index] = CleverKnackSlot(knack = null, hasUsedCombatDie = false)
                                        onCleverKnackOptionsChanged(CleverKnackOptions(
                                            slots = newSlots,
                                            hasUsedUnorthodoxBonus = cleverKnackOptions.hasUsedUnorthodoxBonus
                                        ))
                                        expanded = false
                                    }
                                )
                            }
                        }

                        if (slot.knack != null) {
                            // Combat Die Usage Switch
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Used Combat Die:",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Switch(
                                    checked = slot.hasUsedCombatDie,
                                    onCheckedChange = { checked ->
                                        val newSlots = cleverKnackOptions.slots.toMutableList()
                                        newSlots[index] = slot.copy(hasUsedCombatDie = checked)
                                        onCleverKnackOptionsChanged(CleverKnackOptions(
                                            slots = newSlots,
                                            hasUsedUnorthodoxBonus = cleverKnackOptions.hasUsedUnorthodoxBonus
                                        ))
                                    }
                                )
                            }

                            Text(
                                text = slot.knack.description,
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.padding(top = 4.dp, bottom = 8.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun isKnackActive(cleverKnackOptions: CleverKnackOptions, knack: CleverKnack): Boolean =
    cleverKnackOptions.slots.any { it.knack == knack }
