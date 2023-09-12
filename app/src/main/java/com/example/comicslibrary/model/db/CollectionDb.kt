package com.example.comicslibrary.model.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [DBCharacter::class, DBNote::class], version = 1, exportSchema = false)
abstract class CollectionDB: RoomDatabase() {

    abstract fun characterDao(): CharacterDao

    abstract fun noteDao(): NoteDao

}