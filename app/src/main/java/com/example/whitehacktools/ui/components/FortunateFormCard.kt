package com.example.whitehacktools.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.border
import com.example.whitehacktools.model.*
import com.example.whitehacktools.utilities.AdvancementTables

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

            // Retainers Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Retainers",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "${fortunateOptions.retainers.count { it.name.isNotEmpty() }}/$availableSlots",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Retainers
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                displayedRetainers.forEachIndexed { index, retainer ->
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
                        modifier = Modifier.fillMaxWidth()
                    )
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

    Card(
        modifier = modifier,
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Retainer",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Medium
                )
                IconButton(onClick = onDeleteRetainer) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete Retainer"
                    )
                }
            }

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
                
                var currentRowWidth by remember { mutableStateOf(0f) }
                var currentRow by remember { mutableStateOf(mutableListOf<String>()) }
                val rows = remember(retainer.keywords) {
                    val result = mutableListOf<List<String>>()
                    currentRow.clear()
                    currentRowWidth = 0f
                    
                    retainer.keywords.forEach { keyword ->
                        // Approximate width calculation: 12dp per character + padding + icon
                        val itemWidth = keyword.length * 12f + 60f // 60dp for padding and icon
                        
                        if (currentRowWidth + itemWidth > 360f) { // Assuming max width of 360dp
                            if (currentRow.isNotEmpty()) {
                                result.add(currentRow.toList())
                                currentRow.clear()
                                currentRowWidth = itemWidth
                                currentRow.add(keyword)
                            } else {
                                // If single item is wider than container, put it on its own row
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
                                    modifier = Modifier
                                        .wrapContentWidth()
                                        .border(
                                            width = 1.dp,
                                            color = MaterialTheme.colorScheme.outline,
                                            shape = RoundedCornerShape(16.dp)
                                        ),
                                    shape = RoundedCornerShape(16.dp),
                                    color = MaterialTheme.colorScheme.surfaceVariant,
                                    onClick = {
                                        onRetainerChanged(
                                            retainer.copy(
                                                keywords = retainer.keywords - keyword
                                            )
                                        )
                                    }
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .padding(start = 12.dp, top = 6.dp, bottom = 6.dp, end = 8.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = keyword,
                                            modifier = Modifier
                                                .weight(1f, fill = false)
                                                .padding(end = 8.dp),
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                        IconButton(
                                            onClick = {
                                                onRetainerChanged(
                                                    retainer.copy(
                                                        keywords = retainer.keywords - keyword
                                                    )
                                                )
                                            },
                                            modifier = Modifier.size(24.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Delete,
                                                contentDescription = "Remove Keyword",
                                                modifier = Modifier.size(16.dp),
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
