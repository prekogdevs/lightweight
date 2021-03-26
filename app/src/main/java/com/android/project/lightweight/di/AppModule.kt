package com.android.project.lightweight.di

import android.content.Context
import androidx.room.Room
import com.android.project.lightweight.persistence.DiaryDatabase
import com.android.project.lightweight.utilities.AppConstants.DIARY_DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideDiaryDatabase(
        @ApplicationContext app: Context
    ) = Room.databaseBuilder(
        app,
        DiaryDatabase::class.java,
        DIARY_DATABASE_NAME
    ).build()

    @Singleton
    @Provides
    fun provideDiaryDao(db: DiaryDatabase) = db.diaryDao()

    @Singleton
    @Provides
    fun provideNutrientDao(db: DiaryDatabase) = db.nutrientDao()
}