package com.example.whitehacktools.ui.components

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
import com.example.whitehacktools.model.*

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
                    value = attunement.type.name,
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
