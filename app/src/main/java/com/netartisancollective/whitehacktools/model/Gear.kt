package com.netartisancollective.whitehacktools.model

import java.util.UUID
import kotlinx.serialization.Serializable
import kotlinx.serialization.Contextual

@Serializable
data class Gear(
    @Contextual
    val id: String = UUID.randomUUID().toString(),
    val name: String = "",
    val weight: String = "Minor", // Can be "No Size", "Minor", "Regular", "Heavy"
    val special: String = "",
    val quantity: Int = 1,
    val isEquipped: Boolean = false,
    val isStashed: Boolean = false,
    val isMagical: Boolean = false,
    val isCursed: Boolean = false,
    val isContainer: Boolean = false
)
