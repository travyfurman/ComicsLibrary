package com.example.comicslibrary.model.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Query("SELECT * FROM ${Constants.NOTE_TABLE} ORDER BY id")
    fun getAllNotes(): Flow<List<DBNote>>

    @Query("SELECT * FROM ${Constants.NOTE_TABLE} WHERE characterId = :characterId ORDER BY id ASC")
    fun getNotes(characterId: Int): Flow<List<DBNote>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addNote(note: DBNote)

    @Update
    fun updateNote(note: DBNote)

    @Delete
    fun deleteNote(note: DBNote)

    @Query("DELETE FROM ${Constants.NOTE_TABLE} WHERE characterId = :characterId")
    fun deleteAllNotes(characterId: Int)
}