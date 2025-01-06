package com.example.whitehacktools.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ArmorFormCard(
    modifier: Modifier = Modifier
) {
    SectionCard(
        title = "Armor",
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "Placeholder for armor form")
            Text(text = "Form fields for armor details will go here")
            Text(text = "Add/Remove armor functionality will be added")
        }
    }
}
