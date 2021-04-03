package com.android.project.lightweight.di

import android.content.Context
import androidx.room.Room
import com.android.project.lightweight.api.retrofit.service.UsdaAPI
import com.android.project.lightweight.persistence.DiaryDatabase
import com.android.project.lightweight.persistence.dao.DiaryDao
import com.android.project.lightweight.persistence.dao.NutrientDao
import com.android.project.lightweight.persistence.repository.*
import com.android.project.lightweight.util.AppConstants
import com.android.project.lightweight.util.AppConstants.DIARY_DATABASE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
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
    fun provideUsdaAPI(): UsdaAPI =
        Retrofit
            .Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(AppConstants.BASE_URL)
            .build()
            .create(UsdaAPI::class.java)

    @Singleton
    @Provides
    fun provideDiaryRepository(dao: DiaryDao) = DiaryRepository(dao) as AbstractDiaryRepository

    @Singleton
    @Provides
    fun provideSearchRepository(usdaAPI: UsdaAPI) = SearchRepository(usdaAPI) as AbstractSearchRepository

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