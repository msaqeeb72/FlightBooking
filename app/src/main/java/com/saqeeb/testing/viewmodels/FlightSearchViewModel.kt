package com.saqeeb.testing.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saqeeb.testing.models.CalenderResponse
import com.saqeeb.testing.models.CityAirport
import com.saqeeb.testing.models.GroupsByCountryResponse
import com.saqeeb.testing.models.Passengers
import com.saqeeb.testing.repositories.FlightSearchRepository
import com.saqeeb.testing.utils.NetworkResult
import com.saqeeb.testing.utils.TravellingClass
import com.saqeeb.testing.utils.TripType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.Dispatcher
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class FlightSearchViewModel @Inject constructor( private val flightSearchRepository: FlightSearchRepository) : ViewModel(){
    val selectedTrip = MutableLiveData<TripType>(TripType.ONE_WAY)
    val toAirportList = MutableLiveData<ArrayList<CityAirport>>()
    val fromAirport = MutableLiveData<CityAirport?>(null)
    val toAirport = MutableLiveData<CityAirport?>(null)
    val departureDate = MutableLiveData<Date?>(null)
    val returnDate = MutableLiveData<Date?>(null)
    val passengersCount = MutableLiveData(Passengers())
    val travellingClass = MutableLiveData(TravellingClass.ALL)
    val groupsByCountryResponse : LiveData<NetworkResult<GroupsByCountryResponse?>>
        get() = flightSearchRepository.groupsByCountryResponse
    val fetchCalenderResponse : LiveData<NetworkResult<CalenderResponse?>>
        get() = flightSearchRepository.fetchCalenderResponse
    fun getInitialAirports(): ArrayList<CityAirport> {
        return arrayListOf<CityAirport>(
            CityAirport("DXB", code = "DXB", displayText = "Dubai (DXB)"),
            CityAirport("NBO", code = "NBO", displayText = "Nairobi (NBO)"),
            CityAirport("BSA", code = "BSA", displayText = "Bosaso (BSA)"),
            CityAirport("GGR", code = "GGR", displayText = "Garowe (GGR)"),
            CityAirport("MGQ", code = "MGQ", displayText = "Mogadishu (MGQ)"),
            CityAirport("HGA", code = "HGA", displayText = "Hargeisa (HGA)"),
        )
    }

    fun fetchToAirports() {
        viewModelScope.launch() {
            flightSearchRepository.fetchToAirports(fromAirport.value)
        }
    }
    fun fetchCalender() {
        viewModelScope.launch() {
            flightSearchRepository.fetchCalenderDetails(fromAirport.value?.code!!,toAirport.value?.code!!)
        }
    }

    fun swapAirports() {
        val temp = toAirport.value
        toAirport.postValue(fromAirport.value)
        fromAirport.postValue(temp)
    }
    fun isValidData():Pair<Boolean,String>{
        return if (fromAirport.value==null)
            Pair(false,"Select From")
        else if (toAirport.value == null)
            Pair(false,"Select To")
        else if (departureDate.value == null)
            Pair(false,"Select Departure date")
        else if (selectedTrip.value == TripType.ROUND_TRIP && returnDate.value == null)
            Pair(false,"Select Return date")
        else
            Pair(true,"")


    }
}