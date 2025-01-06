package com.example.whitehacktools.ui.components

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
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

    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.05f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            character.attunementSlots.take(availableSlots).forEachIndexed { index, slot ->
                // Each slot gets its own card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
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
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )

                        // Function to update the slot
                        fun onSlotChange(updatedSlot: AttunementSlot) {
                            val updatedSlots = character.attunementSlots.toMutableList()
                            updatedSlots[index] = updatedSlot
                            onCharacterChange(character.copy(attunementSlots = updatedSlots))
                        }

                        // Daily Power Section
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(
                                    BorderStroke(1.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.2f)),
                                    RoundedCornerShape(8.dp)
                                )
                                .padding(12.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "Daily Power",
                                        style = MaterialTheme.typography.titleSmall,
                                        color = MaterialTheme.colorScheme.error
                                    )
                                    Text(
                                        text = if (slot.hasUsedDailyPower) 
                                            "Used for today"
                                        else 
                                            "Available",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                Switch(
                                    checked = slot.hasUsedDailyPower,
                                    onCheckedChange = { onSlotChange(slot.copy(hasUsedDailyPower = it)) },
                                    colors = SwitchDefaults.colors(
                                        checkedTrackColor = MaterialTheme.colorScheme.error
                                    )
                                )
                            }
                        }

                        // Primary Attunement Section
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(
                                    BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)),
                                    RoundedCornerShape(8.dp)
                                )
                                .padding(12.dp)
                        ) {
                            AttunementField(
                                title = "Primary Attunement",
                                titleColor = MaterialTheme.colorScheme.primary,
                                attunement = slot.primaryAttunement,
                                onAttunementChange = { updatedAttunement ->
                                    var updatedSlot = slot.copy(primaryAttunement = updatedAttunement)
                                    if (updatedAttunement.isActive) {
                                        updatedSlot = updatedSlot.copy(
                                            secondaryAttunement = updatedSlot.secondaryAttunement.copy(isActive = false),
                                            tertiaryAttunement = updatedSlot.tertiaryAttunement.copy(isActive = false),
                                            quaternaryAttunement = updatedSlot.quaternaryAttunement.copy(isActive = false)
                                        )
                                    }
                                    onSlotChange(updatedSlot)
                                }
                            )
                        }

                        // Secondary Attunement Section
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(
                                    BorderStroke(1.dp, MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f)),
                                    RoundedCornerShape(8.dp)
                                )
                                .padding(12.dp)
                        ) {
                            AttunementField(
                                title = "Secondary Attunement",
                                titleColor = MaterialTheme.colorScheme.secondary,
                                attunement = slot.secondaryAttunement,
                                onAttunementChange = { updatedAttunement ->
                                    var updatedSlot = slot.copy(secondaryAttunement = updatedAttunement)
                                    if (updatedAttunement.isActive) {
                                        updatedSlot = updatedSlot.copy(
                                            primaryAttunement = updatedSlot.primaryAttunement.copy(isActive = false),
                                            tertiaryAttunement = updatedSlot.tertiaryAttunement.copy(isActive = false),
                                            quaternaryAttunement = updatedSlot.quaternaryAttunement.copy(isActive = false)
                                        )
                                    }
                                    onSlotChange(updatedSlot)
                                }
                            )
                        }

                        if (index == 0) {
                            // Tertiary Section
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(
                                        BorderStroke(1.dp, MaterialTheme.colorScheme.tertiary.copy(alpha = 0.2f)),
                                        RoundedCornerShape(8.dp)
                                    )
                                    .padding(12.dp)
                            ) {
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
                                            onSlotChange(slot.copy(
                                                hasTertiaryAttunement = it,
                                                quaternaryAttunement = Attunement(),
                                                hasQuaternaryAttunement = false
                                            ))
                                        },
                                        colors = SwitchDefaults.colors(
                                            checkedTrackColor = MaterialTheme.colorScheme.tertiary
                                        )
                                    )
                                }

                                if (slot.hasTertiaryAttunement) {
                                    AttunementField(
                                        title = "Tertiary Attunement",
                                        titleColor = MaterialTheme.colorScheme.tertiary,
                                        attunement = slot.tertiaryAttunement,
                                        onAttunementChange = { updatedAttunement ->
                                            var updatedSlot = slot.copy(tertiaryAttunement = updatedAttunement)
                                            if (updatedAttunement.isActive) {
                                                updatedSlot = updatedSlot.copy(
                                                    primaryAttunement = slot.primaryAttunement.copy(isActive = false),
                                                    secondaryAttunement = slot.secondaryAttunement.copy(isActive = false)
                                                )
                                            }
                                            onSlotChange(updatedSlot)
                                        }
                                    )
                                }
                            }

                            // Quaternary Section (only if tertiary is enabled)
                            if (slot.hasTertiaryAttunement) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .border(
                                            BorderStroke(1.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.2f)),
                                            RoundedCornerShape(8.dp)
                                        )
                                        .padding(12.dp)
                                ) {
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
                                                onSlotChange(slot.copy(
                                                    hasQuaternaryAttunement = it,
                                                    quaternaryAttunement = if (it) Attunement() else slot.quaternaryAttunement
                                                ))
                                            },
                                            colors = SwitchDefaults.colors(
                                                checkedTrackColor = MaterialTheme.colorScheme.error
                                            )
                                        )
                                    }

                                    if (slot.hasQuaternaryAttunement) {
                                        AttunementField(
                                            title = "Quaternary Attunement",
                                            titleColor = MaterialTheme.colorScheme.error,
                                            attunement = slot.quaternaryAttunement,
                                            onAttunementChange = { updatedAttunement ->
                                                var updatedSlot = slot.copy(quaternaryAttunement = updatedAttunement)
                                                if (updatedAttunement.isActive) {
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
                title = if (attunement.isLost) "Lost" else "Not Lost",
                isChecked = attunement.isLost,
                onCheckedChange = { isLost ->
                    var updated = attunement.copy(isLost = isLost)
                    if (isLost) {
                        updated = updated.copy(isActive = false)
                    }
                    onAttunementChange(updated)
                }
            )

            AttunementToggleRow(
                title = if (attunement.isActive) "Active" else "Inactive",
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
                text = if (title.contains("Lost")) {
                    if (isChecked) "This attunement is lost" else "This attunement is not lost"
                } else {
                    if (isChecked) "This attunement is active" else "This attunement is not active"
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
