package com.example.holidayapp.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.holidayapp.db.HolidayDatabase
import com.example.holidayapp.models.HolidayItem
import com.example.holidayapp.models.HolidaysResponse
import com.example.holidayapp.repository.HolidaysRepository
import com.example.holidayapp.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

private const val TAG = "HolidaysViewModel"

class HolidaysViewModel (
    application: Application,
): AndroidViewModel(application) {

    private val holidayDatabase = HolidayDatabase(application)
    private val holidaysRepository = HolidaysRepository(holidayDatabase)

    val holidays: MutableLiveData<Resource<HolidaysResponse>> = MutableLiveData()

    fun getHolidays(countryCode: String) =
        viewModelScope.launch(Dispatchers.IO) {
            holidays.postValue(Resource.Loading())
            try {
                val response = holidaysRepository.getHolidays(countryCode)
                holidays.postValue(handleHolidaysResponse(response))
            } catch (ex: Exception) {
                Log.d(TAG, "getHolidays, Exception: $ex")
            }
        }

    private fun handleHolidaysResponse(response: Response<HolidaysResponse?>): Resource<HolidaysResponse> {
        if (response.isSuccessful) {
            response.body()?.let {
                return Resource.Success(it)
            }
        }
        return Resource.Error(response.message())
    }

    fun saveHoliday(holidayItem: HolidayItem) = viewModelScope.launch(Dispatchers.IO) {
        holidaysRepository.upsert(holidayItem)
    }

    fun getSavedHolidays() = holidaysRepository.getSavedHolidays()

    fun deleteHoliday(holidayItem: HolidayItem) = viewModelScope.launch {
        holidaysRepository.deleteHoliday(holidayItem)
    }
}