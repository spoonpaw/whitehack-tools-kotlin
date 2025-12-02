package com.netartisancollective.whitehacktools.util

import android.util.Log
import kotlinx.serialization.json.*

/**
 * Cross-Platform Import Normalizer
 *
 * Normalizes character data from ANY source (Swift, Kotlin, or mixed)
 * into the format expected by the Kotlin app.
 *
 * Philosophy: Don't assume input format. Accept what's there, map variations,
 * provide defaults for missing fields. Be defensive and flexible.
 */
object CrossPlatformConverter {
    
    private const val TAG = "CrossPlatformConverter"
    
    // MARK: - Detection (for logging purposes)
    
    /**
     * Detect if JSON data appears to be from Swift (iOS/macOS) app
     */
    fun isSwiftFormat(json: JsonElement): Boolean {
        val character = extractFirstCharacter(json) ?: return false
        
        // Swift indicators
        val hasSpeciesGroup = character.containsKey("speciesGroup")
        val hasVocationGroup = character.containsKey("vocationGroup")
        val hasUseCustomAttributes = character.containsKey("useCustomAttributes")
        val hasAttackValue = character.containsKey("_attackValue")
        
        // Kotlin indicators
        val hasSpecies = character.containsKey("species") && !character.containsKey("speciesGroup")
        val hasVocation = character.containsKey("vocation") && !character.containsKey("vocationGroup")
        val hasUseDefaultAttributes = character.containsKey("useDefaultAttributes")
        
        val isSwift = (hasSpeciesGroup || hasVocationGroup || hasUseCustomAttributes || hasAttackValue) &&
                      !hasSpecies && !hasVocation && !hasUseDefaultAttributes
        
        Log.d(TAG, "Format detection: speciesGroup=$hasSpeciesGroup, vocationGroup=$hasVocationGroup, " +
                  "useCustomAttributes=$hasUseCustomAttributes, _attackValue=$hasAttackValue -> isSwift=$isSwift")
        
        return isSwift
    }
    
    private fun extractFirstCharacter(json: JsonElement): JsonObject? {
        return when (json) {
            is JsonArray -> json.firstOrNull() as? JsonObject
            is JsonObject -> json
            else -> null
        }
    }
    
    // MARK: - Normalization (Main Entry Point)
    
    /**
     * Normalize JSON from any source to Kotlin format
     */
    fun normalize(json: JsonElement): JsonElement {
        return when (json) {
            is JsonArray -> JsonArray(json.map { normalize(it) })
            is JsonObject -> normalizeCharacter(json)
            else -> json
        }
    }
    
    /**
     * Alias for backward compatibility
     */
    fun convertFromSwift(json: JsonElement): JsonElement = normalize(json)
    
