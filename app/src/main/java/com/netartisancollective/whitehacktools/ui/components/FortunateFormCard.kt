package com.netartisancollective.whitehacktools.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.ui.draw.clip
import com.netartisancollective.whitehacktools.model.*
import com.netartisancollective.whitehacktools.utilities.AdvancementTables

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FortunateFormCard(
    level: Int,
    fortunateOptions: FortunateOptions,
    onFortunateOptionsChanged: (FortunateOptions) -> Unit,
    modifier: Modifier = Modifier
) {
    val availableSlots = remember(level) {
        AdvancementTables.stats("Fortunate", level).slots
    }

    // Get the retainers to display based on available slots
    val displayedRetainers = remember(fortunateOptions.retainers, availableSlots) {
        val retainersList = fortunateOptions.retainers.toMutableList()
        while (retainersList.size < availableSlots) {
            retainersList.add(Retainer())
        }
        retainersList.take(availableSlots)
    }

    SectionCard(
        title = "Fortunate Features",
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
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
                    // Standing
                    OutlinedTextField(
                        value = fortunateOptions.standing,
                        onValueChange = { onFortunateOptionsChanged(fortunateOptions.copy(standing = it)) },
                        label = { Text("Standing") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Fortune Usage
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Fortune Power",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Medium
                        )
                        Switch(
                            checked = fortunateOptions.hasUsedFortune,
                            onCheckedChange = { onFortunateOptionsChanged(fortunateOptions.copy(hasUsedFortune = it)) }
                        )
                    }
                    Text(
                        text = if (fortunateOptions.hasUsedFortune) {
                            "Fortune power has been used this session"
                        } else {
                            "Fortune power is available"
                        },
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }

            // Signature Object
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
                        text = "Signature Object",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Medium
                    )
                    OutlinedTextField(
                        value = fortunateOptions.signatureObject.name,
                        onValueChange = { 
                            onFortunateOptionsChanged(
                                fortunateOptions.copy(
                                    signatureObject = fortunateOptions.signatureObject.copy(name = it)
                                )
                            )
                        },
                        label = { Text("Name") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            // Retainers Section
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
                    // Header
                    Text(
                        text = "Retainers",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Medium
                    )

                    // Individual Retainer Cards
                    Column(
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        displayedRetainers.forEachIndexed { index, retainer ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
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
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "Retainer ${index + 1}",
                                            style = MaterialTheme.typography.titleSmall,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                    RetainerCard(
                                        retainer = retainer,
                                        onRetainerChanged = { updatedRetainer ->
                                            val updatedRetainers = fortunateOptions.retainers.toMutableList()
                                            while (updatedRetainers.size <= index) {
                                                updatedRetainers.add(Retainer())
                                            }
                                            updatedRetainers[index] = updatedRetainer
                                            onFortunateOptionsChanged(fortunateOptions.copy(retainers = updatedRetainers))
                                        },
                                        onDeleteRetainer = {
                                            val updatedRetainers = fortunateOptions.retainers.toMutableList()
                                            if (index < updatedRetainers.size) {
                                                updatedRetainers[index] = Retainer() // Clear instead of remove to maintain indices
                                                onFortunateOptionsChanged(fortunateOptions.copy(retainers = updatedRetainers))
                                            }
                                        },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp)
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RetainerCard(
    retainer: Retainer,
    onRetainerChanged: (Retainer) -> Unit,
    onDeleteRetainer: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showKeywordDialog by remember { mutableStateOf(false) }
    var newKeyword by remember { mutableStateOf("") }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = retainer.name,
            onValueChange = { onRetainerChanged(retainer.copy(name = it)) },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )

        // Stats Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = retainer.currentHP.toString(),
                onValueChange = { newValue -> 
                    val newHP = newValue.toIntOrNull() ?: 0
                    onRetainerChanged(retainer.copy(currentHP = newHP))
                },
                label = { Text("Current HP") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f)
            )
            OutlinedTextField(
                value = retainer.maxHP.toString(),
                onValueChange = { newValue -> 
                    val newHP = newValue.toIntOrNull() ?: 0
                    onRetainerChanged(retainer.copy(maxHP = newHP))
                },
                label = { Text("Max HP") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = retainer.hitDice.toString(),
                onValueChange = { newValue -> 
                    val newHD = newValue.toIntOrNull() ?: 1
                    onRetainerChanged(retainer.copy(hitDice = newHD))
                },
                label = { Text("HD") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f)
            )
            OutlinedTextField(
                value = retainer.defense.toString(),
                onValueChange = { newValue -> 
                    val newDF = newValue.toIntOrNull() ?: 10
                    onRetainerChanged(retainer.copy(defense = newDF))
                },
                label = { Text("DF") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f)
            )
            OutlinedTextField(
                value = retainer.movement.toString(),
                onValueChange = { newValue -> 
                    val newMV = newValue.toIntOrNull() ?: 30
                    onRetainerChanged(retainer.copy(movement = newMV))
                },
                label = { Text("MV") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.weight(1f)
            )
        }

        // Keywords
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Keywords",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Medium
            )
            
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                val rows = remember(retainer.keywords) {
                    val keywords = retainer.keywords.toSet().toList()
                    val rows = mutableListOf<List<String>>()
                    var currentRow = mutableListOf<String>()
                    var currentWidth = 0
                    for (keyword in keywords) {
                        val keywordWidth = keyword.length * 10
                        if (currentWidth + keywordWidth > 200) {
                            rows.add(currentRow)
                            currentRow = mutableListOf()
                            currentWidth = 0
                        }
                        currentRow.add(keyword)
                        currentWidth += keywordWidth
                    }
                    if (currentRow.isNotEmpty()) {
                        rows.add(currentRow)
                    }
                    rows
                }
                rows.forEach { rowKeywords ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally)
                    ) {
                        rowKeywords.forEach { keyword ->
                            Box(
                                modifier = Modifier
                                    .wrapContentWidth()
                                    .clip(RoundedCornerShape(16.dp))
                                    .border(
                                        width = 1.dp,
                                        color = MaterialTheme.colorScheme.outline,
                                        shape = RoundedCornerShape(16.dp)
                                    )
                                    .background(MaterialTheme.colorScheme.surfaceVariant)
                                    .clickable {
                                        onRetainerChanged(
                                            retainer.copy(
                                                keywords = retainer.keywords - keyword
                                            )
                                        )
                                    }
                            ) {
                                Row(
                                    modifier = Modifier
                                        .padding(start = 12.dp, end = 8.dp, top = 6.dp, bottom = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = keyword,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier = Modifier.weight(1f, fill = false)
                                    )
                                    Box(
                                        modifier = Modifier
                                            .size(24.dp)
                                            .padding(4.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Remove Keyword",
                                            modifier = Modifier.matchParentSize(),
                                            tint = MaterialTheme.colorScheme.error
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Button(
                onClick = { showKeywordDialog = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Keyword"
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Add Keyword")
            }
        }

        OutlinedTextField(
            value = retainer.notes,
            onValueChange = { onRetainerChanged(retainer.copy(notes = it)) },
            label = { Text("Notes") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 2
        )
    }

    if (showKeywordDialog) {
        AlertDialog(
            onDismissRequest = { showKeywordDialog = false },
            title = { Text("Add Keyword") },
            text = {
                OutlinedTextField(
                    value = newKeyword,
                    onValueChange = { newKeyword = it },
                    label = { Text("Keyword") },
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (newKeyword.isNotBlank()) {
                            onRetainerChanged(
                                retainer.copy(
                                    keywords = retainer.keywords + newKeyword.trim()
                                )
                            )
                            newKeyword = ""
                        }
                        showKeywordDialog = false
                    }
                ) {
                    Text("Add")
                }
            },
            dismissButton = {
                TextButton(onClick = { showKeywordDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
