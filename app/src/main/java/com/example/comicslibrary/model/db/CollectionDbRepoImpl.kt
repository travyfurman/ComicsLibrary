package com.example.comicslibrary.model.db

import kotlinx.coroutines.flow.Flow

class CollectionDbRepoImpl(private val characterDao: CharacterDao, private val noteDao: NoteDao): CollectionDbRepo {
    override suspend fun getCharactersFromRepo(): Flow<List<DBCharacter>> =
        characterDao.getCharacters()

    override suspend fun getCharacterFromRepo(characterId: Int): Flow<DBCharacter> =
        characterDao.getCharacter(characterId)

    override suspend fun addCharacterToRepo(character: DBCharacter) =
        characterDao.addCharacter(character)

    override suspend fun updateCharacterInRepo(character: DBCharacter) =
        characterDao.updateCharacter(character)

    override suspend fun deleteCharacterFromRepo(character: DBCharacter) =
        characterDao.deleteCharacter(character)


    override suspend fun getAllNotes(): Flow<List<DBNote>> =
        noteDao.getAllNotes()

    override suspend fun getNotesFromRepo(characterId: Int): Flow<List<DBNote>> =
        noteDao.getNotes(characterId)

    override suspend fun addNote(note: DBNote) =
        noteDao.addNote(note)

    override suspend fun updateNote(note: DBNote) =
        noteDao.updateNote(note)

    override suspend fun deleteNote(note: DBNote) =
        noteDao.deleteNote(note)

    override suspend fun deleteAllNotes(character: DBCharacter) =
        noteDao.deleteAllNotes(character.id)
}