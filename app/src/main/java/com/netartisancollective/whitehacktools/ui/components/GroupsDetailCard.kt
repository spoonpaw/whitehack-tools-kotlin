package com.netartisancollective.whitehacktools.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.netartisancollective.whitehacktools.model.PlayerCharacter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupsDetailCard(
    character: PlayerCharacter,
    modifier: Modifier = Modifier
) {
    SectionCard(
        title = "Groups",
        modifier = modifier
    ) {
        DetailItem(
            label = "Vocation",
            value = character.effectiveVocation.ifEmpty { "None" }
        )
        
        DetailItem(
            label = "Species",
            value = character.effectiveSpecies.ifEmpty { "None" }
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
                Text(
                    text = "Affiliations",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    modifier = Modifier.padding(start = 4.dp)
                )

                if (character.effectiveAffiliations.isEmpty()) {
                    Text(
                        text = "No affiliations added",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        modifier = Modifier.padding(start = 4.dp)
                    )
                } else {
                    character.effectiveAffiliations.forEach { affiliation ->
                        DetailItem(
                            label = "",
                            value = affiliation,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}
