package com.example.comicslibrary.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.viewModelScope
import com.example.comicslibrary.model.CharacterResult
import com.example.comicslibrary.model.Note
import com.example.comicslibrary.model.db.CollectionDbRepo
import com.example.comicslibrary.model.db.DBCharacter
import com.example.comicslibrary.model.db.DBNote
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CollectionDbViewModel @Inject constructor(private val repo: CollectionDbRepo): ViewModel() {

    val currentCharacter = MutableStateFlow<DBCharacter?>(null)
    val collection = MutableStateFlow<List<DBCharacter>>(listOf())
    val notes = MutableStateFlow<List<DBNote>>(listOf())


    init {
        getCollection()
        getNotes()
    }

    private fun getCollection() {
        viewModelScope.launch { repo.getCharactersFromRepo().collect {
            collection.value = it
            }
        }
    }

    fun setCurrentCharacterId(characterId: Int?) {
        characterId?.let {
            viewModelScope.launch {
                repo.getCharacterFromRepo(it).collect {
                    currentCharacter.value = it
                }
            }
        }
    }

    fun addCharacter(character: CharacterResult) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.addCharacterToRepo(DBCharacter.fromCharacter(character))
        }
    }
    fun deleteCharacter(character: DBCharacter) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteAllNotes(character)
            repo.deleteCharacterFromRepo(character)
        }
    }

    private fun getNotes() {
        viewModelScope.launch {
            repo.getAllNotes().collect {
                notes.value = it
            }
        }
    }

    fun addNote(note: Note) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.addNote(DBNote.fromNote(note))
        }
    }

    fun deleteNote(note: DBNote) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteNote(note)
        }
    }

}
