package com.example.whitehacktools.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.whitehacktools.model.CleverAbilities
import com.example.whitehacktools.model.CleverKnack
import com.example.whitehacktools.utilities.AdvancementTables

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CleverFormCard(
    characterClass: String,
    level: Int,
    cleverAbilities: CleverAbilities,
    onCleverAbilitiesChanged: (CleverAbilities) -> Unit,
    modifier: Modifier = Modifier
) {
    if (characterClass == "Clever") {
        val availableSlots by remember(level) {
            mutableStateOf(AdvancementTables.stats("Clever", level).slots)
        }

        // Only update for level if we don't have any knacks yet or if we have too many knacks
        LaunchedEffect(level) {
            if (cleverAbilities.knackSlots.isEmpty() || cleverAbilities.knackSlots.size > availableSlots) {
                onCleverAbilitiesChanged(cleverAbilities.updateForLevel(level))
            }
        }

        SectionCard(
            title = "The Clever",
            modifier = modifier
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Unorthodox Solution Section
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
                                text = "Unorthodox Solution",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Medium
                            )
                            
                            Switch(
                                checked = !cleverAbilities.hasUsedUnorthodoxBonus,
                                onCheckedChange = { isAvailable ->
                                    onCleverAbilitiesChanged(cleverAbilities.copy(hasUsedUnorthodoxBonus = !isAvailable))
                                }
                            )
                            
                            Text(
                                text = if (!cleverAbilities.hasUsedUnorthodoxBonus) "Available" else "Used",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }

                // Knacks Section
                Text(
                    text = "Knacks",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )

                // Get all currently selected knacks
                val selectedKnacks = cleverAbilities.knackSlots.mapNotNull { it.knack }.toSet()

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
                                text = "Knack ${index + 1}",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Medium
                            )

                            val currentKnack = cleverAbilities.knackSlots.getOrNull(index)?.knack
                            var expanded by remember { mutableStateOf(false) }
                            
                            ExposedDropdownMenuBox(
                                expanded = expanded,
                                onExpandedChange = { newExpanded -> 
                                    // If it's already expanded and we click it again, close it
                                    expanded = if (expanded && newExpanded) false else newExpanded
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                OutlinedTextField(
                                    value = currentKnack?.displayName ?: "",
                                    onValueChange = { },
                                    readOnly = true,
                                    label = { Text("Select a Knack") },
                                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .menuAnchor()
                                )

                                ExposedDropdownMenu(
                                    expanded = expanded,
                                    onDismissRequest = { expanded = false }
                                ) {
                                    CleverKnack.values().filter { knack ->
                                        // Include if it's either the current knack for this slot or not used in any slot
                                        knack == currentKnack || !selectedKnacks.contains(knack)
                                    }.forEach { knack ->
                                        DropdownMenuItem(
                                            text = { 
                                                Column {
                                                    Text(knack.displayName)
                                                    Text(
                                                        text = knack.description,
                                                        style = MaterialTheme.typography.bodySmall
                                                    )
                                                }
                                            },
                                            onClick = {
                                                onCleverAbilitiesChanged(cleverAbilities.setKnack(knack, index))
                                                expanded = false
                                            }
                                        )
                                    }
                                }
                            }

                            // Show description if a knack is selected
                            currentKnack?.let { knack ->
                                Text(
                                    text = knack.description,
                                    style = MaterialTheme.typography.bodySmall,
                                    modifier = Modifier.padding(top = 8.dp)
                                )
                            }

                            // Combat Exploiter Switch
                            if (currentKnack == CleverKnack.COMBAT_EXPLOITER) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 8.dp),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Combat Die",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    
                                    Switch(
                                        checked = !cleverAbilities.knackSlots[index].hasUsedCombatDie,
                                        onCheckedChange = { isAvailable ->
                                            onCleverAbilitiesChanged(cleverAbilities.setHasUsedCombatDie(!isAvailable, index))
                                        }
                                    )
                                    
                                    Text(
                                        text = if (!cleverAbilities.knackSlots[index].hasUsedCombatDie) "Available" else "Used",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
