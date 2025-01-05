package com.example.whitehacktools.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.whitehacktools.model.WiseMiracle
import com.example.whitehacktools.model.WiseMiracleSlot
import com.example.whitehacktools.model.WiseMiracles
import com.example.whitehacktools.utilities.AdvancementTables

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WiseFormCard(
    characterClass: String,
    level: Int,
    willpower: Int,
    useCustomAttributes: Boolean,
    wiseMiracles: WiseMiracles,
    onWiseMiraclesChanged: (WiseMiracles) -> Unit,
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

        // Initialize slots if needed
        LaunchedEffect(availableSlots) {
            val newSlots = wiseMiracles.slots.toMutableList()
            while (newSlots.size < availableSlots) {
                // Create a new slot with the appropriate number of miracles
                val slotIndex = newSlots.size
                val miracleCount = if (slotIndex == 0) 2 + extraInactiveMiracles else 2
                val miracles = List(miracleCount) { WiseMiracle() }
                newSlots.add(WiseMiracleSlot(miracles = miracles))
            }
            onWiseMiraclesChanged(wiseMiracles.copy(slots = newSlots))
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

                            val slot = wiseMiracles.slots.getOrNull(index) ?: WiseMiracleSlot()
                            val miracleCount = if (index == 0) 2 + extraInactiveMiracles else 2

                            repeat(miracleCount) { miracleIndex ->
                                val miracle = slot.miracles.getOrNull(miracleIndex) ?: WiseMiracle()
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
                                        OutlinedTextField(
                                            value = miracle.name,
                                            onValueChange = { newName ->
                                                val newMiracles = slot.miracles.toMutableList()
                                                while (newMiracles.size <= miracleIndex) {
                                                    newMiracles.add(WiseMiracle())
                                                }
                                                newMiracles[miracleIndex] = miracle.copy(name = newName)
                                                
                                                val newSlots = wiseMiracles.slots.toMutableList()
                                                while (newSlots.size <= index) {
                                                    val slotIndex = newSlots.size
                                                    val slotMiracleCount = if (slotIndex == 0) 2 + extraInactiveMiracles else 2
                                                    val slotMiracles = List(slotMiracleCount) { WiseMiracle() }
                                                    newSlots.add(WiseMiracleSlot(miracles = slotMiracles))
                                                }
                                                newSlots[index] = slot.copy(miracles = newMiracles)
                                                
                                                onWiseMiraclesChanged(wiseMiracles.copy(slots = newSlots))
                                            },
                                            label = { Text("Miracle Name") },
                                            modifier = Modifier.fillMaxWidth()
                                        )

                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Switch(
                                                checked = miracle.isActive,
                                                onCheckedChange = { isActive ->
                                                    val newMiracles = slot.miracles.toMutableList()
                                                    while (newMiracles.size <= miracleIndex) {
                                                        newMiracles.add(WiseMiracle())
                                                    }
                                                    
                                                    if (isActive) {
                                                        for (i in newMiracles.indices) {
                                                            newMiracles[i] = newMiracles[i].copy(isActive = i == miracleIndex)
                                                        }
                                                    } else {
                                                        newMiracles[miracleIndex] = miracle.copy(isActive = false)
                                                    }
                                                    
                                                    val newSlots = wiseMiracles.slots.toMutableList()
                                                    while (newSlots.size <= index) {
                                                        val slotIndex = newSlots.size
                                                        val slotMiracleCount = if (slotIndex == 0) 2 + extraInactiveMiracles else 2
                                                        val slotMiracles = List(slotMiracleCount) { WiseMiracle() }
                                                        newSlots.add(WiseMiracleSlot(miracles = slotMiracles))
                                                    }
                                                    newSlots[index] = slot.copy(miracles = newMiracles)
                                                    
                                                    onWiseMiraclesChanged(wiseMiracles.copy(slots = newSlots))
                                                }
                                            )
                                            Text(
                                                text = if (miracle.isActive) "Active" else "Inactive",
                                                style = MaterialTheme.typography.bodyMedium
                                            )
                                        }
                                    }
                                }
                            }

                            if (index == 0) {
                                Text(
                                    text = "Additional Miracles",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier.padding(top = 8.dp)
                                )

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Switch(
                                        checked = slot.miracles.size > miracleCount,
                                        onCheckedChange = { hasFirstAdditional ->
                                            val newMiracles = slot.miracles.toMutableList()
                                            if (hasFirstAdditional) {
                                                if (newMiracles.size == miracleCount) {
                                                    newMiracles.add(WiseMiracle())
                                                }
                                            } else {
                                                while (newMiracles.size > miracleCount) {
                                                    newMiracles.removeLast()
                                                }
                                            }
                                            
                                            val newSlots = wiseMiracles.slots.toMutableList()
                                            newSlots[index] = slot.copy(miracles = newMiracles)
                                            onWiseMiraclesChanged(wiseMiracles.copy(slots = newSlots))
                                        }
                                    )
                                    Text(
                                        text = "First Additional Miracle",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }

                                if (slot.miracles.size > miracleCount) {
                                    val additionalMiracle = slot.miracles.getOrNull(miracleCount) ?: WiseMiracle()
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
                                            OutlinedTextField(
                                                value = additionalMiracle.name,
                                                onValueChange = { newName ->
                                                    val newMiracles = slot.miracles.toMutableList()
                                                    newMiracles[miracleCount] = additionalMiracle.copy(name = newName)
                                                    
                                                    val newSlots = wiseMiracles.slots.toMutableList()
                                                    newSlots[index] = slot.copy(miracles = newMiracles)
                                                    onWiseMiraclesChanged(wiseMiracles.copy(slots = newSlots))
                                                },
                                                label = { Text("Additional Miracle Name") },
                                                modifier = Modifier.fillMaxWidth()
                                            )

                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Switch(
                                                    checked = additionalMiracle.isActive,
                                                    onCheckedChange = { isActive ->
                                                        val newMiracles = slot.miracles.toMutableList()
                                                        if (isActive) {
                                                            for (i in newMiracles.indices) {
                                                                newMiracles[i] = newMiracles[i].copy(isActive = i == miracleCount)
                                                            }
                                                        } else {
                                                            newMiracles[miracleCount] = additionalMiracle.copy(isActive = false)
                                                        }
                                                        
                                                        val newSlots = wiseMiracles.slots.toMutableList()
                                                        newSlots[index] = slot.copy(miracles = newMiracles)
                                                        onWiseMiraclesChanged(wiseMiracles.copy(slots = newSlots))
                                                    }
                                                )
                                                Text(
                                                    text = if (additionalMiracle.isActive) "Active" else "Inactive",
                                                    style = MaterialTheme.typography.bodyMedium
                                                )
                                            }
                                        }
                                    }

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Switch(
                                            checked = slot.miracles.size > miracleCount + 1,
                                            onCheckedChange = { hasSecondAdditional ->
                                                val newMiracles = slot.miracles.toMutableList()
                                                if (hasSecondAdditional) {
                                                    if (newMiracles.size == miracleCount + 1) {
                                                        newMiracles.add(WiseMiracle())
                                                    }
                                                } else {
                                                    while (newMiracles.size > miracleCount + 1) {
                                                        newMiracles.removeLast()
                                                    }
                                                }
                                                
                                                val newSlots = wiseMiracles.slots.toMutableList()
                                                newSlots[index] = slot.copy(miracles = newMiracles)
                                                onWiseMiraclesChanged(wiseMiracles.copy(slots = newSlots))
                                            }
                                        )
                                        Text(
                                            text = "Second Additional Miracle",
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                    }

                                    if (slot.miracles.size > miracleCount + 1) {
                                        val secondAdditionalMiracle = slot.miracles.getOrNull(miracleCount + 1) ?: WiseMiracle()
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
                                                OutlinedTextField(
                                                    value = secondAdditionalMiracle.name,
                                                    onValueChange = { newName ->
                                                        val newMiracles = slot.miracles.toMutableList()
                                                        newMiracles[miracleCount + 1] = secondAdditionalMiracle.copy(name = newName)
                                                        
                                                        val newSlots = wiseMiracles.slots.toMutableList()
                                                        newSlots[index] = slot.copy(miracles = newMiracles)
                                                        onWiseMiraclesChanged(wiseMiracles.copy(slots = newSlots))
                                                    },
                                                    label = { Text("Additional Miracle Name") },
                                                    modifier = Modifier.fillMaxWidth()
                                                )

                                                Row(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Switch(
                                                        checked = secondAdditionalMiracle.isActive,
                                                        onCheckedChange = { isActive ->
                                                            val newMiracles = slot.miracles.toMutableList()
                                                            if (isActive) {
                                                                for (i in newMiracles.indices) {
                                                                    newMiracles[i] = newMiracles[i].copy(isActive = i == miracleCount + 1)
                                                                }
                                                            } else {
                                                                newMiracles[miracleCount + 1] = secondAdditionalMiracle.copy(isActive = false)
                                                            }
                                                            
                                                            val newSlots = wiseMiracles.slots.toMutableList()
                                                            newSlots[index] = slot.copy(miracles = newMiracles)
                                                            onWiseMiraclesChanged(wiseMiracles.copy(slots = newSlots))
                                                        }
                                                    )
                                                    Text(
                                                        text = if (secondAdditionalMiracle.isActive) "Active" else "Inactive",
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
            }
        }
    }
}
