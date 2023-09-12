package com.example.comicslibrary.model.db

import kotlinx.coroutines.flow.Flow


interface CollectionDbRepo {
    suspend fun getCharactersFromRepo(): Flow<List<DBCharacter>>

    suspend fun getCharacterFromRepo(characterId: Int): Flow<DBCharacter>

    suspend fun addCharacterToRepo(character: DBCharacter)

    suspend fun updateCharacterInRepo(character: DBCharacter)

    suspend fun deleteCharacterFromRepo(character: DBCharacter)

    suspend fun getAllNotes(): Flow<List<DBNote>>

    suspend fun getNotesFromRepo(characterId: Int): Flow<List<DBNote>>

    suspend fun addNote(note: DBNote)

    suspend fun updateNote(note: DBNote)

    suspend fun deleteNote(note: DBNote)

    suspend fun deleteAllNotes(character: DBCharacter)

}