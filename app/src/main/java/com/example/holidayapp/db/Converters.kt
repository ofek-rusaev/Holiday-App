package com.example.holidayapp.db

import androidx.room.TypeConverter
import java.util.*
import kotlin.collections.ArrayList


class Converters {
    @TypeConverter
    fun fromSource(counties: List<String>?): String {
        return counties.toString()
    }

    @TypeConverter
    fun toSource(countiesStr: String): List<String>? {
        if (countiesStr.isNullOrEmpty()) return null
        return countiesStr.split(",")
    }
}