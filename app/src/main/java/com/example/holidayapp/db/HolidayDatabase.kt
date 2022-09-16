package com.example.holidayapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.holidayapp.models.HolidayItem

@Database(
    entities = [HolidayItem::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class HolidayDatabase : RoomDatabase() {
    abstract fun holidaysDao(): HolidaysDao

    companion object {
        @Volatile
        private var instance: HolidayDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also { instance = it }
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                HolidayDatabase::class.java,
                "holidays_db"
            )
                .fallbackToDestructiveMigration()
                .build()

    }
}