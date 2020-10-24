package com.android.project.lightweight.persistence.converter

import androidx.room.TypeConverter
import com.android.project.lightweight.persistence.entity.FoodNutrient
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.util.*

// TODO: Rework this class
//  in the table it looks weird
class FoodNutrientConverter {

    private var gson: Gson = Gson()

    @TypeConverter
    fun stringToSomeObjectList(data: String?): List<FoodNutrient?>? {
        if (data == null) {
            return Collections.emptyList()
        }
        val listType: Type = object : TypeToken<List<FoodNutrient?>?>() {}.type
        return gson.fromJson<List<FoodNutrient>>(data, listType)
    }

    @TypeConverter
    fun someObjectListToString(someObjects: List<FoodNutrient?>?): String? {
        return gson.toJson(someObjects)
    }
}