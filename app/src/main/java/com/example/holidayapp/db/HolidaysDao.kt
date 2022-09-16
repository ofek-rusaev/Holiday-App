package com.example.holidayapp.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.holidayapp.models.HolidayItem

@Dao
interface HolidaysDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(holidayItem: HolidayItem): Long


    @Query("SELECT * FROM holidays_db")
    fun getFavoriteHolidays(): LiveData<List<HolidayItem>>

    @Delete
    suspend fun deleteHoliday(holidayItem: HolidayItem)
}