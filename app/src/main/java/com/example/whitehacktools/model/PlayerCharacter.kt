package com.example.whitehacktools.model

data class PlayerCharacter(
    val id: String,
    val name: String,
    val playerName: String = "",
    val characterClass: String,
    val level: Int,
    val experience: Int = 0,
    val currentHP: Int = 10,
    val maxHP: Int = 10
)
