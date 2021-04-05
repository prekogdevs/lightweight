package com.android.project.lightweight.di

import android.content.Context
import androidx.room.Room
import com.android.project.lightweight.persistence.DiaryDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
class TestAppModule {
    @Provides
    @Named("in_memory_database")
    fun proveInMemoryDatabase(@ApplicationContext context: Context) = Room.inMemoryDatabaseBuilder(
        context,
        DiaryDatabase::class.java
    ).allowMainThreadQueries().build()
}