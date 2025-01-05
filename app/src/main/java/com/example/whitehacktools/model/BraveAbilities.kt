package com.example.whitehacktools.model

import com.example.whitehacktools.utilities.AdvancementTables
import kotlinx.serialization.Serializable

@Serializable
enum class BraveQuirk {
    DOUBLE_STRAIN_ROLLS,
    IMPROVED_HEALING,
    PROTECT_ALLY,
    RESIST_CURSES,
    DRAW_ATTENTION,
    FULFILL_REQUIREMENTS,
    DIVINE_INVOCATION,
    IMPROVISED_WEAPONS;

    val displayName: String
        get() = when (this) {
            DOUBLE_STRAIN_ROLLS -> "Double Strain Rolls"
            IMPROVED_HEALING -> "Improved Healing"
            PROTECT_ALLY -> "Protect Ally"
            RESIST_CURSES -> "Resist Curses"
            DRAW_ATTENTION -> "Draw Attention"
            FULFILL_REQUIREMENTS -> "Fulfill Requirements"
            DIVINE_INVOCATION -> "Divine Invocation"
            IMPROVISED_WEAPONS -> "Improvised Weapons"
        }

    val description: String
        get() = when (this) {
            DOUBLE_STRAIN_ROLLS -> "Always make double positive strain rolls to move faster when encumbered."
            IMPROVED_HEALING -> "Require no treatment to heal beyond 1 HP from negative value. Can use comeback dice for damage shrugged off on successful save."
            PROTECT_ALLY -> "Choose a party member at session start. When protecting them, use one free comeback die for the roll."
            RESIST_CURSES -> "+4 saving throw vs. cursed objects and may use comeback dice to reduce cursed HP costs."
            DRAW_ATTENTION -> "Enemies choose to attack someone other than you first at the start of battle (if possible). Can be inverted when desired."
            FULFILL_REQUIREMENTS -> "Once per session, your courage fulfills user requirements that an item, place, or passage may have in the form of class or groups."
            DIVINE_INVOCATION -> "Once per session, use a god's name to halt, scare, convince, bless, or curse your level number of listeners (none higher level than you, +1 for holy symbol/affiliation). Each target may save to avoid."
            IMPROVISED_WEAPONS -> "Improvised weapons do at least 1d6 damage, and actual weapons ignore target resistances (but not immunities)."
        }

    companion object {
        val values = values()
    }
}

@Serializable
data class BraveQuirkSlot(
    val quirk: BraveQuirk? = null,
    val protectedAllyName: String = "" // Only used if quirk is PROTECT_ALLY
)

@Serializable
data class BraveAbilities(
    val comebackDice: Int = 0,
    val quirkSlots: List<BraveQuirkSlot> = List(10) { BraveQuirkSlot() }, // Max level is 10
    val hasSayNoPower: Boolean = false
) {
    val activeQuirks: List<BraveQuirk>
        get() = quirkSlots.mapNotNull { it.quirk }

    fun getQuirkSlot(at: Int): BraveQuirkSlot =
        if (at < quirkSlots.size) quirkSlots[at] else BraveQuirkSlot()

    fun setQuirkSlot(slot: BraveQuirkSlot, at: Int): BraveAbilities {
        if (at >= quirkSlots.size) return this
        val newSlots = quirkSlots.toMutableList()
        newSlots[at] = slot
        // Ensure list size remains the same
        while (newSlots.size < 10) {
            newSlots.add(BraveQuirkSlot())
        }
        return copy(quirkSlots = newSlots)
    }

    fun isQuirkActive(quirk: BraveQuirk): Boolean =
        quirkSlots.any { it.quirk == quirk }

    fun isSlotFilled(at: Int): Boolean =
        at < quirkSlots.size && quirkSlots[at].quirk != null

    val count: Int
        get() = quirkSlots.count { it.quirk != null }

    fun setQuirk(at: Int, quirk: BraveQuirk?, protectedAllyName: String = ""): BraveAbilities {
        return setQuirkSlot(BraveQuirkSlot(quirk = quirk, protectedAllyName = protectedAllyName), at)
    }

    fun updateForLevel(level: Int): BraveAbilities {
        val availableSlots = AdvancementTables.stats("Brave", level).slots
        val newSlots = quirkSlots.take(availableSlots).toMutableList()
        while (newSlots.size < availableSlots) {
            newSlots.add(BraveQuirkSlot())
        }
        return copy(quirkSlots = newSlots)
    }
}
