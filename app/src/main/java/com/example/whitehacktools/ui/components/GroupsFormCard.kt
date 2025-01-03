package com.example.whitehacktools.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun GroupsFormCard(
    vocation: String,
    onVocationChange: (String) -> Unit,
    species: String,
    onSpeciesChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    SectionCard(
        title = "Groups",
        modifier = modifier
    ) {
        FormField(
            value = vocation,
            onValueChange = onVocationChange,
            label = "Vocation"
        )
        FormField(
            value = species,
            onValueChange = onSpeciesChange,
            label = "Species"
        )
    }
}
