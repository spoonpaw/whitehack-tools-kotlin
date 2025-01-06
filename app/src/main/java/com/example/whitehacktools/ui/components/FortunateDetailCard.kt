package com.example.whitehacktools.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.whitehacktools.model.*

@Composable
fun FortunateDetailCard(
    character: PlayerCharacter,
    modifier: Modifier = Modifier
) {
    if (character.characterClass == "Fortunate") {
        SectionCard(
            title = "The Fortunate",
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
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "Class Overview",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Medium
                        )
                        
                        Text(
                            text = """
                                Fortunate characters are born with the advantages of nobility, fame, destiny, wealth, or a combination thereof. They can be royal heirs, rich and influential merchants, star performers, or religious icons. Once per game session, they may use their good fortune in a major way, like hiring a large ship, performing the will of a god, getting a personal audience with the queen, or being hailed as a friend by a hostile tribe.

                                The Fortunate may use any weapon or armor without penalty. They have +4 to charisma when checking retainer morale, +2 in reaction rolls, and +6 in any reputation roll. They also get a single signature object with plot immunity.
                            """.trimIndent(),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            FeatureRow(
                                title = "Combat Proficiency",
                                description = "Can use any weapon or armor without penalty."
                            )
                            FeatureRow(
                                title = "Social Advantages",
                                description = "+4 to charisma for retainer morale, +2 on reaction rolls, +6 on reputation rolls."
                            )
                        }
                    }
                }

                // Fortune Power Card
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
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "Fortune Power",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Medium
                        )
                        
                        Text(
                            text = """
                                Once per game session, the Fortunate may use their good fortune in a major way, such as:

                                • Hiring a large ship.
                                • Performing the will of a god.
                                • Getting a personal audience with the queen.
                                • Being hailed as a friend by a hostile tribe.

                                Note: The Fortunate may not use their fortune power to purchase experience or fund XP for others.
                            """.trimIndent(),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Text(
                            text = if (character.fortunateOptions.hasUsedFortune) 
                                "Fortune power has been used this session" 
                            else 
                                "Fortune power is available",
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (character.fortunateOptions.hasUsedFortune) 
                                MaterialTheme.colorScheme.error 
                            else 
                                MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }

                // Standing Card
                if (character.fortunateOptions.standing.isNotEmpty()) {
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
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Text(
                                text = "Standing",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Medium
                            )
                            
                            Text(
                                text = character.fortunateOptions.standing,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Text(
                                text = "When the referee thinks that the standing is relevant:",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                BenefitRow(
                                    title = "Faction Relations",
                                    description = "Affiliated factions are considerably more helpful, and their enemies more vengeful. Others may distance themselves or show interest."
                                )
                                BenefitRow(
                                    title = "Species Benefits",
                                    description = "Your species gives any applicable benefits regardless of attribute."
                                )
                                BenefitRow(
                                    title = "Task Bonus",
                                    description = "If standing and vocation align for a task, and the vocation is marked next to the applicable attribute, you get a +6 bonus."
                                )
                            }
                        }
                    }
                }

                // Signature Object Card
                if (character.fortunateOptions.signatureObject.name.isNotEmpty()) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "Signature Object",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = character.fortunateOptions.signatureObject.name,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }

                // Retainers Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Retainers",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = "${character.fortunateOptions.retainers.count { it.name.isNotEmpty() }}/4",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        
                        character.fortunateOptions.retainers.forEachIndexed { index, retainer ->
                            RetainerCard(
                                retainer = retainer,
                                index = index,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FeatureRow(
    title: String,
    description: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = description,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun BenefitRow(
    title: String,
    description: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = description,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun StatBox(label: String, value: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.primary
        )
        Surface(
            shape = RoundedCornerShape(4.dp),
            color = MaterialTheme.colorScheme.surfaceVariant
        ) {
            Text(
                text = value,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
private fun RetainerCard(
    retainer: Retainer,
    index: Int,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Retainer ${index + 1}",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary
            )
            if (retainer.name.isNotEmpty()) {
                Text(
                    text = retainer.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    StatBox(label = "HP", value = "${retainer.currentHP}/${retainer.maxHP}")
                    StatBox(label = "HD", value = retainer.hitDice.toString())
                    StatBox(label = "DF", value = retainer.defense.toString())
                    StatBox(label = "MV", value = retainer.movement.toString())
                }
                if (retainer.keywords.isNotEmpty()) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "Keywords",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        
                        val rows = remember(retainer.keywords) {
                            val result = mutableListOf<List<String>>()
                            val currentRow = mutableListOf<String>()
                            var currentRowWidth = 0f
                            
                            retainer.keywords.forEach { keyword ->
                                val itemWidth = keyword.length * 12f + 24f // 24dp for padding
                                
                                if (currentRowWidth + itemWidth > 360f) {
                                    if (currentRow.isNotEmpty()) {
                                        result.add(currentRow.toList())
                                        currentRow.clear()
                                        currentRowWidth = itemWidth
                                        currentRow.add(keyword)
                                    } else {
                                        result.add(listOf(keyword))
                                    }
                                } else {
                                    currentRowWidth += itemWidth
                                    currentRow.add(keyword)
                                }
                            }
                            
                            if (currentRow.isNotEmpty()) {
                                result.add(currentRow.toList())
                            }
                            
                            result
                        }
                        
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            rows.forEach { rowKeywords ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally)
                                ) {
                                    rowKeywords.forEach { keyword ->
                                        Surface(
                                            modifier = Modifier.wrapContentWidth(),
                                            shape = RoundedCornerShape(16.dp),
                                            color = MaterialTheme.colorScheme.surfaceVariant,
                                            border = BorderStroke(
                                                width = 1.dp,
                                                color = MaterialTheme.colorScheme.outline
                                            )
                                        ) {
                                            Text(
                                                text = keyword,
                                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                                style = MaterialTheme.typography.bodyMedium,
                                                textAlign = TextAlign.Center
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                if (retainer.notes.isNotEmpty()) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "Notes",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = retainer.notes,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            } else {
                Text(
                    text = "Empty",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.outline
                )
            }
        }
    }
}
