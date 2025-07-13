package com.netartisancollective.whitehacktools.model

object WeaponDataset {
    val standardWeapons = listOf(
        Weapon(name = "Axe", damage = "1d6+1", weight = "Regular", range = "N/A", rateOfFire = "N/A", special = ""),
        Weapon(name = "Sword", damage = "1d6+1", weight = "Regular", range = "N/A", rateOfFire = "N/A", special = ""),
        Weapon(name = "Club", damage = "1d6-2", weight = "Minor", range = "N/A", rateOfFire = "N/A", special = "Knock-out, improvised"),
        Weapon(name = "Crossbow", damage = "1d6+1", weight = "Heavy", range = "70", rateOfFire = "1/2", special = "Two handed"),
        Weapon(name = "Dagger", damage = "1d6-2", weight = "Minor", range = "15", rateOfFire = "1", special = ""),
        Weapon(name = "Darts", damage = "1", weight = "Minor", range = "20", rateOfFire = "3", special = "1–3 equal a minor item"),
        Weapon(name = "Flail", damage = "1d6", weight = "Regular", range = "N/A", rateOfFire = "N/A", special = "Ignore shield DF"),
        Weapon(name = "Great Sword", damage = "1d6+2", weight = "Heavy", range = "N/A", rateOfFire = "N/A", special = "Two handed"),
        Weapon(name = "Great Axe", damage = "1d6+2", weight = "Heavy", range = "N/A", rateOfFire = "N/A", special = "Two handed"),
        Weapon(name = "Javelin", damage = "1d6", weight = "Minor", range = "40", rateOfFire = "1", special = "d6–2 damage in melee"),
        Weapon(name = "Longbow", damage = "1d6", weight = "Regular", range = "70", rateOfFire = "1", special = "Two handed"),
        Weapon(name = "Mace", damage = "1d6", weight = "Regular", range = "N/A", rateOfFire = "N/A", special = "+1 AV vs. metal armor"),
        Weapon(name = "Hammer", damage = "1d6", weight = "Regular", range = "N/A", rateOfFire = "N/A", special = "+1 AV vs. metal armor"),
        Weapon(name = "Morning Star", damage = "1d6", weight = "Heavy", range = "N/A", rateOfFire = "N/A", special = "As above, x3 crit dam."),
        Weapon(name = "Musket", damage = "1d6+2", weight = "Heavy", range = "30", rateOfFire = "1/4", special = "Two handed"),
        Weapon(name = "Pistol", damage = "1d6+1", weight = "Regular", range = "20", rateOfFire = "1/3", special = ""),
        Weapon(name = "Pole Arms", damage = "1d6", weight = "Heavy", range = "N/A", rateOfFire = "N/A", special = "Two handed, reach"),
        Weapon(name = "Quarterstaff", damage = "1d6-1", weight = "Regular", range = "N/A", rateOfFire = "N/A", special = "Two handed, reach"),
        Weapon(name = "Scimitar", damage = "1d6-1", weight = "Regular", range = "N/A", rateOfFire = "N/A", special = "+1 AV while riding"),
        Weapon(name = "Shortbow", damage = "1d6-1", weight = "Regular", range = "50", rateOfFire = "1", special = "Two handed, w. mount"),
        Weapon(name = "Shortsword", damage = "1d6-1", weight = "Minor", range = "N/A", rateOfFire = "N/A", special = ""),
        Weapon(name = "Sling", damage = "1d6-2", weight = "No size", range = "30", rateOfFire = "1", special = "Use with stones"),
        Weapon(name = "Spear", damage = "1d6", weight = "Regular", range = "N/A", rateOfFire = "N/A", special = "Reach"),
        Weapon(name = "Throwing Knife", damage = "1d6-2", weight = "Minor", range = "25", rateOfFire = "1", special = "−1 AV in melee"),
        Weapon(name = "Throwing Axe", damage = "1d6-2", weight = "Minor", range = "25", rateOfFire = "1", special = "−1 AV in melee"),
        Weapon(name = "Unarmed", damage = "d6-3", weight = "", range = "N/A", rateOfFire = "N/A", special = "Knock-out, grapple")
    )
}
