package com.example.whitehacktools.ui.components

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.whitehacktools.model.*
import com.example.whitehacktools.utilities.AdvancementTables

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeftFormCard(
    character: PlayerCharacter,
    onCharacterChange: (PlayerCharacter) -> Unit,
    modifier: Modifier = Modifier
) {
    val stats = AdvancementTables.stats("Deft", character.level)
    val availableSlots = stats.slots

    SectionCard(
        title = "The Deft",
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            character.attunementSlots.take(availableSlots).forEachIndexed { index, slot ->
                AttunementSlotCard(
                    slot = slot,
                    index = index,
                    isFirstSlot = index == 0,
                    onSlotChange = { updatedSlot ->
                        val updatedSlots = character.attunementSlots.toMutableList()
                        updatedSlots[index] = updatedSlot
                        onCharacterChange(character.copy(attunementSlots = updatedSlots))
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun AttunementSlotCard(
    slot: AttunementSlot,
    index: Int,
    isFirstSlot: Boolean,
    onSlotChange: (AttunementSlot) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.05f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Attunement Slot ${index + 1}",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
            )
            
            AttunementField(
                title = "Primary Attunement",
                titleColor = MaterialTheme.colorScheme.primary,
                attunement = slot.primaryAttunement,
                onAttunementChange = { updatedAttunement ->
                    var updatedSlot = slot.copy(primaryAttunement = updatedAttunement)
                    if (updatedAttunement.isActive) {
                        // Deactivate all other attunements
                        updatedSlot = updatedSlot.copy(
                            secondaryAttunement = updatedSlot.secondaryAttunement.copy(isActive = false),
                            tertiaryAttunement = updatedSlot.tertiaryAttunement.copy(isActive = false),
                            quaternaryAttunement = updatedSlot.quaternaryAttunement.copy(isActive = false)
                        )
                    }
                    onSlotChange(updatedSlot)
                }
            )
            
            AttunementField(
                title = "Secondary Attunement",
                titleColor = MaterialTheme.colorScheme.secondary,
                attunement = slot.secondaryAttunement,
                onAttunementChange = { updatedAttunement ->
                    var updatedSlot = slot.copy(secondaryAttunement = updatedAttunement)
                    if (updatedAttunement.isActive) {
                        // Deactivate all other attunements
                        updatedSlot = updatedSlot.copy(
                            primaryAttunement = updatedSlot.primaryAttunement.copy(isActive = false),
                            tertiaryAttunement = updatedSlot.tertiaryAttunement.copy(isActive = false),
                            quaternaryAttunement = updatedSlot.quaternaryAttunement.copy(isActive = false)
                        )
                    }
                    onSlotChange(updatedSlot)
                }
            )

            if (isFirstSlot) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    // TERTIARY SWITCH
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Tertiary Attunement",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.tertiary
                        )
                        Switch(
                            checked = slot.hasTertiaryAttunement,
                            onCheckedChange = { 
                                Log.d("DEFTFORM", "TERTIARY SWITCH - Changing to: $it")
                                // When enabling tertiary, RESET quaternary completely
                                onSlotChange(slot.copy(
                                    hasTertiaryAttunement = it,
                                    // Reset quaternary when enabling tertiary
                                    quaternaryAttunement = Attunement(),
                                    hasQuaternaryAttunement = false
                                ))
                            },
                            colors = SwitchDefaults.colors(
                                checkedTrackColor = MaterialTheme.colorScheme.tertiary
                            )
                        )
                    }

                    // TERTIARY FIELD
                    if (slot.hasTertiaryAttunement) {
                        AttunementField(
                            title = "Tertiary Attunement",
                            titleColor = MaterialTheme.colorScheme.tertiary,
                            attunement = slot.tertiaryAttunement,
                            onAttunementChange = { updatedAttunement ->
                                Log.d("DEFTFORM", "========= TERTIARY ATTUNEMENT CHANGE =========")
                                Log.d("DEFTFORM", "BEFORE - Tertiary active: ${slot.tertiaryAttunement.isActive}")
                                Log.d("DEFTFORM", "CHANGING TO - Tertiary active: ${updatedAttunement.isActive}")

                                var updatedSlot = slot.copy(tertiaryAttunement = updatedAttunement)
                                
                                if (updatedAttunement.isActive) {
                                    // When tertiary becomes active, deactivate primary/secondary
                                    updatedSlot = updatedSlot.copy(
                                        primaryAttunement = slot.primaryAttunement.copy(isActive = false),
                                        secondaryAttunement = slot.secondaryAttunement.copy(isActive = false)
                                    )
                                } else {
                                    // When tertiary becomes inactive, reset quaternary
                                    updatedSlot = updatedSlot.copy(
                                        quaternaryAttunement = Attunement(),
                                        hasQuaternaryAttunement = false
                                    )
                                }
                                
                                onSlotChange(updatedSlot)
                            }
                        )

                        // QUATERNARY SECTION - Show when tertiary is enabled
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        // QUATERNARY SWITCH
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Quaternary Attunement",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.error
                            )
                            Switch(
                                checked = slot.hasQuaternaryAttunement,
                                onCheckedChange = { 
                                    Log.d("DEFTFORM", "QUATERNARY SWITCH - Changing to: $it")
                                    onSlotChange(slot.copy(
                                        hasQuaternaryAttunement = it,
                                        // Reset quaternary when enabling
                                        quaternaryAttunement = if (it) Attunement() else slot.quaternaryAttunement
                                    ))
                                },
                                colors = SwitchDefaults.colors(
                                    checkedTrackColor = MaterialTheme.colorScheme.error
                                )
                            )
                        }

                        // QUATERNARY FIELD
                        if (slot.hasQuaternaryAttunement) {
                            AttunementField(
                                title = "Quaternary Attunement",
                                titleColor = MaterialTheme.colorScheme.error,
                                attunement = slot.quaternaryAttunement,
                                onAttunementChange = { updatedAttunement ->
                                    Log.d("DEFTFORM", "QUATERNARY FIELD - Changing active to: ${updatedAttunement.isActive}")
                                    var updatedSlot = slot.copy(quaternaryAttunement = updatedAttunement)
                                    if (updatedAttunement.isActive) {
                                        // When quaternary becomes active, deactivate all others
                                        updatedSlot = updatedSlot.copy(
                                            primaryAttunement = slot.primaryAttunement.copy(isActive = false),
                                            secondaryAttunement = slot.secondaryAttunement.copy(isActive = false),
                                            tertiaryAttunement = slot.tertiaryAttunement.copy(isActive = false)
                                        )
                                    }
                                    onSlotChange(updatedSlot)
                                }
                            )
                        }
                    }
                }
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "Daily Power",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.error
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Left box with title and status
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(12.dp)
                    ) {
                        Text(
                            text = "Daily Power Status",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text = if (slot.hasUsedDailyPower) {
                                "This slot's daily power has been used and cannot be used again until tomorrow"
                            } else {
                                "This slot's daily power is available to use"
                            },
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                    
                    // Right box with switch
                    Box(
                        modifier = Modifier
                            .padding(12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Switch(
                            checked = slot.hasUsedDailyPower,
                            onCheckedChange = { onSlotChange(slot.copy(hasUsedDailyPower = it)) }
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AttunementField(
    title: String,
    titleColor: Color,
    attunement: Attunement,
    onAttunementChange: (Attunement) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
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
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    color = titleColor
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Name",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
                OutlinedTextField(
                    value = attunement.name,
                    onValueChange = { onAttunementChange(attunement.copy(name = it)) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                    )
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Type",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
                var expanded by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = attunement.type.name.lowercase().replaceFirstChar { it.uppercase() },
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier
                            .menuAnchor()
                            .weight(1f),
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                        )
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        AttunementType.values().forEach { type ->
                            DropdownMenuItem(
                                text = { Text(type.name.lowercase().replaceFirstChar { it.uppercase() }) },
                                onClick = {
                                    onAttunementChange(attunement.copy(type = type))
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }

            AttunementToggleRow(
                title = "Active",
                isChecked = attunement.isActive,
                onCheckedChange = { isActive ->
                    println("TOGGLE ROW - Changing active state to: $isActive")
                    var updated = attunement.copy(isActive = isActive)
                    if (isActive) {
                        updated = updated.copy(isLost = false)
                    }
                    onAttunementChange(updated)
                }
            )

            AttunementToggleRow(
                title = "Lost",
                isChecked = attunement.isLost,
                onCheckedChange = { isLost ->
                    var updated = attunement.copy(isLost = isLost)
                    if (isLost) {
                        updated = updated.copy(isActive = false)
                    }
                    onAttunementChange(updated)
                }
            )
        }
    }
}

@Composable
private fun AttunementToggleRow(
    title: String,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Left box with title and status
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(12.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = if (isChecked) {
                    "This attunement is active"
                } else {
                    "This attunement is not active"
                },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
        
        // Right box with switch
        Box(
            modifier = Modifier
                .padding(12.dp),
            contentAlignment = Alignment.Center
        ) {
            Switch(
                checked = isChecked,
                onCheckedChange = onCheckedChange
            )
        }
    }
}
