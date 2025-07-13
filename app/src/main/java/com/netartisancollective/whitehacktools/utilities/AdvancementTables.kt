package com.netartisancollective.whitehacktools.utilities

data class CharacterStats(
    val hitDice: String,
    val attackValue: Int,
    val savingValue: Int,
    val slots: Int,
    val groups: Int,
    val raises: String  // Changed to String to handle "-" for level 1
)

object AdvancementTables {
    private val statProgressions = mapOf(
        "Deft" to mapOf(
            1 to CharacterStats(hitDice = "1", attackValue = 10, savingValue = 7, slots = 1, groups = 2, raises = "-"),
            2 to CharacterStats(hitDice = "2", attackValue = 11, savingValue = 8, slots = 1, groups = 2, raises = "1"),
            3 to CharacterStats(hitDice = "2+1", attackValue = 11, savingValue = 9, slots = 1, groups = 3, raises = "1"),
            4 to CharacterStats(hitDice = "3", attackValue = 12, savingValue = 10, slots = 2, groups = 3, raises = "2"),
            5 to CharacterStats(hitDice = "3+1", attackValue = 12, savingValue = 11, slots = 2, groups = 4, raises = "2"),
            6 to CharacterStats(hitDice = "4", attackValue = 13, savingValue = 12, slots = 2, groups = 4, raises = "3"),
            7 to CharacterStats(hitDice = "4+1", attackValue = 13, savingValue = 13, slots = 3, groups = 5, raises = "3"),
            8 to CharacterStats(hitDice = "5", attackValue = 14, savingValue = 14, slots = 3, groups = 5, raises = "4"),
            9 to CharacterStats(hitDice = "5+1", attackValue = 14, savingValue = 15, slots = 3, groups = 6, raises = "4"),
            10 to CharacterStats(hitDice = "6", attackValue = 15, savingValue = 16, slots = 4, groups = 6, raises = "5")
        ),
        "Strong" to mapOf(
            1 to CharacterStats(hitDice = "1+2", attackValue = 11, savingValue = 5, slots = 1, groups = 2, raises = "-"),
            2 to CharacterStats(hitDice = "2", attackValue = 11, savingValue = 6, slots = 1, groups = 2, raises = "1"),
            3 to CharacterStats(hitDice = "3", attackValue = 12, savingValue = 7, slots = 1, groups = 2, raises = "1"),
            4 to CharacterStats(hitDice = "4", attackValue = 13, savingValue = 8, slots = 2, groups = 3, raises = "2"),
            5 to CharacterStats(hitDice = "5", attackValue = 13, savingValue = 9, slots = 2, groups = 3, raises = "2"),
            6 to CharacterStats(hitDice = "6", attackValue = 14, savingValue = 10, slots = 2, groups = 3, raises = "3"),
            7 to CharacterStats(hitDice = "7", attackValue = 15, savingValue = 11, slots = 3, groups = 4, raises = "3"),
            8 to CharacterStats(hitDice = "8", attackValue = 15, savingValue = 12, slots = 3, groups = 4, raises = "4"),
            9 to CharacterStats(hitDice = "9", attackValue = 16, savingValue = 13, slots = 3, groups = 4, raises = "4"),
            10 to CharacterStats(hitDice = "10", attackValue = 17, savingValue = 14, slots = 4, groups = 5, raises = "5")
        ),
        "Wise" to mapOf(
            1 to CharacterStats(hitDice = "1+1", attackValue = 10, savingValue = 6, slots = 1, groups = 2, raises = "-"),
            2 to CharacterStats(hitDice = "2", attackValue = 11, savingValue = 7, slots = 1, groups = 2, raises = "1"),
            3 to CharacterStats(hitDice = "2+1", attackValue = 11, savingValue = 8, slots = 2, groups = 2, raises = "1"),
            4 to CharacterStats(hitDice = "3", attackValue = 11, savingValue = 9, slots = 2, groups = 3, raises = "2"),
            5 to CharacterStats(hitDice = "4", attackValue = 12, savingValue = 10, slots = 3, groups = 3, raises = "2"),
            6 to CharacterStats(hitDice = "4+1", attackValue = 12, savingValue = 11, slots = 3, groups = 3, raises = "3"),
            7 to CharacterStats(hitDice = "5", attackValue = 12, savingValue = 12, slots = 4, groups = 4, raises = "3"),
            8 to CharacterStats(hitDice = "6", attackValue = 13, savingValue = 13, slots = 4, groups = 4, raises = "4"),
            9 to CharacterStats(hitDice = "6+1", attackValue = 13, savingValue = 14, slots = 5, groups = 4, raises = "4"),
            10 to CharacterStats(hitDice = "7", attackValue = 13, savingValue = 15, slots = 5, groups = 5, raises = "5")
        ),
        "Brave" to mapOf(
            1 to CharacterStats(hitDice = "1*", attackValue = 10, savingValue = 9, slots = 1, groups = 2, raises = "-"),
            2 to CharacterStats(hitDice = "2*", attackValue = 10, savingValue = 10, slots = 1, groups = 2, raises = "1"),
            3 to CharacterStats(hitDice = "3*", attackValue = 10, savingValue = 11, slots = 1, groups = 2, raises = "1"),
            4 to CharacterStats(hitDice = "4", attackValue = 11, savingValue = 12, slots = 2, groups = 2, raises = "2"),
            5 to CharacterStats(hitDice = "5", attackValue = 11, savingValue = 13, slots = 2, groups = 3, raises = "2"),
            6 to CharacterStats(hitDice = "6", attackValue = 11, savingValue = 14, slots = 2, groups = 3, raises = "3"),
            7 to CharacterStats(hitDice = "7", attackValue = 12, savingValue = 15, slots = 3, groups = 3, raises = "3"),
            8 to CharacterStats(hitDice = "8", attackValue = 12, savingValue = 16, slots = 3, groups = 3, raises = "4"),
            9 to CharacterStats(hitDice = "9", attackValue = 12, savingValue = 17, slots = 3, groups = 4, raises = "4"),
            10 to CharacterStats(hitDice = "10", attackValue = 13, savingValue = 18, slots = 4, groups = 4, raises = "5")
        ),
        "Clever" to mapOf(
            1 to CharacterStats(hitDice = "1", attackValue = 10, savingValue = 8, slots = 1, groups = 2, raises = "-"),
            2 to CharacterStats(hitDice = "2", attackValue = 11, savingValue = 9, slots = 1, groups = 2, raises = "1"),
            3 to CharacterStats(hitDice = "2+1", attackValue = 11, savingValue = 10, slots = 1, groups = 2, raises = "1"),
            4 to CharacterStats(hitDice = "3", attackValue = 11, savingValue = 11, slots = 2, groups = 3, raises = "2"),
            5 to CharacterStats(hitDice = "3+1", attackValue = 12, savingValue = 12, slots = 2, groups = 3, raises = "2"),
            6 to CharacterStats(hitDice = "4", attackValue = 12, savingValue = 13, slots = 2, groups = 3, raises = "3"),
            7 to CharacterStats(hitDice = "4+1", attackValue = 13, savingValue = 14, slots = 3, groups = 4, raises = "3"),
            8 to CharacterStats(hitDice = "5", attackValue = 13, savingValue = 15, slots = 3, groups = 4, raises = "4"),
            9 to CharacterStats(hitDice = "5+1", attackValue = 13, savingValue = 16, slots = 3, groups = 4, raises = "4"),
            10 to CharacterStats(hitDice = "6", attackValue = 14, savingValue = 17, slots = 4, groups = 5, raises = "5")
        ),
        "Fortunate" to mapOf(
            1 to CharacterStats(hitDice = "1", attackValue = 10, savingValue = 6, slots = 1, groups = 2, raises = "-"),
            2 to CharacterStats(hitDice = "2", attackValue = 10, savingValue = 7, slots = 1, groups = 2, raises = "1"),
            3 to CharacterStats(hitDice = "2+1", attackValue = 11, savingValue = 8, slots = 1, groups = 3, raises = "1"),
            4 to CharacterStats(hitDice = "3", attackValue = 11, savingValue = 9, slots = 2, groups = 3, raises = "2"),
            5 to CharacterStats(hitDice = "3+1", attackValue = 12, savingValue = 10, slots = 2, groups = 4, raises = "2"),
            6 to CharacterStats(hitDice = "4", attackValue = 12, savingValue = 11, slots = 2, groups = 4, raises = "3"),
            7 to CharacterStats(hitDice = "4+1", attackValue = 13, savingValue = 12, slots = 3, groups = 5, raises = "3"),
            8 to CharacterStats(hitDice = "5", attackValue = 13, savingValue = 13, slots = 3, groups = 5, raises = "4"),
            9 to CharacterStats(hitDice = "5+1", attackValue = 14, savingValue = 14, slots = 3, groups = 6, raises = "4"),
            10 to CharacterStats(hitDice = "6", attackValue = 14, savingValue = 15, slots = 4, groups = 6, raises = "5")
        )
    )

