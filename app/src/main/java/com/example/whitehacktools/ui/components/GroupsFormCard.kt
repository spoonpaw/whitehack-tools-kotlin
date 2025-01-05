package com.example.whitehacktools.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import com.example.whitehacktools.model.AttributeGroupPair
import com.example.whitehacktools.model.GroupType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupsFormCard(
    vocation: String,
    onVocationChange: (String) -> Unit,
    species: String,
    onSpeciesChange: (String) -> Unit,
    affiliations: List<String>,
    onAffiliationsChange: (List<String>) -> Unit,
    attributeGroupPairs: List<AttributeGroupPair>,
    onAttributeGroupPairsChange: (List<AttributeGroupPair>) -> Unit,
    availableAttributes: List<String>,
    modifier: Modifier = Modifier
) {
    var showAddAffiliationDialog by remember { mutableStateOf(false) }
    var newAffiliation by remember { mutableStateOf("") }
    var showAddPairDialog by remember { mutableStateOf(false) }
    var selectedAttribute by remember { mutableStateOf("") }
    var selectedGroupType by remember { mutableStateOf<GroupType?>(null) }
    var selectedGroupName by remember { mutableStateOf("") }

    SectionCard(
        title = "Groups",
        modifier = modifier
    ) {
        OutlinedTextField(
            value = vocation,
            onValueChange = { newVocation ->
                onVocationChange(newVocation)
                // Remove pairs using vocation if it's cleared, otherwise update them
                val updatedPairs = if (newVocation.isBlank()) {
                    attributeGroupPairs.filter { it.groupType != GroupType.Vocation }
                } else {
                    attributeGroupPairs.map { pair ->
                        if (pair.groupType == GroupType.Vocation) {
                            pair.copy(groupName = newVocation)
                        } else {
                            pair
                        }
                    }
                }
                onAttributeGroupPairsChange(updatedPairs)
            },
            label = { Text("Vocation") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
            )
        )

        OutlinedTextField(
            value = species,
            onValueChange = { newSpecies ->
                onSpeciesChange(newSpecies)
                // Remove pairs using species if it's cleared, otherwise update them
                val updatedPairs = if (newSpecies.isBlank()) {
                    attributeGroupPairs.filter { it.groupType != GroupType.Species }
                } else {
                    attributeGroupPairs.map { pair ->
                        if (pair.groupType == GroupType.Species) {
                            pair.copy(groupName = newSpecies)
                        } else {
                            pair
                        }
                    }
                }
                onAttributeGroupPairsChange(updatedPairs)
            },
            label = { Text("Species") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
            )
        )

        OutlinedCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
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
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Affiliations",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        modifier = Modifier.padding(start = 4.dp)
                    )
                    FilledTonalButton(
                        onClick = { showAddAffiliationDialog = true },
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add Affiliation"
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Add")
                    }
                }

                if (affiliations.isEmpty()) {
                    Text(
                        text = "No affiliations added",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        modifier = Modifier.padding(start = 4.dp)
                    )
                } else {
                    affiliations.forEachIndexed { index, affiliation ->
                        OutlinedTextField(
                            value = affiliation,
                            onValueChange = { },
                            readOnly = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                                disabledBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                            ),
                            trailingIcon = {
                                IconButton(
                                    onClick = {
                                        val removedAffiliation = affiliations[index]
                                        onAffiliationsChange(affiliations.filterIndexed { i, _ -> i != index })
                                        // Remove any attribute-group pairs that use this affiliation
                                        val updatedPairs = attributeGroupPairs.filter { pair ->
                                            !(pair.groupType == GroupType.Affiliation && pair.groupName == removedAffiliation)
                                        }
                                        onAttributeGroupPairsChange(updatedPairs)
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Remove Affiliation",
                                        tint = MaterialTheme.colorScheme.error
                                    )
                                }
                            }
                        )
                    }
                }
            }
        }

        OutlinedCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
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
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Attribute-Group Pairs",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        modifier = Modifier.padding(start = 4.dp)
                    )
                    FilledTonalButton(
                        onClick = { showAddPairDialog = true },
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add Pair"
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Add")
                    }
                }

                if (attributeGroupPairs.isEmpty()) {
                    Text(
                        text = "No attribute-group pairs added",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        modifier = Modifier.padding(start = 4.dp)
                    )
                } else {
                    attributeGroupPairs.forEachIndexed { index, pair ->
                        OutlinedTextField(
                            value = "${pair.attributeName} - ${pair.groupName}",
                            onValueChange = { },
                            readOnly = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                                disabledBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                            ),
                            trailingIcon = {
                                IconButton(
                                    onClick = {
                                        onAttributeGroupPairsChange(attributeGroupPairs.filterIndexed { i, _ -> i != index })
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Remove Pair",
                                        tint = MaterialTheme.colorScheme.error
                                    )
                                }
                            }
                        )
                    }
                }
            }
        }

        if (showAddAffiliationDialog) {
            AlertDialog(
                onDismissRequest = { 
                    showAddAffiliationDialog = false
                    newAffiliation = ""
                },
                title = { Text("Add Affiliation") },
                text = {
                    OutlinedTextField(
                        value = newAffiliation,
                        onValueChange = { newAffiliation = it },
                        label = { Text("Affiliation") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                        )
                    )
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            if (newAffiliation.isNotBlank()) {
                                onAffiliationsChange(affiliations + newAffiliation)
                                showAddAffiliationDialog = false
                                newAffiliation = ""
                            }
                        }
                    ) {
                        Text("Add")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { 
                            showAddAffiliationDialog = false
                            newAffiliation = ""
                        }
                    ) {
                        Text("Cancel")
                    }
                }
            )
        }

        if (showAddPairDialog) {
            AlertDialog(
                onDismissRequest = { 
                    showAddPairDialog = false
                    selectedAttribute = ""
                    selectedGroupType = null
                    selectedGroupName = ""
                },
                title = { Text("Add Attribute-Group Pair") },
                text = {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Attribute dropdown
                        var attributeExpanded by remember { mutableStateOf(false) }
                        ExposedDropdownMenuBox(
                            expanded = attributeExpanded,
                            onExpandedChange = { attributeExpanded = !attributeExpanded }
                        ) {
                            OutlinedTextField(
                                value = selectedAttribute,
                                onValueChange = { },
                                readOnly = true,
                                label = { Text("Attribute") },
                                modifier = Modifier.fillMaxWidth().menuAnchor(),
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                                )
                            )
                            ExposedDropdownMenu(
                                expanded = attributeExpanded,
                                onDismissRequest = { attributeExpanded = false }
                            ) {
                                availableAttributes.forEach { attribute ->
                                    DropdownMenuItem(
                                        text = { Text(attribute) },
                                        onClick = { 
                                            selectedAttribute = attribute
                                            attributeExpanded = false
                                        }
                                    )
                                }
                            }
                        }

                        // Group dropdown
                        var groupExpanded by remember { mutableStateOf(false) }
                        val availableGroups = buildList {
                            if (vocation.isNotEmpty()) add(GroupType.Vocation to vocation)
                            if (species.isNotEmpty()) add(GroupType.Species to species)
                            addAll(affiliations.map { GroupType.Affiliation to it })
                        }

                        ExposedDropdownMenuBox(
                            expanded = groupExpanded,
                            onExpandedChange = { groupExpanded = !groupExpanded }
                        ) {
                            OutlinedTextField(
                                value = selectedGroupName,
                                onValueChange = { },
                                readOnly = true,
                                label = { Text("Group") },
                                modifier = Modifier.fillMaxWidth().menuAnchor(),
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                                )
                            )
                            ExposedDropdownMenu(
                                expanded = groupExpanded,
                                onDismissRequest = { groupExpanded = false }
                            ) {
                                availableGroups.forEach { (type, name) ->
                                    DropdownMenuItem(
                                        text = { Text(name) },
                                        onClick = { 
                                            selectedGroupType = type
                                            selectedGroupName = name
                                            groupExpanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            if (selectedAttribute.isNotBlank() && selectedGroupType != null && selectedGroupName.isNotBlank()) {
                                onAttributeGroupPairsChange(attributeGroupPairs + AttributeGroupPair(
                                    attributeName = selectedAttribute,
                                    groupType = selectedGroupType!!,
                                    groupName = selectedGroupName
                                ))
                                showAddPairDialog = false
                                selectedAttribute = ""
                                selectedGroupType = null
                                selectedGroupName = ""
                            }
                        }
                    ) {
                        Text("Add")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = { 
                            showAddPairDialog = false
                            selectedAttribute = ""
                            selectedGroupType = null
                            selectedGroupName = ""
                        }
                    ) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}
