package com.example.holidayapp.api

import com.example.holidayapp.models.HolidaysResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface HolidaysApi {

    @GET("api/v3/PublicHolidays/{year}/{countryCode}")
    suspend fun executeGetHolidays(
        @Path("year") currentYear: String,
        @Path("countryCode") countryCode: String,
    ) : Response<HolidaysResponse?>
}