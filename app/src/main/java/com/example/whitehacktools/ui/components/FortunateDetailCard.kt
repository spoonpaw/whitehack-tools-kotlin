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
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
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
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "Class Overview",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Medium
                        )
                        
                        Text(
                            text = """
                                Fortunate characters are born with the advantages of nobility, fame, destiny, wealth, or a combination thereof. They can be royal heirs, rich and influential merchants, star performers, or religious icons.
                                
                                The Fortunate may use any weapon or armor without penalty. They have +4 to charisma when checking retainer morale, +2 in reaction rolls, and +6 in any reputation roll.
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

                // Good Fortune Card
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
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "Good Fortune",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Medium
                        )
                        
                        Text(
                            text = """
                                Once per game session, they may use their good fortune in a major way, such as:
                                • Hiring a large ship
                                • Performing the will of a god
                                • Getting a personal audience with the queen
                                • Being hailed as a friend by a hostile tribe
                                
                                Important: The Fortunate may not use their good fortune class power to purchase experience or fund XP for others.
                            """.trimIndent(),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Text(
                            text = if (character.fortunateOptions.hasUsedFortune) 
                                "Good fortune has been used this session" 
                            else 
                                "Good fortune is available",
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
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "Standing",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Medium
                        )
                        
                        Text(
                            text = """
                                Fortunate characters have a defining standing noted in the Identity section that works as an occasional group booster. For example, a Fortunate Flower Monk with a "Reincarnated Master" standing might have unique tattoos, training, and physical traits that boost the role of their groups.

                                When the Referee thinks the standing is relevant:
                                • Affiliated factions are considerably more helpful and their enemies more vengeful. Others may distance themselves or show interest
                                • The character's species gives any applicable benefits regardless of attribute
                                • If standing and vocation align for a task, and the vocation is marked next to the applicable attribute, the character gets a +6 bonus
                            """.trimIndent(),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        if (character.fortunateOptions.standing.isNotEmpty()) {
                            Text(
                                text = character.fortunateOptions.standing,
                                style = MaterialTheme.typography.bodyLarge,
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
                        } else {
                            Text(
                                text = "Empty",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.outline,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }

                // Retainers Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "Retainers",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Medium
                        )
                        
                        Text(
                            text = """
                                As the only class, the Fortunate are allowed to have retainers that can grow in strength, like a chambermaid, cook, apprentice, or squire. They start with one retainer and gain slots for additional ones.

                                Retainer Details:
                                • Have HD, DF, MV, and keywords as per monster rules
                                • DF and AV (HD+10) may be reconsidered if equipment changes
                                • HD increases at the Fortunate's even levels
                                • First retainer can reach HD 6 by level 10
                                
                                At the Referee's discretion, retainers:
                                • Work within established contracts
                                • Have unique personalities (devout, rebellious, loving, etc.)
                                • Players may temporarily control retainers during adventures
                            """.trimIndent(),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
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

                // Signature Object Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Signature Object",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Medium
                        )
                        
                        Text(
                            text = """
                                The Fortunate get a single signature object. At the Referee's discretion it may be:
                                • Special material
                                • Superior quality
                                • Magical
                                
                                Plot Immunity: The object can never be lost, destroyed, or made irretrievable by the Referee unless the player wishes it to happen.
                            """.trimIndent(),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        if (character.fortunateOptions.signatureObject.name.isNotEmpty()) {
                            Text(
                                text = character.fortunateOptions.signatureObject.name,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        } else {
                            Text(
                                text = "Empty",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.outline,
                                textAlign = TextAlign.Center,
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
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Retainer ${index + 1}",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )
            if (retainer.name.isNotEmpty()) {
                Text(
                    text = retainer.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    StatBox(label = "HP", value = "${retainer.currentHP}/${retainer.maxHP}")
                    Spacer(modifier = Modifier.width(8.dp))
                    StatBox(label = "HD", value = retainer.hitDice.toString())
                    Spacer(modifier = Modifier.width(8.dp))
                    StatBox(label = "DF", value = retainer.defense.toString())
                    Spacer(modifier = Modifier.width(8.dp))
                    StatBox(label = "MV", value = retainer.movement.toString())
                }
                if (retainer.keywords.isNotEmpty()) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Keywords",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary,
                            textAlign = TextAlign.Center
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
                            verticalArrangement = Arrangement.spacedBy(4.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            rows.forEach { rowKeywords ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
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
                                        if (keyword != rowKeywords.last()) {
                                            Spacer(modifier = Modifier.width(4.dp))
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
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Notes",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = retainer.notes,
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                Text(
                    text = "Empty",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.outline,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
