package com.saqeeb.testing.fragments

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.saqeeb.testing.databinding.PassengetCountSheetLayoutBinding
import com.saqeeb.testing.viewmodels.FlightSearchViewModel

class PassengersCountSheet : BottomSheetDialogFragment() {

    lateinit var binding: PassengetCountSheetLayoutBinding
    private val flightViewModel: FlightSearchViewModel by activityViewModels<FlightSearchViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = PassengetCountSheetLayoutBinding.inflate(inflater,container,false);
        binding.viewModel = flightViewModel
        binding.lifecycleOwner = this
        binding.apply {
            addAdult.setOnClickListener {
                val obj = flightViewModel.passengersCount.value!!
                obj.adult = (obj.adult.toInt()+1).toString()
                flightViewModel.passengersCount.postValue(obj)
            }
            removeAdult.setOnClickListener {
                val obj = flightViewModel.passengersCount.value!!
                if(obj.adult == "1")
                    return@setOnClickListener
                obj.adult = (obj.adult.toInt()-1).toString()
                flightViewModel.passengersCount.postValue(obj)
            }
            addChild.setOnClickListener {
                val obj = flightViewModel.passengersCount.value!!
                obj.child = (obj.child.toInt()+1).toString()
                flightViewModel.passengersCount.postValue(obj)
            }
            removeChild.setOnClickListener {
                val obj = flightViewModel.passengersCount.value!!
                if(obj.child == "0")
                    return@setOnClickListener
                obj.child = (obj.child.toInt()-1).toString()
                flightViewModel.passengersCount.postValue(obj)
            }
            addInfant.setOnClickListener {
                val obj = flightViewModel.passengersCount.value!!
                obj.infant = (obj.infant.toInt()+1).toString()
                flightViewModel.passengersCount.postValue(obj)
            }
            removeInfant.setOnClickListener {
                val obj = flightViewModel.passengersCount.value!!
                if(obj.infant == "0")
                    return@setOnClickListener
                obj.infant = (obj.infant.toInt()-1).toString()
                flightViewModel.passengersCount.postValue(obj)
            }
            confirmButton.setOnClickListener {
                dismiss()
            }
        }
        return binding.root
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState)
    }
}