    private val xpRequirements = mapOf(
        "Deft" to mapOf(
            2 to 1450, 3 to 2900, 4 to 5800, 5 to 11600,
            6 to 23200, 7 to 46400, 8 to 92800, 9 to 185600, 10 to 371200
        ),
        "Strong" to mapOf(
            2 to 1900, 3 to 3800, 4 to 7600, 5 to 15200,
            6 to 30400, 7 to 60800, 8 to 121600, 9 to 243200, 10 to 486400
        ),
        "Wise" to mapOf(
            2 to 2350, 3 to 4700, 4 to 9400, 5 to 18800,
            6 to 37600, 7 to 75200, 8 to 150400, 9 to 300800, 10 to 601600
        ),
        "Brave" to mapOf(
            2 to 1225, 3 to 2450, 4 to 4900, 5 to 9800,
            6 to 19600, 7 to 39200, 8 to 78400, 9 to 156800, 10 to 313600
        ),
        "Clever" to mapOf(
            2 to 1350, 3 to 2700, 4 to 5400, 5 to 10800,
            6 to 21600, 7 to 43200, 8 to 86400, 9 to 172800, 10 to 345600
        ),
        "Fortunate" to mapOf(
            2 to 1450, 3 to 2900, 4 to 5800, 5 to 11600,
            6 to 23200, 7 to 46400, 8 to 92800, 9 to 185600, 10 to 371200
        )
    )

    fun stats(characterClass: String, level: Int): CharacterStats {
        // Ensure level is between 1 and 10
        val validLevel = level.coerceIn(1, 10)
        
        // Return stats for the valid level, defaulting to level 1 if something goes wrong
        return statProgressions[characterClass]?.get(validLevel) 
            ?: statProgressions[characterClass]?.get(1) 
            ?: CharacterStats(hitDice = "1", attackValue = 10, savingValue = 7, slots = 1, groups = 2, raises = "-")
    }

    fun xpRequirement(characterClass: String, targetLevel: Int): Int {
        val clampedLevel = targetLevel.coerceIn(2, 10)
        return xpRequirements[characterClass]?.get(clampedLevel) ?: 0
    }

    fun level(characterClass: String, xp: Int): Int {
        if (xp <= 0) return 1

        val requirements = xpRequirements[characterClass] ?: return 1

        // Find the highest level where the XP requirement is less than or equal to the character's XP
        return (2..10).reversed().firstOrNull { level ->
            requirements[level]?.let { requirement -> xp >= requirement } ?: false
        } ?: 1
    }
}
