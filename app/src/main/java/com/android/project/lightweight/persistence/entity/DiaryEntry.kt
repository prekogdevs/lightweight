package com.android.project.lightweight.persistence.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "Diary")
data class DiaryEntry(
    val fdcId: Long,
    val description: String,
    var consumedOn: Long
) : Parcelable {
    @IgnoredOnParcel
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
}