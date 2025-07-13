package com.netartisancollective.whitehacktools.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

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
            Spacer(modifier = Modifier.height(8.dp))
            DetailTextArea(
                label = "Notes",
                value = notes
            )
        }
    }
}
