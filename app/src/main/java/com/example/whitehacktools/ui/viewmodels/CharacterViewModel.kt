package com.example.whitehacktools.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.whitehacktools.model.PlayerCharacter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import android.net.Uri
import android.content.Context

class CharacterViewModel : ViewModel() {
    private val _characters = MutableStateFlow<List<PlayerCharacter>>(emptyList())
    val characters: StateFlow<List<PlayerCharacter>> = _characters.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val json = Json { 
        prettyPrint = true 
        ignoreUnknownKeys = true
    }

    fun addCharacter(character: PlayerCharacter) {
        _characters.value = _characters.value + character
    }

    fun deleteCharacter(character: PlayerCharacter) {
        _characters.value = _characters.value.filter { it.id != character.id }
    }

    fun exportCharacters(characters: List<PlayerCharacter>, uri: Uri, context: Context) {
        viewModelScope.launch {
            try {
                val jsonString = json.encodeToString(characters)
                context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                    outputStream.write(jsonString.toByteArray())
                }
            } catch (e: Exception) {
                _error.value = "Failed to export characters: ${e.message}"
            }
        }
    }

    fun importCharacter(uri: Uri, context: Context, onSuccess: (PlayerCharacter) -> Unit) {
        viewModelScope.launch {
            try {
                context.contentResolver.openInputStream(uri)?.use { inputStream ->
                    val jsonString = inputStream.bufferedReader().readText()
                    val character = json.decodeFromString<PlayerCharacter>(jsonString)
                    onSuccess(character)
                }
            } catch (e: Exception) {
                _error.value = "Failed to import character: ${e.message}"
            }
        }
    }

    fun clearError() {
        _error.value = null
    }
}
