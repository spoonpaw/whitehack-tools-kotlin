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
import androidx.compose.ui.unit.dp
import com.example.whitehacktools.model.*
import com.example.whitehacktools.utilities.AdvancementTables

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StrongFormCard(
    characterClass: String,
    level: Int,
    strongCombatOptions: StrongCombatOptions,
    currentConflictLoot: ConflictLoot?,
    onStrongCombatOptionsChanged: (StrongCombatOptions) -> Unit,
    onConflictLootChanged: (ConflictLoot?) -> Unit,
    modifier: Modifier = Modifier
) {
    if (characterClass == "Strong") {
        val availableSlots = remember(level) {
            AdvancementTables.stats(characterClass, level).slots
        }

        SectionCard(
            title = "The Strong",
            modifier = modifier
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // Combat Options Section
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Combat Options",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )

                        repeat(availableSlots) { slotIndex ->
                            CombatOptionSlot(
                                slotIndex = slotIndex,
                                currentOption = strongCombatOptions.getOption(slotIndex),
                                availableOptions = StrongCombatOption.values().filter { option ->
                                    !strongCombatOptions.isActive(option) || strongCombatOptions.getOption(slotIndex) == option
                                },
                                onOptionSelected = { option ->
                                    onStrongCombatOptionsChanged(strongCombatOptions.setOption(option, slotIndex))
                                }
                            )
                        }
                    }
                }

                // Conflict Loot Section
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Current Conflict Loot",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )

                        val currentLoot = currentConflictLoot ?: ConflictLoot()
                        var usesText by remember { mutableStateOf(currentLoot.usesRemaining.toString()) }

                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            OutlinedTextField(
                                value = currentLoot.keyword,
                                onValueChange = { 
                                    onConflictLootChanged(currentLoot.copy(keyword = it))
                                },
                                label = { Text("Keyword") },
                                modifier = Modifier.fillMaxWidth()
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                ConflictLootType.values().forEach { type ->
                                    FilterChip(
                                        selected = currentLoot.type == type,
                                        onClick = { 
                                            onConflictLootChanged(currentLoot.copy(type = type))
                                        },
                                        label = { Text(type.toString()) }
                                    )
                                }
                            }

                            OutlinedTextField(
                                value = usesText,
                                onValueChange = { value ->
                                    if (value.isEmpty()) {
                                        usesText = value
                                    } else {
                                        value.toIntOrNull()?.let { number ->
                                            if (number in 0..level) {
                                                usesText = number.toString()
                                                onConflictLootChanged(currentLoot.copy(usesRemaining = number))
                                            }
                                        }
                                    }
                                },
                                label = { Text("Uses Remaining (0-$level)") },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .onFocusChanged { focusState ->
                                        if (!focusState.isFocused && usesText.isEmpty()) {
                                            usesText = "0"
                                            onConflictLootChanged(currentLoot.copy(usesRemaining = 0))
                                        }
                                    }
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CombatOptionSlot(
    slotIndex: Int,
    currentOption: StrongCombatOption?,
    availableOptions: List<StrongCombatOption>,
    onOptionSelected: (StrongCombatOption?) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Slot ${slotIndex + 1}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = currentOption?.toString() ?: "Select Option",
                onValueChange = { },
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                DropdownMenuItem(
                    text = { Text("None") },
                    onClick = {
                        onOptionSelected(null)
                        expanded = false
                    }
                )
                
                availableOptions.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option.toString()) },
                        onClick = {
                            onOptionSelected(option)
                            expanded = false
                        }
                    )
                }
            }
        }

        if (currentOption != null) {
            Text(
                text = currentOption.description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}
