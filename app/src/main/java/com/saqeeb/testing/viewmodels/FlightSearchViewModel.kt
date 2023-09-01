package com.saqeeb.testing.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.saqeeb.testing.repositories.FlightSearchRepository
import com.saqeeb.testing.utils.TripType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FlightSearchViewModel @Inject constructor( private val flightSearchRepository: FlightSearchRepository) : ViewModel(){
    val selectedTrip = MutableLiveData<TripType>(TripType.ONE_WAY)
}