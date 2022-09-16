package com.example.holidayapp.repository

import com.example.holidayapp.api.RetrofitInstance
import com.example.holidayapp.db.HolidayDatabase
import com.example.holidayapp.models.HolidayItem
import java.util.*

class HolidaysRepository(
    private val db: HolidayDatabase,
) {

    private val retrofit = RetrofitInstance

    suspend fun getHolidays(countryCode: String) =
        retrofit.api.executeGetHolidays(
            getYear(),
            countryCode
        )

    private fun getYear(): String {
        val calendar = Calendar.getInstance()
        return calendar[Calendar.YEAR].toString()
    }

    suspend fun upsert(holidayItem: HolidayItem) = db.holidaysDao().upsert(holidayItem)

    fun getSavedHolidays() = db.holidaysDao().getFavoriteHolidays()

    suspend fun deleteHoliday(holidayItem: HolidayItem) =
        db.holidaysDao().deleteHoliday(holidayItem)

}