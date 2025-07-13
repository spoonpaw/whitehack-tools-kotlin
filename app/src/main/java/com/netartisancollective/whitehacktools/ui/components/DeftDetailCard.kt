package com.netartisancollective.whitehacktools.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import com.netartisancollective.whitehacktools.model.*

@Composable
private fun AttunementFieldCard(
    label: String,
    value: String,
    color: Color = MaterialTheme.colorScheme.onSurface,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                color = color
            )
        }
    }
}

@Composable
private fun EmptyAttunementCard(
    title: String,
    titleColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.1f)
        ),
        border = BorderStroke(1.dp, titleColor.copy(alpha = 0.3f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = titleColor,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Empty",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
        }
    }
}

@Composable
private fun AttunementDetailCard(
    attunement: Attunement,
    title: String,
    titleColor: Color,
    modifier: Modifier = Modifier
) {
    if (attunement.name.isEmpty()) {
        EmptyAttunementCard(
            title = title,
            titleColor = titleColor,
            modifier = modifier
        )
    } else {
        Card(
            modifier = modifier,
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.1f)
            ),
            border = BorderStroke(1.dp, titleColor.copy(alpha = 0.3f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Title
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = titleColor,
                    fontWeight = FontWeight.Bold
                )

                // Name
                AttunementFieldCard(
                    label = "Name",
                    value = attunement.name,
                    modifier = Modifier.fillMaxWidth()
                )

                // Type
                AttunementFieldCard(
                    label = "Type",
                    value = attunement.type.name.lowercase().replaceFirstChar { it.uppercase() },
                    modifier = Modifier.fillMaxWidth()
                )

                // Lost Status
                AttunementFieldCard(
                    label = "Lost Status",
                    value = if (attunement.isLost) "Lost" else "Not Lost",
                    color = if (attunement.isLost) 
                        MaterialTheme.colorScheme.error 
                    else 
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    modifier = Modifier.fillMaxWidth()
                )

                // Active Status
                AttunementFieldCard(
                    label = "Active Status",
                    value = if (attunement.isActive) "Active" else "Inactive",
                    color = if (attunement.isActive) 
                        MaterialTheme.colorScheme.primary 
                    else 
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun DeftFeatureRow(
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
                            text = "Masters of technique and skill who rely on superior training and expertise. At level 1, must choose a vocation group without marking it next to an attribute. Whether as thieves, wandering monks, spies, marksmen, rangers, or assassins, they excel through precision and finesse.",
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
                            DeftFeatureRow(
                                title = "Double Roll",
                                description = "When properly equipped, gain positive double roll for tasks and attacks matching your vocation"
                            )
                            DeftFeatureRow(
                                title = "Combat Advantage",
                                description = "May swap combat advantage for double damage when vocation is relevant (e.g. trader defending cargo, assassin striking from shadows)"
                            )
                            DeftFeatureRow(
                                title = "Weapon Proficiency",
                                description = "Combat vocation: +1 damage and Defense from off-hand weapon. All: -2 Attack Value with non-attuned two-handed melee weapons"
                            )
                            DeftFeatureRow(
                                title = "Light Armor",
                                description = "Must use light armor (studded leather or lighter) and no shield to maintain expertise. Required for slot abilities and double damage"
                            )
                            DeftFeatureRow(
                                title = "Non-Combat Vocation",
                                description = "Once per session, may turn a successful task roll into a critical success"
                            )
                        }
                    }
                }

                // Attunement Rules Card
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
                            text = "Attunements",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "Each slot holds two attunements (teacher, item, vehicle, pet, or place). Only one can be active at a time.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "• Switching attunements takes a day of practice",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "• Active attunements can be invoked once per day",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "• Hard tasks succeed automatically, nigh impossible tasks become regular rolls",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "• Lost attunements become keywords with +1 to related tasks when active",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "Examples: Ranger with trained dog or ancestral lands, Monk with master or special bow",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

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

                                // Primary
                                AttunementDetailCard(
                                    attunement = slot.primaryAttunement,
                                    title = "Primary Attunement",
                                    titleColor = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.fillMaxWidth()
                                )

                                // Secondary
                                AttunementDetailCard(
                                    attunement = slot.secondaryAttunement,
                                    title = "Secondary Attunement",
                                    titleColor = MaterialTheme.colorScheme.secondary,
                                    modifier = Modifier.fillMaxWidth()
                                )

                                // Tertiary
                                if (slot.hasTertiaryAttunement) {
                                    AttunementDetailCard(
                                        attunement = slot.tertiaryAttunement,
                                        title = "Tertiary Attunement",
                                        titleColor = MaterialTheme.colorScheme.tertiary,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }

                                // Quaternary
                                if (slot.hasQuaternaryAttunement) {
                                    AttunementDetailCard(
                                        attunement = slot.quaternaryAttunement,
                                        title = "Quaternary Attunement",
                                        titleColor = MaterialTheme.colorScheme.error,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }

                                AttunementFieldCard(
                                    label = "Attunement Slot ${index + 1} Daily Power Status",
                                    value = if (slot.hasUsedDailyPower) "Used" else "Available",
                                    color = if (slot.hasUsedDailyPower) 
                                        MaterialTheme.colorScheme.error 
                                    else 
                                        MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
