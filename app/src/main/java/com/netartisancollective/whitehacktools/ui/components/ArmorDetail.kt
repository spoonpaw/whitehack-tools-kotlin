package com.netartisancollective.whitehacktools.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import com.netartisancollective.whitehacktools.model.Armor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArmorDetail(
    armor: Armor,
    onArmorChange: (Armor) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isEditing by remember { mutableStateOf(false) }

    if (isEditing) {
        ArmorEditRow(
            armor = armor,
            onSave = { 
                onArmorChange(it)
                isEditing = false
            },
            onCancel = { isEditing = false },
            modifier = modifier
        )
    } else {
        AlertDialog(
            onDismissRequest = onDismiss,
            confirmButton = {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = { isEditing = true },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Edit")
                    }
                    Button(
                        onClick = { 
                            onArmorChange(armor.copy(
                                isEquipped = !armor.isEquipped,
                                isStashed = if (!armor.isEquipped) false else armor.isStashed
                            ))
                        },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(if (armor.isEquipped) "Unequip" else "Equip")
                    }
                }
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Header with name and quantity
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = armor.name,
                            style = MaterialTheme.typography.titleLarge
                        )
                        if (armor.quantity > 1) {
                            Text(
                                text = "×${armor.quantity}",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    // Status row (Equipped/Stashed)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        if (armor.isEquipped) {
                            Text(
                                text = "Equipped",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        if (armor.isStashed) {
                            Text(
                                text = "Stashed",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }
                        if (!armor.isEquipped && !armor.isStashed) {
                            Text(
                                text = "On Person",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Divider()

                    // Main stats
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        // Defense and Weight row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Defense: ${armor.df}",
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Text(
                                text = "Weight: ${armor.weight}",
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }

                        // Shield status
                        if (armor.isShield) {
                            Text(
                                text = "Shield",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    // Special properties section
                    if (armor.special.isNotBlank() || armor.isMagical || armor.isCursed || armor.bonus != 0) {
                        Divider()
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            // Magical/Cursed status
                            if (armor.isMagical || armor.isCursed) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    if (armor.isMagical) {
                                        Text(
                                            text = "Magical",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                    if (armor.isCursed) {
                                        Text(
                                            text = "Cursed",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.error
                                        )
                                    }
                                }
                            }

                            // Bonus/Penalty
                            if (armor.bonus != 0) {
                                Text(
                                    text = if (armor.bonus > 0) "+${armor.bonus}" else armor.bonus.toString(),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = if (armor.bonus > 0) 
                                        MaterialTheme.colorScheme.primary 
                                    else 
                                        MaterialTheme.colorScheme.error
                                )
                            }

                            // Special text
                            if (armor.special.isNotBlank()) {
                                Text(
                                    text = armor.special,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontStyle = FontStyle.Italic
                                    ),
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            },
            modifier = modifier
        )
    }
}
