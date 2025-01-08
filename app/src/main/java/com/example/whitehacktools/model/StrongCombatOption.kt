package com.example.whitehacktools.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
enum class StrongCombatOption {
    @SerialName("0")
    PROTECT_ALLY,
    @SerialName("1")
    FORCE_MOVEMENT,
    @SerialName("2")
    CLIMB_OPPONENTS,
    @SerialName("3")
    SPECIAL_ATTACKS,
    @SerialName("4")
    GRANT_ADVANTAGE,
    @SerialName("5")
    ENCOURAGE_FRIGHTEN,
    @SerialName("6")
    DUAL_ATTACK,
    @SerialName("7")
    PARRY_WAIT;

    override fun toString(): String = when(this) {
        PROTECT_ALLY -> "Protect Adjacent Ally"
        FORCE_MOVEMENT -> "Force Movement"
        CLIMB_OPPONENTS -> "Climb Big Opponents"
        SPECIAL_ATTACKS -> "Special Attack Effects"
        GRANT_ADVANTAGE -> "Grant Double Advantage"
        ENCOURAGE_FRIGHTEN -> "Encourage/Frighten"
        DUAL_ATTACK -> "Melee + Ranged Attack"
        PARRY_WAIT -> "Parry and Wait"
    }

    val description: String
        get() = when(this) {
            PROTECT_ALLY -> "Full round action: Protect an adjacent character by redirecting all attacks targeting them to yourself until your next turn. Each enemy gets a save against this effect."
            FORCE_MOVEMENT -> "After a successful attack, you may forgo damage to force the opponent to move up to 10 feet from their current position. In melee, you may take their previous position as a free move. Target gets a save to resist. Note: This movement may trigger free attacks from other characters."
            CLIMB_OPPONENTS -> "When fighting opponents larger than your species (e.g., a human vs a halfling), spend one action to climb them with an agility roll. If successful, gain double combat advantage (+4 to attack and damage) next round and subsequent rounds while holding on. Additional agility rolls may be required but don't cost actions."
            SPECIAL_ATTACKS -> "On a successful attack, you may forgo normal damage to instead cause one of these effects: reduce enemy initiative by 2, reduce their movement by 10, or deal 2 points of ongoing damage per round. Must describe how the effect is achieved. Target may save to end movement/damage effects after 1+ rounds."
            GRANT_ADVANTAGE -> "Once per battle, grant an ally double combat advantage on a single attack (can be used immediately or saved for later in the fight). Requires a small verbal action like a tactical command or suggestion."
            ENCOURAGE_FRIGHTEN -> "With a small verbal action, either encourage allies or frighten enemies in a 15-foot radius. Encouragement: allies gain +1 to attack and saving throws. Frighten: enemies suffer -1 to attack and saving throws."
            DUAL_ATTACK -> "By giving up your movement for the round, make both a melee and a ranged attack. Requires appropriate one-handed weapons (e.g., sword and throwing knife). Both attacks must use suitable one-handed weapons."
            PARRY_WAIT -> "Instead of attacking, parry to gain +2 defense this round and double combat advantage against the parried enemy next round. Parrying two rounds in a row grants triple advantage. If you take damage while parrying, you must save or lose the effect."
        }

    val displayName: String
        get() = name.split('_').joinToString(" ") { it.lowercase().replaceFirstChar { char -> char.uppercase() } }

    companion object {
        fun fromString(str: String): StrongCombatOption? = 
            values().find { it.toString() == str }
    }
}

@Serializable
data class StrongCombatOptions(
    @SerialName("slots")
    private val _slots: List<StrongCombatOption?> = List(10) { null }
) {
    // For Swift compatibility
    val slots: List<StrongCombatOption?>
        get() = _slots
        
    // For Kotlin UI backward compatibility
    val options: List<StrongCombatOption?>
        get() = _slots

    val activeOptions: List<StrongCombatOption> 
        get() = _slots.filterNotNull()

    fun getOption(at: Int): StrongCombatOption? = 
        if (at < _slots.size) _slots[at] else null

    fun setOption(option: StrongCombatOption?, at: Int): StrongCombatOptions {
        // Create a new list with enough capacity
        val newSlots = _slots.toMutableList()
        while (newSlots.size <= at) {
            newSlots.add(null)
        }
        newSlots[at] = option
        return copy(_slots = newSlots)
    }

    fun isActive(option: StrongCombatOption): Boolean =
        _slots.contains(option)

    fun isSlotFilled(at: Int): Boolean =
        at < _slots.size && _slots[at] != null

    val count: Int
        get() = _slots.count { it != null }
}

@Serializable
enum class ConflictLootType {
    Special,
    Substance,
    Supernatural;

    override fun toString(): String = name.split('_')
        .joinToString(" ") { it.lowercase().replaceFirstChar { char -> char.uppercase() } }
}
