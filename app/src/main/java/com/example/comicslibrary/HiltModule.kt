package com.example.comicslibrary

import android.content.Context
import androidx.room.Room
import com.example.comicslibrary.model.api.ApiService
import com.example.comicslibrary.model.api.MarvelApiRepo
import com.example.comicslibrary.model.connectivity.ConnectivityMonitor
import com.example.comicslibrary.model.db.CharacterDao
import com.example.comicslibrary.model.db.CollectionDB
import com.example.comicslibrary.model.db.CollectionDbRepo
import com.example.comicslibrary.model.db.CollectionDbRepoImpl
import com.example.comicslibrary.model.db.Constants.DB
import com.example.comicslibrary.model.db.NoteDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ViewModelComponent::class)
class HiltModule {
    @Provides
    fun provideApiRepo() = MarvelApiRepo(ApiService.api)

    @Provides
    fun provideCollectionDb(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, CollectionDB::class.java, DB).build()

    @Provides
    fun provideCharacterDao(collectionDB: CollectionDB) = collectionDB.characterDao()

    @Provides
    fun provideNoteDao(collectionDB: CollectionDB) = collectionDB.noteDao()

    @Provides
    fun provideDbRepoImpl(characterDao: CharacterDao, noteDao: NoteDao): CollectionDbRepo =
        CollectionDbRepoImpl(characterDao, noteDao)

    @Provides
    fun provideConnectivityManager(@ApplicationContext context: Context) =
        ConnectivityMonitor.getInstance(context)

}