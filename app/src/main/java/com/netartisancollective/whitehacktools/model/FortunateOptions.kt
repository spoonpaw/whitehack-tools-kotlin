package com.netartisancollective.whitehacktools.model

import kotlinx.serialization.Serializable
import com.netartisancollective.whitehacktools.utilities.AdvancementTables

@Serializable
data class FortunateOptions(
    val standing: String = "",
    val hasUsedFortune: Boolean = false,
    val signatureObject: SignatureObject = SignatureObject(),
    val retainers: List<Retainer> = emptyList()
) {
    fun updateForLevel(level: Int): FortunateOptions {
        val slots = AdvancementTables.stats("Fortunate", level).slots
        val updatedRetainers = when {
            retainers.size > slots -> retainers.take(slots)
            retainers.size < slots -> retainers + List(slots - retainers.size) { Retainer() }
            else -> retainers
        }
        return copy(retainers = updatedRetainers)
    }
}

@Serializable
data class SignatureObject(
    val name: String = "",
)

@Serializable
data class Retainer(
    val name: String = "",
    val currentHP: Int = 0,
    val maxHP: Int = 0,
    val hitDice: Int = 1,
    val defense: Int = 10,
    val movement: Int = 30,
    val notes: String = "",
    val keywords: List<String> = emptyList()
)
