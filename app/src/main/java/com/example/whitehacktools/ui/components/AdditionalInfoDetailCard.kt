package com.example.whitehacktools.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun AdditionalInfoDetailCard(
    experience: Int,
    corruption: Int,
    notes: String,
    modifier: Modifier = Modifier
) {
    SectionCard(
        title = "Additional Info",
        modifier = modifier
    ) {
        DetailItem(
            label = "Experience",
            value = experience.toString()
        )
        
        DetailItem(
            label = "Corruption",
            value = corruption.toString()
        )
        
        if (notes.isNotBlank()) {
            DetailItem(
                label = "Notes",
                value = notes
            )
        }
    }
}
