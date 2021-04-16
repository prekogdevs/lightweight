package com.android.project.lightweight.persistence.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.android.project.lightweight.persistence.entity.DiaryEntry
import com.android.project.lightweight.persistence.entity.NutrientEntry

data class DiaryEntryWithNutrients(
    @Embedded val diaryEntry: DiaryEntry,
    @Relation(
        parentColumn = "id",
        entityColumn = "diaryEntryId"
    )
    val nutrients: List<NutrientEntry>
)