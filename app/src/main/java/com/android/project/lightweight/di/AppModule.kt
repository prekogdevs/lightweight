package com.android.project.lightweight.di

import android.content.Context
import androidx.room.Room
import com.android.project.lightweight.persistence.DiaryDatabase
import com.android.project.lightweight.persistence.dao.DiaryDao
import com.android.project.lightweight.persistence.dao.NutrientDao
import com.android.project.lightweight.persistence.repository.AbstractDiaryRepository
import com.android.project.lightweight.persistence.repository.AbstractNutrientRepository
import com.android.project.lightweight.persistence.repository.DiaryRepository
import com.android.project.lightweight.persistence.repository.NutrientRepository
import com.android.project.lightweight.util.AppConstants.DIARY_DATABASE_NAME
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
    fun provideDiaryRepository(dao: DiaryDao) = DiaryRepository(dao) as AbstractDiaryRepository

    @Singleton
    @Provides
    fun provideNutrientRepository(dao: NutrientDao) = NutrientRepository(dao) as AbstractNutrientRepository

    @Singleton
    @Provides
    fun provideDiaryDao(db: DiaryDatabase) = db.diaryDao()

    @Singleton
    @Provides
    fun provideNutrientDao(db: DiaryDatabase) = db.nutrientDao()
}