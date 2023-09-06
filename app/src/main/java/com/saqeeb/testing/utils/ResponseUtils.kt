package com.saqeeb.testing.utils

import com.saqeeb.testing.models.CalenderItem
import com.saqeeb.testing.models.CityAirport
import com.saqeeb.testing.models.GroupsByCountryResponse
import java.util.Calendar

class ResponseUtils {
    companion object{
        fun getToAirportListFromResponse(data: GroupsByCountryResponse?):ArrayList<CityAirport>{
            val airportList = ArrayList<CityAirport>()
            data?.forEach { countryMap ->
                countryMap.values.forEach { airports ->
                    airports.forEach {
                        airportList.add(it)
                    }
                }
            }
            return airportList

        }

        fun getAvailableDayFromCalenderArray(calenderData: ArrayList<CalenderItem>): ArrayList<Int> {
            val res = ArrayList<Int>()
            calenderData.forEach {
                res.apply {
                    when(Day.valueOf(it.DAY)){
                        Day.MON -> add(Calendar.MONDAY)
                        Day.TUE -> add(Calendar.TUESDAY)
                        Day.WED -> add(Calendar.WEDNESDAY)
                        Day.THU -> add(Calendar.TUESDAY)
                        Day.FRI -> add(Calendar.FRIDAY)
                        Day.SAT -> add(Calendar.SATURDAY)
                        Day.SUN -> add(Calendar.SUNDAY)
                    }
                }

            }
            return res
        }
    }
}