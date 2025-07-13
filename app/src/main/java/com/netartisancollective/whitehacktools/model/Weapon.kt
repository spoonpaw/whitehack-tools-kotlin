package com.netartisancollective.whitehacktools.model

import java.util.UUID
import kotlinx.serialization.Serializable
import kotlinx.serialization.Contextual

@Serializable
data class Weapon(
    @Contextual
    val id: String = UUID.randomUUID().toString(),
    val name: String = "",
    val damage: String = "",
    val weight: String = "Regular",
    val range: String = "",
    val rateOfFire: String = "",
    val special: String = "",
    val isEquipped: Boolean = false,
    val isStashed: Boolean = false,
    val isMagical: Boolean = false,
    val isCursed: Boolean = false,
    val bonus: Int = 0,
    val quantity: Int = 1
)