    /**
     * Normalize a single character to Kotlin format
     */
    private fun normalizeCharacter(input: JsonObject): JsonObject {
        Log.d(TAG, "Normalizing character: ${input["name"]}")
        
        return buildJsonObject {
            // MARK: - Identity Fields
            putIfNotNull("id", input.stringOrNull("id"))
            putIfNotNull("name", input.stringOrNull("name") ?: "")
            putIfNotNull("playerName", input.stringOrNull("playerName") ?: "")
            putIfNotNull("characterClass", input.stringOrNull("characterClass") ?: "Deft")
            putIfNotNull("level", input.intOrNull("level") ?: 1)
            
            // MARK: - Group Fields (accept either naming convention)
            val species = input.stringOrNull("species") ?: input.stringOrNull("speciesGroup") ?: ""
            val vocation = input.stringOrNull("vocation") ?: input.stringOrNull("vocationGroup") ?: ""
            val affiliations = input["affiliations"] ?: input["affiliationGroups"]
            
            if (species.isNotEmpty()) put("species", JsonPrimitive(species))
            if (vocation.isNotEmpty()) put("vocation", JsonPrimitive(vocation))
            affiliations?.let { put("affiliations", it) }
            
            // MARK: - Attributes
            putIfNotDefault("strength", input.intOrNull("strength"), 10)
            putIfNotDefault("agility", input.intOrNull("agility"), 10)
            putIfNotDefault("toughness", input.intOrNull("toughness"), 10)
            putIfNotDefault("intelligence", input.intOrNull("intelligence"), 10)
            putIfNotDefault("willpower", input.intOrNull("willpower"), 10)
            putIfNotDefault("charisma", input.intOrNull("charisma"), 10)
            
            // Custom attributes - accept either boolean naming, invert if needed
            val useDefault = when {
                input.containsKey("useDefaultAttributes") -> input.boolOrNull("useDefaultAttributes") ?: true
                input.containsKey("useCustomAttributes") -> !(input.boolOrNull("useCustomAttributes") ?: false)
                else -> true
            }
            if (!useDefault) put("useDefaultAttributes", JsonPrimitive(false))
            
            input["attributeGroupPairs"]?.let { put("attributeGroupPairs", normalizeAttributeGroupPairs(it)) }
            
            // MARK: - Combat Stats
            putIfNotNull("currentHP", input.intOrNull("currentHP"))
            putIfNotNull("maxHP", input.intOrNull("maxHP"))
            putIfNotDefault("movement", input.intOrNull("movement"), 30)
            
            // MARK: - Other Fields
            putIfNotEmpty("saveColor", input.stringOrNull("saveColor"))
            input["languages"]?.let { put("languages", it) }
            putIfNotDefault("coinsOnHand", input.intOrNull("coinsOnHand"), 0)
            putIfNotDefault("stashedCoins", input.intOrNull("stashedCoins"), 0)
            putIfNotDefault("experience", input.intOrNull("experience"), 0)
            putIfNotEmpty("notes", input.stringOrNull("notes"))
            putIfNotDefault("comebackDice", input.intOrNull("comebackDice"), 0)
            
            // MARK: - Equipment
            input["weapons"]?.let { put("weapons", normalizeWeapons(it)) }
            input["armor"]?.let { put("armor", normalizeArmor(it)) }
            input["gear"]?.let { put("gear", normalizeGear(it)) }
            
            // MARK: - Class Options
            input["attunementSlots"]?.let { put("attunementSlots", normalizeAttunementSlots(it)) }
            input["wiseMiracleSlots"]?.let { put("wiseMiracleSlots", normalizeWiseMiracleSlots(it)) }
            input["strongCombatOptions"]?.let { put("strongCombatOptions", normalizeStrongCombatOptions(it)) }
            input["currentConflictLoot"]?.let { 
                if (it !is JsonNull) put("currentConflictLoot", normalizeConflictLoot(it)) 
            }
            input["braveQuirkOptions"]?.let { put("braveQuirkOptions", normalizeBraveQuirkOptions(it)) }
            input["cleverKnackOptions"]?.let { put("cleverKnackOptions", normalizeCleverKnackOptions(it)) }
            input["fortunateOptions"]?.let { put("fortunateOptions", normalizeFortunateOptions(it)) }
        }
    }
    
    // MARK: - Helper Extensions
    
    private fun JsonObject.stringOrNull(key: String): String? =
        (this[key] as? JsonPrimitive)?.contentOrNull
    
    private fun JsonObject.intOrNull(key: String): Int? =
        (this[key] as? JsonPrimitive)?.intOrNull
    
    private fun JsonObject.boolOrNull(key: String): Boolean? =
        (this[key] as? JsonPrimitive)?.booleanOrNull
    
    private fun JsonObjectBuilder.putIfNotNull(key: String, value: String?) {
        value?.let { put(key, JsonPrimitive(it)) }
    }
    
    private fun JsonObjectBuilder.putIfNotNull(key: String, value: Int?) {
        value?.let { put(key, JsonPrimitive(it)) }
    }
    
    private fun JsonObjectBuilder.putIfNotEmpty(key: String, value: String?) {
        if (!value.isNullOrEmpty()) put(key, JsonPrimitive(value))
    }
    
    private fun JsonObjectBuilder.putIfNotDefault(key: String, value: Int?, default: Int) {
        if (value != null && value != default) put(key, JsonPrimitive(value))
    }
    
    // MARK: - Attribute Group Pairs
    
