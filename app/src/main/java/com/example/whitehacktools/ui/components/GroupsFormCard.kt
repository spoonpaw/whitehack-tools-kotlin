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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupsFormCard(
    vocation: String,
    onVocationChange: (String) -> Unit,
    species: String,
    onSpeciesChange: (String) -> Unit,
    affiliations: List<String>,
    onAffiliationsChange: (List<String>) -> Unit,
    modifier: Modifier = Modifier
) {
    var showAddAffiliationDialog by remember { mutableStateOf(false) }
    var newAffiliation by remember { mutableStateOf("") }

    SectionCard(
        title = "Groups",
        modifier = modifier
    ) {
        OutlinedTextField(
            value = vocation,
            onValueChange = onVocationChange,
            label = { Text("Vocation") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
            )
        )

        OutlinedTextField(
            value = species,
            onValueChange = onSpeciesChange,
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
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
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
                                        onAffiliationsChange(affiliations.filterIndexed { i, _ -> i != index })
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
    }
}
