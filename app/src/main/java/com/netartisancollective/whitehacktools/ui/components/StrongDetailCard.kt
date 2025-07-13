package com.netartisancollective.whitehacktools.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.netartisancollective.whitehacktools.model.PlayerCharacter
import com.netartisancollective.whitehacktools.utilities.AdvancementTables

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StrongDetailCard(
    character: PlayerCharacter,
    modifier: Modifier = Modifier
) {
    if (character.characterClass == "Strong") {
        val availableSlots = remember(character.level) {
            AdvancementTables.stats("Strong", character.level).slots
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
                // Class Overview Card
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
                            text = "Strong characters rely on combat skills and physique. They can be warriors, guards, brigands, knights, bounty hunters or barbarians.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Attribute Bonuses Card
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
                            text = "Class Bonuses",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "• Gain +1 Attack Value at Strength 13, and +1 damage at Strength 16.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "• Gain +1 Hit Points at Toughness 13, and another +1 at Toughness 16.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "• Proficient with all weapons and armor.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "• Gain +1 Saving Value against poison and death, and +4 against special melee attacks.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                // Combat Features Card
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
                            text = "Combat Features",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            StrongFeatureRow(
                                title = "Basic Combat",
                                description = "Two free attacks per round (others get one)"
                            )

                            val stats = remember(character.level) {
                                AdvancementTables.stats("Strong", character.level)
                            }
                            val maxFlowAttacks = remember(stats.raises) {
                                if (stats.raises == "-") 1 else stats.raises.toInt() + 1
                            }
                            StrongFeatureRow(
                                title = "Flow Attacks ($maxFlowAttacks per round)",
                                description = "After reducing enemy to 0 HP, attack adjacent enemy (melee) or enemy adjacent to prior target (ranged)"
                            )
                            StrongFeatureRow(
                                title = "Combat Options",
                                description = "Use any special combat option and fill slots with Strong abilities. Effects last one round"
                            )
                        }
                    }
                }

                // Conflict Looting Card
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
                            text = "Conflict Looting",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            StrongLootTypeCard(
                                title = "Special",
                                description = "Note special/cultural conflicts. Later gain +2 for one round to any stat if related to the memory"
                            )
                            StrongLootTypeCard(
                                title = "Substance",
                                description = "Extract rare substance (poison, acid, etc.) from enemy with suitable keyword"
                            )
                            StrongLootTypeCard(
                                title = "Supernatural",
                                description = "On killing blow, gain enemy's non-violent supernatural ability"
                            )
                        }

                        Text(
                            text = "Can hold one loot at a time, usable character-level times. Substances count as inventory",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Combat Options Section
                if (character.strongCombatOptions != null) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "Combat Options",
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
                                                .padding(12.dp)
                                        ) {
                                            Text(
                                                text = "Slot ${index + 1}",
                                                style = MaterialTheme.typography.titleSmall,
                                                fontWeight = FontWeight.Medium
                                            )
                                            
                                            Spacer(modifier = Modifier.height(8.dp))

                                            val option = character.strongCombatOptions.options.getOrNull(index)
                                            if (option != null) {
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
                                                            text = option.displayName,
                                                            style = MaterialTheme.typography.bodyLarge,
                                                            fontWeight = FontWeight.Medium
                                                        )
                                                        Text(
                                                            text = option.description,
                                                            style = MaterialTheme.typography.bodyMedium,
                                                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                                        )
                                                    }
                                                }
                                            } else {
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
                                                            text = "Empty",
                                                            style = MaterialTheme.typography.bodyMedium,
                                                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
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
                            if (character.conflictLoot?.keyword.isNullOrEmpty()) {
                                Text(
                                    text = "Empty",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                )
                            } else {
                                character.conflictLoot?.let { loot ->
                                    Text(
                                        text = "Keyword: ${loot.keyword}",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    Text(
                                        text = "Type: ${loot.type}",
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                    Text(
                                        text = "Uses Remaining: ${loot.usesRemaining}",
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

@Composable
private fun StrongFeatureRow(
    title: String,
    description: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun StrongLootTypeCard(
    title: String,
    description: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