    private fun normalizeAttributeGroupPairs(json: JsonElement): JsonElement {
        if (json !is JsonArray) return json
        return JsonArray(json.map { pair ->
            if (pair !is JsonObject) return@map pair
            buildJsonObject {
                pair["id"]?.let { put("id", it) }
                pair["attribute"]?.let { put("attribute", it) }
                pair["group"]?.let { put("group", it) }
            }
        })
    }
    
    // MARK: - Equipment Normalization
    
    private fun normalizeWeapons(json: JsonElement): JsonElement {
        if (json !is JsonArray) return json
        return JsonArray(json.map { weapon ->
            if (weapon !is JsonObject) return@map weapon
            buildJsonObject {
                // Copy all fields, skip Swift-only ones
                weapon.forEach { (key, value) ->
                    when (key) {
                        "isStashed", "isCursed" -> {} // Skip Swift-only
                        else -> put(key, value)
                    }
                }
            }
        })
    }
    
    private fun normalizeArmor(json: JsonElement): JsonElement {
        if (json !is JsonArray) return json
        return JsonArray(json.map { armor ->
            if (armor !is JsonObject) return@map armor
            buildJsonObject {
                armor.forEach { (key, value) ->
                    when (key) {
                        "isStashed", "isCursed" -> {} // Skip Swift-only
                        else -> put(key, value)
                    }
                }
            }
        })
    }
    
    private fun normalizeGear(json: JsonElement): JsonElement {
        if (json !is JsonArray) return json
        return JsonArray(json.map { gear ->
            if (gear !is JsonObject) return@map gear
            buildJsonObject {
                gear.forEach { (key, value) ->
                    when (key) {
                        "isStashed", "isCursed" -> {} // Skip Swift-only
                        else -> put(key, value)
                    }
                }
            }
        })
    }
    
    // MARK: - Attunement Slots
    
    private fun normalizeAttunementSlots(json: JsonElement): JsonElement {
        if (json !is JsonArray) return json
        return JsonArray(json.map { slot ->
            if (slot !is JsonObject) return@map slot
            buildJsonObject {
                slot.forEach { (key, value) ->
                    when (key) {
                        "primaryAttunement", "secondaryAttunement",
                        "tertiaryAttunement", "quaternaryAttunement" -> {
                            put(key, normalizeAttunement(value))
                        }
                        // Skip Swift-only fields
                        "id", "hasUsedDailyPower" -> {}
                        else -> put(key, value)
                    }
                }
            }
        })
    }
    
    private fun normalizeAttunement(json: JsonElement): JsonElement {
        if (json !is JsonObject) return json
        return buildJsonObject {
            json.forEach { (key, value) ->
                when (key) {
                    "type" -> {
                        // Accept any casing, normalize to UPPERCASE for Kotlin
                        val typeStr = (value as? JsonPrimitive)?.contentOrNull ?: "ITEM"
                        put(key, JsonPrimitive(typeStr.uppercase()))
                    }
                    // Skip Swift-only fields
                    "id", "isLost" -> {}
                    else -> put(key, value)
                }
            }
        }
    }
    
    // MARK: - Wise Miracle Slots
    
    private fun normalizeWiseMiracleSlots(json: JsonElement): JsonElement {
        if (json !is JsonArray) return json
        return JsonArray(json.map { slot ->
            if (slot !is JsonObject) return@map slot
            buildJsonObject {
                slot.forEach { (key, value) ->
                    when (key) {
                        // Accept either field name
                        "isMagicItem" -> put("isMagicItemSlot", value)
                        "isMagicItemSlot" -> put("isMagicItemSlot", value)
                        "baseMiracles", "additionalMiracles" -> put(key, normalizeMiracles(value))
                        else -> put(key, value)
                    }
                }
            }
        })
    }
    
    private fun normalizeMiracles(json: JsonElement): JsonElement {
        if (json !is JsonArray) return json
        return JsonArray(json.map { miracle ->
            if (miracle !is JsonObject) return@map miracle
            buildJsonObject {
                miracle.forEach { (key, value) ->
                    // Skip Swift-only fields, pass through the rest
                    when (key) {
                        "isAdditional" -> {} // Kotlin doesn't use this
                        else -> put(key, value)
                    }
                }
            }
        })
    }
    
