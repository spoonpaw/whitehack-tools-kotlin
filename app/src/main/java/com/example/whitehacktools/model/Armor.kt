package com.example.whitehacktools.model

import java.util.UUID
import kotlinx.serialization.Serializable
import kotlinx.serialization.Contextual

@Serializable
data class Armor(
    @Contextual
    val id: String = UUID.randomUUID().toString(),
    val name: String = "",
    val df: Int = 0,
    val weight: Int = 1,
    val special: String = "",
    val isShield: Boolean = false,
    val isEquipped: Boolean = false,
    val isStashed: Boolean = false,
    val isMagical: Boolean = false,
    val isCursed: Boolean = false,
    val bonus: Int = 0,
    val quantity: Int = 1
)

object ArmorData {
    val armors = listOf(
        Armor(name = "Shield", df = 1, weight = 1, special = "", isShield = true),
        Armor(name = "Cloth", df = 1, weight = 1, special = "", isShield = false),
        Armor(name = "Leather", df = 2, weight = 2, special = "", isShield = false),
        Armor(name = "Hard leather", df = 3, weight = 3, special = "", isShield = false),
        Armor(name = "Chainmail", df = 4, weight = 4, special = "", isShield = false),
        Armor(name = "Splint mail", df = 5, weight = 5, special = "", isShield = false),
        Armor(name = "Full plate", df = 6, weight = 6, special = "", isShield = false)
    )
}
