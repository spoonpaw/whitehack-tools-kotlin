package com.netartisancollective.whitehacktools.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

sealed class TopBarAction {
    data class IconAction(
        val icon: androidx.compose.ui.graphics.vector.ImageVector,
        val contentDescription: String,
        val onClick: () -> Unit
    ) : TopBarAction()
    
    data class TextAction(
        val text: String,
        val onClick: () -> Unit,
        val enabled: Boolean = true,
        val isPrimary: Boolean = false
    ) : TopBarAction()
    
    data class TonalButtonAction(
        val text: String,
        val onClick: () -> Unit,
        val enabled: Boolean = true
    ) : TopBarAction()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WhitehackTopAppBar(
    title: String,
    onNavigateBack: (() -> Unit)? = null,
    actions: List<TopBarAction> = emptyList(),
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = { Text(title) },
        navigationIcon = {
            if (onNavigateBack != null) {
                IconButton(
                    onClick = onNavigateBack,
                    modifier = Modifier.padding(8.dp)
                ) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        },
        actions = {
            actions.forEach { action ->
                when (action) {
                    is TopBarAction.IconAction -> {
                        IconButton(
                            onClick = action.onClick,
                            modifier = Modifier.padding(horizontal = 4.dp)
                        ) {
                            Icon(
                                action.icon,
                                contentDescription = action.contentDescription,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                    is TopBarAction.TextAction -> {
                        TextButton(
                            onClick = action.onClick,
                            enabled = action.enabled,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        ) {
                            Text(
                                text = action.text,
                                fontSize = if (action.isPrimary) 18.sp else 16.sp,
                                fontWeight = if (action.isPrimary) FontWeight.Bold else FontWeight.Medium
                            )
                        }
                    }
                    is TopBarAction.TonalButtonAction -> {
                        FilledTonalButton(
                            onClick = action.onClick,
                            enabled = action.enabled,
                            modifier = Modifier.padding(horizontal = 4.dp)
                        ) {
                            Text(action.text)
                        }
                    }
                }
            }
        },
        modifier = modifier,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    )
}
