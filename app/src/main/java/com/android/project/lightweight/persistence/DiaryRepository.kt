package com.android.project.lightweight.persistence

class DiaryRepository(diaryDao: DiaryDao, consumptionDate: String) {
    val foods = diaryDao.getFoods(consumptionDate)
}