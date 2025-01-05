package com.example.whitehacktools.model

import kotlinx.serialization.Serializable

@Serializable
enum class CleverKnack {
    COMBAT_EXPLOITER,
    EFFICIENT_CRAFTER,
    WEAKENED_SAVES,
    NAVIGATION_MASTER,
    CONVINCING_NEGOTIATOR,
    ESCAPE_ARTIST,
    SUBSTANCE_EXPERT,
    MACHINE_MASTER,
    TRACKING_EXPERT;

    val displayName: String
        get() = when (this) {
            COMBAT_EXPLOITER -> "Combat Exploiter"
            EFFICIENT_CRAFTER -> "Efficient Crafter"
            WEAKENED_SAVES -> "Weakened Saves"
            NAVIGATION_MASTER -> "Navigation Master"
            CONVINCING_NEGOTIATOR -> "Convincing Negotiator"
            ESCAPE_ARTIST -> "Escape Artist"
            SUBSTANCE_EXPERT -> "Substance Expert"
            MACHINE_MASTER -> "Machine Master"
            TRACKING_EXPERT -> "Tracking Expert"
        }

    val description: String
        get() = when (this) {
            COMBAT_EXPLOITER -> "Base bonus for combat advantage is +3 instead of +2, and once per battle may switch d6 for d10 as damage die"
            EFFICIENT_CRAFTER -> "+4 to crafting, mending, or assembly. Takes half the time and can skip one non-essential part"
            WEAKENED_SAVES -> "Targets of special attacks get -3 to their saves"
            NAVIGATION_MASTER -> "Can always figure out location roughly. Never gets lost"
            CONVINCING_NEGOTIATOR -> "+2 to task rolls and saves in conviction attempts, including trade"
            ESCAPE_ARTIST -> "+4 to any task roll related to escaping confinement or bypassing barriers"
            SUBSTANCE_EXPERT -> "+4 to substance identification and saves, +1 to quantified effects in character's favor"
            MACHINE_MASTER -> "+4 to task rolls with or concerning machines"
            TRACKING_EXPERT -> "+4 to tracking and covering own tracks"
        }
}
