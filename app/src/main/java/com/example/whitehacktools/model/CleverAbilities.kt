package com.example.whitehacktools.model

import com.example.whitehacktools.utilities.AdvancementTables
import kotlinx.serialization.Serializable

@Serializable
data class CleverKnackSlot(
    val knack: CleverKnack? = null,
    val hasUsedCombatDie: Boolean = false // Only used if knack is COMBAT_EXPLOITER
)

@Serializable
data class CleverAbilities(
    val knackSlots: List<CleverKnackSlot> = List(10) { CleverKnackSlot() }, // Max level is 10
    val hasUsedUnorthodoxBonus: Boolean = false
) {
    fun updateForLevel(level: Int): CleverAbilities {
        val availableSlots = AdvancementTables.stats("Clever", level).slots
        
        // If we have too many slots, truncate
        if (knackSlots.size > availableSlots) {
            return copy(knackSlots = knackSlots.take(availableSlots))
        }
        
        // If we need more slots, add empty ones
        if (knackSlots.size < availableSlots) {
            val newSlots = knackSlots.toMutableList()
            repeat(availableSlots - knackSlots.size) {
                newSlots.add(CleverKnackSlot())
            }
            return copy(knackSlots = newSlots)
        }
        
        return this
    }

    fun setKnack(knack: CleverKnack?, index: Int): CleverAbilities {
        if (index < 0) return this
        
        val slots = knackSlots.toMutableList()
        // Add empty slots if needed
        while (slots.size <= index) {
            slots.add(CleverKnackSlot())
        }
        slots[index] = CleverKnackSlot(knack = knack)
        return copy(knackSlots = slots)
    }

    fun setHasUsedCombatDie(value: Boolean, index: Int): CleverAbilities {
        if (index < 0 || index >= knackSlots.size) return this
        
        val slots = knackSlots.toMutableList()
        slots[index] = slots[index].copy(hasUsedCombatDie = value)
        return copy(knackSlots = slots)
    }

    fun resetDailyPowers(): CleverAbilities {
        val resetSlots = knackSlots.map { it.copy(hasUsedCombatDie = false) }
        return copy(knackSlots = resetSlots, hasUsedUnorthodoxBonus = false)
    }
}
