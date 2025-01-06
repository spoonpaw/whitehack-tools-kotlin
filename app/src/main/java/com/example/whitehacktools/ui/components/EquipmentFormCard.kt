package com.example.whitehacktools.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun EquipmentFormCard(
    modifier: Modifier = Modifier
) {
    SectionCard(
        title = "Equipment",
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "Placeholder for equipment form")
            Text(text = "Form fields for equipment details will go here")
            Text(text = "Add/Remove equipment functionality will be added")
        }
    }
}
