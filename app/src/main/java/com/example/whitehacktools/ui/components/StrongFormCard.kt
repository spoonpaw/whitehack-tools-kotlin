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
import kotlinx.coroutines.launch

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
            AdvancementTables.stats("Strong", level).slots
        }

        // Initialize or resize slots list if needed
        LaunchedEffect(availableSlots) {
            if (strongCombatOptions.slots.size < 4) {
                // Create a new list with 4 slots, copying over existing values
                val newSlots = List(4) { index ->
                    if (index < strongCombatOptions.slots.size) {
                        strongCombatOptions.slots[index]
                    } else {
                        null
                    }
                }
                onStrongCombatOptionsChanged(StrongCombatOptions(_slots = newSlots))
            }
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
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Combat Options",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )

                    Text(
                        text = "Available Slots: $availableSlots",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
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
                                    text = "Slot ${index + 1}",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Medium
                                )

                                val currentSlot = strongCombatOptions.slots[index]
                                var expanded by remember { mutableStateOf(false) }
                                
                                ExposedDropdownMenuBox(
                                    expanded = expanded,
                                    onExpandedChange = { expanded = it },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    OutlinedTextField(
                                        value = currentSlot?.toString() ?: "Select a Slot",
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
                                        StrongCombatOption.values().forEach { option ->
                                            DropdownMenuItem(
                                                text = {
                                                    Column {
                                                        Text(option.toString())
                                                        Text(
                                                            text = option.description,
                                                            style = MaterialTheme.typography.bodySmall,
                                                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                                        )
                                                    }
                                                },
                                                onClick = {
                                                    val updatedSlots = strongCombatOptions.slots.toMutableList()
                                                    
                                                    // If this option is selected in a hidden slot, clear that slot
                                                    val hiddenIndex = updatedSlots
                                                        .drop(availableSlots)
                                                        .indexOfFirst { it == option }
                                                    if (hiddenIndex != -1) {
                                                        updatedSlots[hiddenIndex + availableSlots] = null
                                                    }
                                                    
                                                    // Set the new option for this slot
                                                    updatedSlots[index] = option
                                                    
                                                    onStrongCombatOptionsChanged(StrongCombatOptions(_slots = updatedSlots))
                                                    expanded = false
                                                }
                                            )
                                        }
                                    }
                                }

                                if (currentSlot != null) {
                                    Text(
                                        text = currentSlot.description,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                    )
                                }
                            }
                        }
                    }
                }

                // Conflict Loot Section
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Current Conflict Loot",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            val currentLoot = currentConflictLoot ?: ConflictLoot()
                            var usesText by remember { mutableStateOf(currentLoot.usesRemaining.toString()) }

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

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = currentOption?.displayName ?: "",
            onValueChange = {},
            readOnly = true,
            placeholder = { Text("Select combat option") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            availableOptions.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option.displayName) },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }
                )
            }
            // Add "None" option
            DropdownMenuItem(
                text = { Text("None") },
                onClick = {
                    onOptionSelected(null)
                    expanded = false
                }
            )
        }
    }

    if (currentOption != null) {
        Text(
            text = currentOption.description,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
    }
}
