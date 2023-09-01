package com.saqeeb.testing.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.saqeeb.testing.R
import com.saqeeb.testing.databinding.SearchFlightHomeLayoutBinding
import com.saqeeb.testing.utils.TripType
import com.saqeeb.testing.viewmodels.FlightSearchViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFlightHomeFragment : Fragment() {
    private lateinit var binding: SearchFlightHomeLayoutBinding
    private val flightViewModel: FlightSearchViewModel by activityViewModels<FlightSearchViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SearchFlightHomeLayoutBinding.inflate(inflater,container,false)
        bindObservers()
        bindTripTypeListeners()
        return binding.root
    }

    private fun bindTripTypeListeners() {
        binding.oneWayBackground.setOnClickListener {
            flightViewModel.selectedTrip.postValue(TripType.ONE_WAY)
        }
        binding.roundTripBackground.setOnClickListener {
            flightViewModel.selectedTrip.postValue(TripType.ROUND_TRIP)
        }
        binding.multiCityBackground.setOnClickListener {
            flightViewModel.selectedTrip.postValue(TripType.MULTI_CITY)
        }
    }

    private fun bindObservers() {
        flightViewModel.selectedTrip.observe(viewLifecycleOwner){
            makeTripTypeSelection(it!!)
        }
    }

    private fun makeTripTypeSelection(it: TripType) {
        unselectTripType()
        when(it){
            TripType.ONE_WAY -> {
                binding.oneWayBackground.setBackgroundColor(ResourcesCompat.getColor(resources,R.color.primaryColor,null))
                binding.oneWayText.setTextColor(ResourcesCompat.getColor(resources,R.color.white,null))
            }
            TripType.ROUND_TRIP -> {
                binding.returnInputLayout.isVisible = true
                binding.returnCalenderIcon.isVisible = true
                binding.roundTripBackground.setBackgroundColor(ResourcesCompat.getColor(resources,R.color.primaryColor,null))
                binding.roundTripText.setTextColor(ResourcesCompat.getColor(resources,R.color.white,null))
            }
            TripType.MULTI_CITY -> {
                binding.multiCityBackground.setBackgroundColor(ResourcesCompat.getColor(resources,R.color.primaryColor,null))
                binding.multiCityText.setTextColor(ResourcesCompat.getColor(resources,R.color.white,null))
            }
        }
    }

    private fun unselectTripType() {
        binding.oneWayBackground.setBackgroundColor(ResourcesCompat.getColor(resources,R.color.white,null))
        binding.roundTripBackground.setBackgroundColor(ResourcesCompat.getColor(resources,R.color.white,null))
        binding.multiCityBackground.setBackgroundColor(ResourcesCompat.getColor(resources,R.color.white,null))

        binding.oneWayText.setTextColor(ResourcesCompat.getColor(resources,R.color.primaryColor,null))
        binding.roundTripText.setTextColor(ResourcesCompat.getColor(resources,R.color.primaryColor,null))
        binding.multiCityText.setTextColor(ResourcesCompat.getColor(resources,R.color.primaryColor,null))

        binding.returnInputLayout.isVisible = false;
        binding.returnCalenderIcon.isVisible = false;
    }
}