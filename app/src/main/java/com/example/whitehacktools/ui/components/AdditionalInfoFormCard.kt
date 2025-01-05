package com.example.whitehacktools.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun AdditionalInfoFormCard(
    experience: String,
    onExperienceChange: (String) -> Unit,
    corruption: String,
    onCorruptionChange: (String) -> Unit,
    notes: String,
    onNotesChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    SectionCard(
        title = "Additional Info",
        modifier = modifier
    ) {
        FormField(
            value = experience,
            onValueChange = { value ->
                if (value.isEmpty() || value.toIntOrNull() != null) {
                    onExperienceChange(value)
                }
            },
            label = "Experience",
            keyboardType = KeyboardType.Number,
            numberOnly = true
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        FormField(
            value = corruption,
            onValueChange = { value ->
                if (value.isEmpty() || value.toIntOrNull() != null) {
                    onCorruptionChange(value)
                }
            },
            label = "Corruption",
            keyboardType = KeyboardType.Number,
            numberOnly = true
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        OutlinedTextField(
            value = notes,
            onValueChange = onNotesChange,
            label = { Text("Notes") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3,
            maxLines = 5,
            shape = MaterialTheme.shapes.medium
        )
    }
}
