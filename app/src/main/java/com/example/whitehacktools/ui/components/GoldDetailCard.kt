package com.example.whitehacktools.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun GoldDetailCard(
    goldOnHand: Int,
    stashedGold: Int,
    modifier: Modifier = Modifier
) {
    val totalGold = goldOnHand + stashedGold

    SectionCard(
        title = "Gold",
        modifier = modifier
    ) {
        DetailItem(
            label = "Gold on Hand",
            value = goldOnHand.toString()
        )
        
        DetailItem(
            label = "Stashed Gold",
            value = stashedGold.toString()
        )
        
        DetailItem(
            label = "Total Gold",
            value = totalGold.toString()
        )
    }
}
