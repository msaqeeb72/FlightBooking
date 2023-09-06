package com.saqeeb.testing.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.saqeeb.testing.api.CalenderAPI
import com.saqeeb.testing.api.FlightAPI
import com.saqeeb.testing.models.CalenderResponse
import com.saqeeb.testing.models.CityAirport
import com.saqeeb.testing.models.GroupsByCountryResponse
import com.saqeeb.testing.utils.NetworkResult
import retrofit2.Response
import javax.inject.Inject

class FlightSearchRepository @Inject constructor( private val flightAPI: FlightAPI, private val calenderAPI: CalenderAPI) {

    private val _groupsByCountryResponse = MutableLiveData<NetworkResult<GroupsByCountryResponse?>>()
    val groupsByCountryResponse: LiveData<NetworkResult<GroupsByCountryResponse?>>
        get() = _groupsByCountryResponse

    private val _fetchCalenderResponse = MutableLiveData<NetworkResult<CalenderResponse?>>()
    val fetchCalenderResponse: LiveData<NetworkResult<CalenderResponse?>>
        get() = _fetchCalenderResponse
    suspend fun fetchToAirports(fromAirport: CityAirport?) {
        _groupsByCountryResponse.postValue(NetworkResult.Loading())
        val response = flightAPI.portGroupsByCountry(fromAirport?.code!!)
        handleFetchToAirportResponse(response)
    }
    suspend fun fetchCalenderDetails(fromCode: String,toCode:String) {
        _fetchCalenderResponse.postValue(NetworkResult.Loading())
        val response = calenderAPI.fetchCalender(fromCode,toCode)
        handleFetchCalenderResponse(response)
    }

    private fun handleFetchCalenderResponse(response: Response<CalenderResponse>) {
        if (response.isSuccessful && response.code() ==200){
            _fetchCalenderResponse.postValue(NetworkResult.Success(response.body()!!))
        }else{
            _fetchCalenderResponse.postValue(NetworkResult.Error(message = response.message()))
        }
    }

    private fun handleFetchToAirportResponse(response: Response<GroupsByCountryResponse>) {
        if (response.isSuccessful && response.code() ==200){
            _groupsByCountryResponse.postValue(NetworkResult.Success(response.body()!!))
        }else{
            _groupsByCountryResponse.postValue(NetworkResult.Error(message = response.message()))
        }
    }

}