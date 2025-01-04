package com.example.whitehacktools.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguagesFormCard(
    languages: List<String>,
    onLanguagesChange: (List<String>) -> Unit,
    modifier: Modifier = Modifier
) {
    var showAddLanguageDialog by remember { mutableStateOf(false) }
    var newLanguage by remember { mutableStateOf("") }

    SectionCard(
        title = "Languages",
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilledTonalButton(
                onClick = { showAddLanguageDialog = true },
                modifier = Modifier.align(Alignment.End)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Language"
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("Add")
            }

            if (languages.isEmpty()) {
                Text(
                    text = "No languages added",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    modifier = Modifier.padding(start = 4.dp)
                )
            } else {
                languages.forEachIndexed { index, language ->
                    OutlinedTextField(
                        value = language,
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
                                    onLanguagesChange(languages.filterIndexed { i, _ -> i != index })
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Remove Language",
                                    tint = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    )
                }
            }
        }
    }

    if (showAddLanguageDialog) {
        AlertDialog(
            onDismissRequest = { 
                showAddLanguageDialog = false
                newLanguage = ""
            },
            title = { Text("Add Language") },
            text = {
                OutlinedTextField(
                    value = newLanguage,
                    onValueChange = { newLanguage = it },
                    label = { Text("Language") },
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
                        if (newLanguage.isNotBlank()) {
                            onLanguagesChange(languages + newLanguage)
                            showAddLanguageDialog = false
                            newLanguage = ""
                        }
                    }
                ) {
                    Text("Add")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { 
                        showAddLanguageDialog = false
                        newLanguage = ""
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}
