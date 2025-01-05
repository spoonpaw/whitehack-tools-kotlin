package com.example.whitehacktools.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.whitehacktools.model.*

@Composable
fun DeftDetailCard(
    character: PlayerCharacter?,
    onCharacterChange: (PlayerCharacter) -> Unit,
    modifier: Modifier = Modifier
) {
    if (character != null && character.characterClass == "Deft") {
        SectionCard(
            title = "The Deft",
            modifier = modifier
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                for (index in 0 until character.level) {
                    if (index < character.attunementSlots.size) {
                        val slot = character.attunementSlots[index]
                        OutlinedCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = if (index == 0) 0.dp else 8.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.outlinedCardColors(
                                containerColor = MaterialTheme.colorScheme.surface
                            ),
                            border = BorderStroke(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                            )
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 12.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = "Attunement Slot ${index + 1}",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                                    modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
                                )

                                // Always show primary and secondary
                                DetailItem(
                                    label = "Primary Attunement",
                                    value = if (slot.primaryAttunement.name.isEmpty()) {
                                        "None"
                                    } else {
                                        "${slot.primaryAttunement.name} (${slot.primaryAttunement.type.name.lowercase().replaceFirstChar { it.uppercase() }})" +
                                            if (slot.primaryAttunement.isActive) " - Active" else "" +
                                            if (slot.primaryAttunement.isLost) " - Lost" else ""
                                    }
                                )
                                
                                DetailItem(
                                    label = "Secondary Attunement",
                                    value = if (slot.secondaryAttunement.name.isEmpty()) {
                                        "None"
                                    } else {
                                        "${slot.secondaryAttunement.name} (${slot.secondaryAttunement.type.name.lowercase().replaceFirstChar { it.uppercase() }})" +
                                            if (slot.secondaryAttunement.isActive) " - Active" else "" +
                                            if (slot.secondaryAttunement.isLost) " - Lost" else ""
                                    }
                                )

                                // Show tertiary if enabled
                                if (slot.hasTertiaryAttunement) {
                                    DetailItem(
                                        label = "Tertiary Attunement",
                                        value = if (slot.tertiaryAttunement.name.isEmpty()) {
                                            "None"
                                        } else {
                                            "${slot.tertiaryAttunement.name} (${slot.tertiaryAttunement.type.name.lowercase().replaceFirstChar { it.uppercase() }})" +
                                                if (slot.tertiaryAttunement.isActive) " - Active" else "" +
                                                if (slot.tertiaryAttunement.isLost) " - Lost" else ""
                                        }
                                    )
                                }

                                // Show quaternary if enabled
                                if (slot.hasQuaternaryAttunement) {
                                    DetailItem(
                                        label = "Quaternary Attunement",
                                        value = if (slot.quaternaryAttunement.name.isEmpty()) {
                                            "None"
                                        } else {
                                            "${slot.quaternaryAttunement.name} (${slot.quaternaryAttunement.type.name.lowercase().replaceFirstChar { it.uppercase() }})" +
                                                if (slot.quaternaryAttunement.isActive) " - Active" else "" +
                                                if (slot.quaternaryAttunement.isLost) " - Lost" else ""
                                        }
                                    )
                                }

                                DetailItem(
                                    label = "Daily Power",
                                    value = if (slot.hasUsedDailyPower) 
                                        "Used" 
                                    else 
                                        "Available",
                                    valueColor = if (slot.hasUsedDailyPower) 
                                        MaterialTheme.colorScheme.error 
                                    else 
                                        MaterialTheme.colorScheme.tertiary
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
