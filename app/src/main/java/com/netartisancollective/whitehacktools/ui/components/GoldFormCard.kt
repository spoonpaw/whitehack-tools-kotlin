package com.netartisancollective.whitehacktools.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GoldFormCard(
    coinsOnHand: String,
    onCoinsOnHandChange: (String) -> Unit,
    stashedCoins: String,
    onStashedCoinsChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val totalGold = (coinsOnHand.toIntOrNull() ?: 0) + (stashedCoins.toIntOrNull() ?: 0)

    SectionCard(
        title = "Gold",
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FormField(
                value = coinsOnHand,
                onValueChange = onCoinsOnHandChange,
                label = "Gold on Hand",
                keyboardType = KeyboardType.Number
            )
            FormField(
                value = stashedCoins,
                onValueChange = onStashedCoinsChange,
                label = "Stashed Gold",
                keyboardType = KeyboardType.Number
            )
            DetailItem(
                label = "Total Gold",
                value = totalGold.toString()
            )
        }
    }
}
