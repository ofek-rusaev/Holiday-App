package com.example.holidayapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "holidays_db")
data class HolidayItem(
    @PrimaryKey(autoGenerate = false)
    val name: String,
    val counties: List<String>?,
    val countryCode: String?,
    val date: String?,
    val fixed: Boolean?,
    val global: Boolean?,
    val launchYear: Int?,
    val localName: String?,
    val types: List<String>?
): Serializable