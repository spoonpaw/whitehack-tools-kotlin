package com.example.whitehacktools.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.whitehacktools.model.PlayerCharacter
import com.example.whitehacktools.model.WiseMiracle
import com.example.whitehacktools.utilities.AdvancementTables

@Composable
fun WiseDetailCard(
    character: PlayerCharacter,
    onCharacterChanged: (PlayerCharacter) -> Unit,
    modifier: Modifier = Modifier
) {
    if (character.characterClass == "Wise") {
        val availableSlots = remember(character.level) {
            AdvancementTables.stats("Wise", character.level).slots
        }

        val extraInactiveMiracles = remember(character.willpower) {
            if (character.willpower >= 16) {
                2
            } else if (character.willpower >= 14) {
                1
            } else {
                0
            }
        }

        LaunchedEffect(character) {
            // Validate wise miracle slots to ensure only one miracle per slot is active
            val wiseMiracleSlots = character.wiseMiracleSlots.toMutableList()
            var needsUpdate = false

            wiseMiracleSlots.forEachIndexed { index, slot ->
                if (!slot.isMagicItemSlot) {
                    var activeFound = false
                    val updatedBaseMiracles = slot.baseMiracles.map { miracle ->
                        if (miracle.isActive) {
                            if (activeFound) {
                                needsUpdate = true
                                miracle.copy(isActive = false)
                            } else {
                                activeFound = true
                                miracle
                            }
                        } else miracle
                    }

                    val updatedAdditionalMiracles = slot.additionalMiracles.map { miracle ->
                        if (miracle.isActive) {
                            if (activeFound) {
                                needsUpdate = true
                                miracle.copy(isActive = false)
                            } else {
                                activeFound = true
                                miracle
                            }
                        } else miracle
                    }

                    if (updatedBaseMiracles != slot.baseMiracles || updatedAdditionalMiracles != slot.additionalMiracles) {
                        wiseMiracleSlots[index] = slot.copy(
                            baseMiracles = updatedBaseMiracles,
                            additionalMiracles = updatedAdditionalMiracles
                        )
                    }
                }
            }

            if (needsUpdate) {
                onCharacterChanged(character.copy(wiseMiracleSlots = wiseMiracleSlots))
            }
        }

        SectionCard(
            title = "The Wise",
            modifier = modifier
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // Overview Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "Class Overview",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Masters of arcane negotiations who perform miracles through environmental forces. They may be cultists, chemists, meta-mathematicians, exorcists, druids, bards, rune-carvers or wizards.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Class Features Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "Class Features",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "Combat & Equipment:",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "• -2 Attack Value with non-slotted two-handed weapons",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "• +2 Saving Value against magick and mind influence",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "• +2 Hit Point costs when using shields or armor heavier than leather",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "Healing:",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "• Recover Hit Points at twice the natural rate through supernatural recuperation",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "• Still require treatment and potions for non-Hit Point recovery",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                // Miracle Rules Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "Miracle Rules",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "Miracle Costs:",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "• Based on wording, vocation, and desired effect",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "• Cannot attempt miracles costing more than current Hit Points",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "• Must save or double cost if it exceeds character level",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "Energy Detection:",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "• Once per day, spend 10 minutes to detect and use local energy",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "• Makes one effect one magnitude cheaper",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "Cost Modifiers:",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "• +2 HP for each additional target",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "• +2 HP for each additional magnitude",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "• +2 HP for each additional minute of duration",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "• +2 HP for each additional yard of range",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                // Miracles Section
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Miracles",
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
                                    text = "Slot ${index + 1}",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Medium
                                )

                                if (index < character.wiseMiracleSlots.size) {
                                    val slot = character.wiseMiracleSlots[index]

                                    if (index == 1 && slot.isMagicItemSlot) {
                                        // Display Magic Item
                                        Card(
                                            modifier = Modifier.fillMaxWidth(),
                                            shape = RoundedCornerShape(8.dp),
                                            colors = CardDefaults.cardColors(
                                                containerColor = MaterialTheme.colorScheme.surface
                                            )
                                        ) {
                                            Column(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(12.dp),
                                                verticalArrangement = Arrangement.spacedBy(8.dp)
                                            ) {
                                                Text(
                                                    text = slot.magicItemName.ifEmpty { "No magic item name" },
                                                    style = MaterialTheme.typography.bodyMedium,
                                                    color = if (slot.magicItemName.isEmpty()) {
                                                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                                    } else {
                                                        MaterialTheme.colorScheme.onSurface
                                                    }
                                                )
                                            }
                                        }
                                    } else {
                                        // Display miracles
                                        Column(
                                            modifier = Modifier.fillMaxWidth(),
                                            verticalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            // Base miracles
                                            slot.baseMiracles.forEach { miracle ->
                                                MiracleRow(miracle = miracle)
                                            }

                                            // Additional miracles for first slot
                                            if (index == 0) {
                                                slot.additionalMiracles.forEach { miracle ->
                                                    MiracleRow(miracle = miracle)
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
        }
    }
}

@Composable
private fun MiracleRow(
    miracle: WiseMiracle,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = if (miracle.name.isEmpty()) {
                if (miracle.isAdditional) "No additional miracle" else "No base miracle"
            } else {
                miracle.name
            },
            style = MaterialTheme.typography.bodyMedium,
            color = if (miracle.name.isEmpty()) {
                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            } else {
                MaterialTheme.colorScheme.onSurface
            },
            modifier = Modifier.weight(1f)
        )

        Text(
            text = if (miracle.isActive) "Active" else "Inactive",
            style = MaterialTheme.typography.bodySmall,
            color = if (miracle.isActive) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            }
        )
    }
}
