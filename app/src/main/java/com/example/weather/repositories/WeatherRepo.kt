package ir.ehsan.asmrweather.repositories

import ir.ehsan.asmrweather.models.BaseModel
import ir.ehsan.asmrweather.models.DailyForecasts
import ir.ehsan.asmrweather.models.HourlyForecast
import ir.ehsan.asmrweather.models.Location

interface WeatherRepo {
    suspend fun searchLocation(query:String):BaseModel<List<Location>>
    suspend fun getDailyForecasts(locationKey:String):BaseModel<DailyForecasts>
    suspend fun getHourlyForecasts(locationKey: String):BaseModel<List<HourlyForecast>>
}