package com.example.whitehacktools.model

object GearDataset {
    val standardGear = listOf(
        // Containers
        Gear(name = "Backpack", weight = "Minor", isContainer = true),
        Gear(name = "Sack", weight = "Minor", isContainer = true),
        Gear(name = "Pouch", weight = "No Size", isContainer = true),
        
        // Light Sources
        Gear(name = "Torch", weight = "Minor"),
        Gear(name = "Lantern", weight = "Minor"),
        Gear(name = "Oil Flask", weight = "Minor"),
        
        // Tools
        Gear(name = "Rope (50ft)", weight = "Regular"),
        Gear(name = "Grappling Hook", weight = "Minor"),
        Gear(name = "Crowbar", weight = "Minor"),
        Gear(name = "Hammer", weight = "Minor"),
        Gear(name = "Pitons (10)", weight = "Minor"),
        Gear(name = "Lockpicks", weight = "No Size"),
        
        // Survival
        Gear(name = "Bedroll", weight = "Regular"),
        Gear(name = "Tent", weight = "Regular"),
        Gear(name = "Flint and Steel", weight = "No Size"),
        Gear(name = "Waterskin", weight = "Minor"),
        Gear(name = "Rations (1 day)", weight = "Minor"),
        
        // Writing
        Gear(name = "Parchment (10 sheets)", weight = "No Size"),
        Gear(name = "Ink and Quill", weight = "No Size"),
        
        // Other
        Gear(name = "Holy Symbol", weight = "No Size"),
        Gear(name = "Spellbook", weight = "Minor"),
        Gear(name = "Mirror", weight = "No Size"),
        Gear(name = "Chalk", weight = "No Size"),
        Gear(name = "Compass", weight = "No Size")
    )
}
