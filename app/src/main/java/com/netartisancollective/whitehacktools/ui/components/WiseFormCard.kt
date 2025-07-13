package com.netartisancollective.whitehacktools.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.netartisancollective.whitehacktools.model.WiseMiracle
import com.netartisancollective.whitehacktools.model.WiseMiracleSlot
import com.netartisancollective.whitehacktools.utilities.AdvancementTables

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WiseFormCard(
    characterClass: String,
    level: Int,
    willpower: Int,
    useCustomAttributes: Boolean,
    wiseMiracleSlots: List<WiseMiracleSlot>,
    onWiseMiracleSlotsChanged: (List<WiseMiracleSlot>) -> Unit,
    modifier: Modifier = Modifier
) {
    if (characterClass == "Wise") {
        val availableSlots = remember(level) {
            AdvancementTables.stats("Wise", level).slots
        }

        val extraInactiveMiracles = remember(willpower, useCustomAttributes) {
            if (useCustomAttributes) {
                0
            } else if (willpower >= 16) {
                2
            } else if (willpower >= 14) {
                1
            } else {
                0
            }
        }

        // Initialize slots if needed and validate active miracles
        LaunchedEffect(availableSlots, wiseMiracleSlots) {
            var needsUpdate = false
            val newSlots = wiseMiracleSlots.toMutableList()

            // Initialize slots if needed
            while (newSlots.size < availableSlots) {
                val slotIndex = newSlots.size
                val baseMiracles = List(2) { WiseMiracle() }
                val additionalMiracles = if (slotIndex == 0) List(extraInactiveMiracles) { WiseMiracle(isAdditional = true) } else emptyList()
                newSlots.add(WiseMiracleSlot(
                    baseMiracles = baseMiracles,
                    additionalMiracles = additionalMiracles,
                    additionalMiracleCount = if (slotIndex == 0) extraInactiveMiracles else 0
                ))
                needsUpdate = true
            }

            // Validate each slot to ensure only one miracle is active
            newSlots.forEachIndexed { index, slot ->
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
                        newSlots[index] = slot.copy(
                            baseMiracles = updatedBaseMiracles,
                            additionalMiracles = updatedAdditionalMiracles
                        )
                    }
                }
            }

            if (needsUpdate) {
                onWiseMiracleSlotsChanged(newSlots)
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
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                wiseMiracleSlots.take(availableSlots).forEachIndexed { index, slot ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            // Slot Header
                            Text(
                                text = "Slot ${index + 1}",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Medium
                            )

                            // Magic Item Toggle for second slot
                            if (index == 1) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = if (slot.isMagicItemSlot) "Magic Item" else "Miracles",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    Switch(
                                        checked = slot.isMagicItemSlot,
                                        onCheckedChange = { isChecked ->
                                            val updatedSlots = wiseMiracleSlots.toMutableList()
                                            val updatedSlot = slot.copy(isMagicItemSlot = isChecked)
                                            updatedSlots[index] = updatedSlot
                                            onWiseMiracleSlotsChanged(updatedSlots)
                                        }
                                    )
                                }

                                if (slot.isMagicItemSlot) {
                                    OutlinedTextField(
                                        value = slot.magicItemName,
                                        onValueChange = { newValue ->
                                            val updatedSlots = wiseMiracleSlots.toMutableList()
                                            val updatedSlot = slot.copy(magicItemName = newValue)
                                            updatedSlots[index] = updatedSlot
                                            onWiseMiracleSlotsChanged(updatedSlots)
                                        },
                                        modifier = Modifier.fillMaxWidth(),
                                        label = { Text("Magic Item Name") },
                                        singleLine = true
                                    )
                                }
                            }

                            // Base Miracles
                            if (!slot.isMagicItemSlot) {
                                slot.baseMiracles.forEachIndexed { miracleIndex, miracle ->
                                    MiracleRow(
                                        miracle = miracle,
                                        onMiracleChanged = { updatedMiracle ->
                                            val updatedSlots = wiseMiracleSlots.toMutableList()
                                            val updatedSlot = if (updatedMiracle.isActive) {
                                                // If activating this miracle, deactivate all others in this slot
                                                slot.copy(
                                                    baseMiracles = slot.baseMiracles.mapIndexed { i, m ->
                                                        if (i == miracleIndex) updatedMiracle else m.copy(isActive = false)
                                                    },
                                                    additionalMiracles = slot.additionalMiracles.map { it.copy(isActive = false) }
                                                )
                                            } else {
                                                slot.copy(
                                                    baseMiracles = slot.baseMiracles.toMutableList().also {
                                                        it[miracleIndex] = updatedMiracle
                                                    }
                                                )
                                            }
                                            updatedSlots[index] = updatedSlot
                                            onWiseMiracleSlotsChanged(updatedSlots)
                                        }
                                    )
                                }

                                // Additional Miracles for first slot
                                if (index == 0) {
                                    Column(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Text(
                                            text = "Additional Miracles",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                                        )

                                        // First Additional Miracle Toggle
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = "First Additional Miracle",
                                                style = MaterialTheme.typography.bodyMedium
                                            )
                                            Switch(
                                                checked = slot.additionalMiracleCount >= 1,
                                                onCheckedChange = { isChecked ->
                                                    val updatedSlots = wiseMiracleSlots.toMutableList()
                                                    val updatedSlot = slot.copy(
                                                        additionalMiracleCount = if (isChecked) 1 else 0
                                                    )
                                                    updatedSlots[index] = updatedSlot
                                                    onWiseMiracleSlotsChanged(updatedSlots)
                                                }
                                            )
                                        }

                                        if (slot.additionalMiracleCount >= 1 && slot.additionalMiracles.isNotEmpty()) {
                                            MiracleRow(
                                                miracle = slot.additionalMiracles[0],
                                                onMiracleChanged = { updatedMiracle ->
                                                    val updatedSlots = wiseMiracleSlots.toMutableList()
                                                    val updatedSlot = if (updatedMiracle.isActive) {
                                                        // If activating this miracle, deactivate all others in this slot
                                                        slot.copy(
                                                            baseMiracles = slot.baseMiracles.map { it.copy(isActive = false) },
                                                            additionalMiracles = slot.additionalMiracles.mapIndexed { i, m ->
                                                                if (i == 0) updatedMiracle else m.copy(isActive = false)
                                                            }
                                                        )
                                                    } else {
                                                        slot.copy(
                                                            additionalMiracles = slot.additionalMiracles.toMutableList().also {
                                                                it[0] = updatedMiracle
                                                            }
                                                        )
                                                    }
                                                    updatedSlots[index] = updatedSlot
                                                    onWiseMiracleSlotsChanged(updatedSlots)
                                                }
                                            )

                                            // Second Additional Miracle Toggle
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(
                                                    text = "Second Additional Miracle",
                                                    style = MaterialTheme.typography.bodyMedium
                                                )
                                                Switch(
                                                    checked = slot.additionalMiracleCount == 2,
                                                    onCheckedChange = { isChecked ->
                                                        val updatedSlots = wiseMiracleSlots.toMutableList()
                                                        val updatedSlot = slot.copy(
                                                            additionalMiracleCount = if (isChecked) 2 else 1
                                                        )
                                                        updatedSlots[index] = updatedSlot
                                                        onWiseMiracleSlotsChanged(updatedSlots)
                                                    }
                                                )
                                            }

                                            if (slot.additionalMiracleCount == 2 && slot.additionalMiracles.size > 1) {
                                                MiracleRow(
                                                    miracle = slot.additionalMiracles[1],
                                                    onMiracleChanged = { updatedMiracle ->
                                                        val updatedSlots = wiseMiracleSlots.toMutableList()
                                                        val updatedSlot = if (updatedMiracle.isActive) {
                                                            // If activating this miracle, deactivate all others in this slot
                                                            slot.copy(
                                                                baseMiracles = slot.baseMiracles.map { it.copy(isActive = false) },
                                                                additionalMiracles = slot.additionalMiracles.mapIndexed { i, m ->
                                                                    if (i == 1) updatedMiracle else m.copy(isActive = false)
                                                                }
                                                            )
                                                        } else {
                                                            slot.copy(
                                                                additionalMiracles = slot.additionalMiracles.toMutableList().also {
                                                                    it[1] = updatedMiracle
                                                                }
                                                            )
                                                        }
                                                        updatedSlots[index] = updatedSlot
                                                        onWiseMiracleSlotsChanged(updatedSlots)
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
    }
}

@Composable
private fun MiracleRow(
    miracle: WiseMiracle,
    onMiracleChanged: (WiseMiracle) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = miracle.name,
            onValueChange = { onMiracleChanged(miracle.copy(name = it)) },
            modifier = Modifier.weight(1f),
            label = { Text(if (miracle.isAdditional) "Additional Miracle" else "Base Miracle") },
            singleLine = true
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Active",
                style = MaterialTheme.typography.bodyMedium,
                color = if (miracle.isActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
            )
            Switch(
                checked = miracle.isActive,
                onCheckedChange = { onMiracleChanged(miracle.copy(isActive = it)) }
            )
        }
    }
}
