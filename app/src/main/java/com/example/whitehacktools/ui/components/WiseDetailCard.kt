package com.example.whitehacktools.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.whitehacktools.model.PlayerCharacter
import com.example.whitehacktools.model.WiseMiracle
import com.example.whitehacktools.utilities.AdvancementTables

@Composable
fun WiseDetailCard(
    character: PlayerCharacter,
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

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
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

                                        val slot = character.wiseMiracles.slots.getOrNull(index)
                                        val miracleCount = if (index == 0) 2 + extraInactiveMiracles else 2
                                        
                                        // Show base miracles
                                        repeat(miracleCount) { miracleIndex ->
                                            val miracle = slot?.miracles?.getOrNull(miracleIndex) ?: WiseMiracle()
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
                                                        .padding(12.dp)
                                                ) {
                                                    Text(
                                                        text = if (miracle.name.isEmpty()) "(Empty)" else miracle.name,
                                                        style = MaterialTheme.typography.bodyLarge,
                                                        fontWeight = FontWeight.Medium,
                                                        color = if (miracle.name.isEmpty()) 
                                                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                                        else 
                                                            MaterialTheme.colorScheme.onSurface
                                                    )
                                                    Row(
                                                        modifier = Modifier.fillMaxWidth(),
                                                        horizontalArrangement = Arrangement.Start,
                                                        verticalAlignment = Alignment.CenterVertically
                                                    ) {
                                                        Text(
                                                            text = if (miracle.isActive) "Active" else "Inactive",
                                                            style = MaterialTheme.typography.bodyMedium,
                                                            color = if (miracle.isActive) 
                                                                MaterialTheme.colorScheme.primary 
                                                            else 
                                                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                                        )
                                                    }
                                                }
                                            }
                                        }

                                        // Show additional miracles for first slot
                                        if (index == 0 && slot?.miracles?.size ?: 0 > miracleCount) {
                                            Text(
                                                text = "Additional Miracles",
                                                style = MaterialTheme.typography.titleSmall,
                                                fontWeight = FontWeight.Medium,
                                                modifier = Modifier.padding(top = 8.dp)
                                            )

                                            // Show additional miracles
                                            for (additionalIndex in miracleCount until (slot?.miracles?.size ?: 0)) {
                                                val additionalMiracle = slot?.miracles?.getOrNull(additionalIndex) ?: WiseMiracle()
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
                                                            .padding(12.dp)
                                                    ) {
                                                        Text(
                                                            text = if (additionalMiracle.name.isEmpty()) "(Empty)" else additionalMiracle.name,
                                                            style = MaterialTheme.typography.bodyLarge,
                                                            fontWeight = FontWeight.Medium,
                                                            color = if (additionalMiracle.name.isEmpty()) 
                                                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                                            else 
                                                                MaterialTheme.colorScheme.onSurface
                                                        )
                                                        Row(
                                                            modifier = Modifier.fillMaxWidth(),
                                                            horizontalArrangement = Arrangement.Start,
                                                            verticalAlignment = Alignment.CenterVertically
                                                        ) {
                                                            Text(
                                                                text = if (additionalMiracle.isActive) "Active" else "Inactive",
                                                                style = MaterialTheme.typography.bodyMedium,
                                                                color = if (additionalMiracle.isActive) 
                                                                    MaterialTheme.colorScheme.primary 
                                                                else 
                                                                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
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
    }
}
