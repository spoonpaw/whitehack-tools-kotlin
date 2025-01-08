package com.example.whitehacktools.ui.viewmodels

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.whitehacktools.model.PlayerCharacter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class CharacterImportViewModel : ViewModel() {
    private val _importError = MutableStateFlow<String?>(null)
    val importError: StateFlow<String?> = _importError.asStateFlow()

    private val json = Json { 
        prettyPrint = true 
        ignoreUnknownKeys = true
        isLenient = true
    }

    fun importCharacter(uri: Uri): PlayerCharacter? {
        return try {
            android.util.Log.d("Import", "Starting character import")
            val jsonString = uri.toString()
            android.util.Log.d("Import", "JSON: $jsonString")
            
            val character = json.decodeFromString<PlayerCharacter>(jsonString)
            android.util.Log.d("Import", "Character imported successfully")
            android.util.Log.d("Import", "Wise miracles: ${character.wiseMiracleSlots.size} slots")
            character.wiseMiracleSlots.forEachIndexed { index, slot ->
                android.util.Log.d("Import", "Slot $index:")
                android.util.Log.d("Import", "  Base miracles: ${slot.baseMiracles.map { it.name }}")
                android.util.Log.d("Import", "  Additional miracles: ${slot.additionalMiracles.map { it.name }}")
            }
            character
        } catch (e: Exception) {
            android.util.Log.e("Import", "Failed to import character", e)
            _importError.value = "Failed to import character: ${e.message}"
            null
        }
    }

    fun exportCharacter(character: PlayerCharacter, file: File) {
        viewModelScope.launch {
            try {
                val jsonString = json.encodeToString(character)
                FileOutputStream(file).use { outputStream ->
                    outputStream.write(jsonString.toByteArray())
                }
            } catch (e: IOException) {
                _importError.value = "Failed to export character: ${e.message}"
            }
        }
    }

    fun clearError() {
        _importError.value = null
    }
}