    // MARK: - Strong Combat Options
    
    private fun normalizeStrongCombatOptions(json: JsonElement): JsonElement {
        if (json !is JsonObject) return json
        return buildJsonObject {
            json["slots"]?.let { slots ->
                if (slots is JsonArray) {
                    // Convert numbers to strings (Kotlin uses string enum values)
                    put("slots", JsonArray(slots.map { slot ->
                        when {
                            slot is JsonNull -> JsonNull
                            slot is JsonPrimitive && slot.isString -> slot
                            slot is JsonPrimitive -> JsonPrimitive(slot.content)
                            else -> slot
                        }
                    }))
                }
            }
        }
    }
    
    // MARK: - Conflict Loot
    
    private fun normalizeConflictLoot(json: JsonElement): JsonElement {
        if (json is JsonNull || json !is JsonObject) return json
        return buildJsonObject {
            json.forEach { (key, value) ->
                when (key) {
                    "type" -> {
                        // Accept any casing, normalize to TitleCase for Kotlin
                        val typeStr = (value as? JsonPrimitive)?.contentOrNull ?: "Object"
                        val titleCase = typeStr.lowercase().replaceFirstChar { it.uppercaseChar() }
                        put(key, JsonPrimitive(titleCase))
                    }
                    else -> put(key, value)
                }
            }
        }
    }
    
    // MARK: - Brave Quirk Options
    
    private fun normalizeBraveQuirkOptions(json: JsonElement): JsonElement {
        if (json !is JsonObject) return json
        return buildJsonObject {
            json["slots"]?.let { slots ->
                if (slots is JsonArray) {
                    put("slots", JsonArray(slots.map { slot ->
                        if (slot !is JsonObject) return@map slot
                        buildJsonObject {
                            slot.forEach { (key, value) ->
                                // Skip Swift-only fields
                                when (key) {
                                    "protectedAllyName" -> {} // Kotlin might not use this
                                    else -> put(key, value)
                                }
                            }
                        }
                    }))
                }
            }
        }
    }
    
    // MARK: - Clever Knack Options
    
    private fun normalizeCleverKnackOptions(json: JsonElement): JsonElement {
        if (json !is JsonObject) return json
        return buildJsonObject {
            json.forEach { (key, value) ->
                when (key) {
                    "slots" -> {
                        if (value is JsonArray) {
                            put(key, JsonArray(value.map { slot ->
                                if (slot !is JsonObject) return@map slot
                                buildJsonObject {
                                    slot.forEach { (slotKey, slotValue) ->
                                        // Skip Swift-only fields
                                        when (slotKey) {
                                            "hasUsedCombatDie" -> {} // Kotlin might not have per-slot
                                            else -> put(slotKey, slotValue)
                                        }
                                    }
                                }
                            }))
                        }
                    }
                    else -> put(key, value)
                }
            }
        }
    }
    
    // MARK: - Fortunate Options
    
    private fun normalizeFortunateOptions(json: JsonElement): JsonElement {
        if (json !is JsonObject) return json
        return buildJsonObject {
            json.forEach { (key, value) ->
                when (key) {
                    "retainers" -> put(key, normalizeRetainers(value))
                    // Skip Swift-only fields
                    "newKeyword" -> {}
                    else -> put(key, value)
                }
            }
        }
    }
    
    private fun normalizeRetainers(json: JsonElement): JsonElement {
        if (json !is JsonArray) return json
        return JsonArray(json.map { retainer ->
            if (retainer !is JsonObject) return@map retainer
            buildJsonObject {
                retainer.forEach { (key, value) ->
                    when (key) {
                        // Accept either field name, output Kotlin's
                        "defenseFactor" -> put("defense", value)
                        "defense" -> put("defense", value)
                        // Skip Swift-only fields
                        "id", "type", "attitude" -> {}
                        else -> put(key, value)
                    }
                }
            }
        })
    }
}
