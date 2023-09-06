package com.saqeeb.testing.fragments

import android.os.Bundle
import android.os.Parcel
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.saqeeb.testing.R
import com.saqeeb.testing.adapters.DropDownItemAdapter
import com.saqeeb.testing.base.BaseFragment
import com.saqeeb.testing.databinding.SearchFlightHomeLayoutBinding
import com.saqeeb.testing.models.CalenderItem
import com.saqeeb.testing.models.CityAirport
import com.saqeeb.testing.utils.NetworkResult
import com.saqeeb.testing.utils.ResponseUtils
import com.saqeeb.testing.utils.TravellingClass
import com.saqeeb.testing.utils.TripType
import com.saqeeb.testing.viewmodels.FlightSearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


@AndroidEntryPoint
class SearchFlightHomeFragment : BaseFragment() {
    private lateinit var binding: SearchFlightHomeLayoutBinding
    private val flightViewModel: FlightSearchViewModel by activityViewModels<FlightSearchViewModel>()
    val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SearchFlightHomeLayoutBinding.inflate(inflater,container,false)
        bindObservers()
        bindTripTypeListeners()
        loadDataIntoDropDown(binding.fromAutoComplete,flightViewModel.getInitialAirports())
        loadSpinnerData()
        addViewListeners()
        loadTodayDate()
        bindListenersOnAutoCompleteText()
        return binding.root
    }

    private fun loadSpinnerData() {
        val list = arrayListOf(
            "All","Economy","Business"
        )
        binding.classSpinner.adapter = ArrayAdapter(requireContext(),R.layout.drop_down_item_layout,R.id.cityName,list)
        binding.classSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                when(position){
                    1 -> flightViewModel.travellingClass.postValue(TravellingClass.ALL)
                    2 -> flightViewModel.travellingClass.postValue(TravellingClass.ECONOMY)
                    3 -> flightViewModel.travellingClass.postValue(TravellingClass.BUSINESS)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }
    }

    private fun loadTodayDate() {
        val calender = Calendar.getInstance()
        binding.departureDate.setText(dateFormat.format(calender.time))
    }
    private fun showCalender(isDeparture:Boolean,calenderData:ArrayList<CalenderItem>){
        val availableDays = ResponseUtils.getAvailableDayFromCalenderArray(calenderData)
        val constraintsBuilder = CalendarConstraints.Builder()
        constraintsBuilder.setValidator(object :CalendarConstraints.DateValidator{
            override fun describeContents(): Int {
                return  0
            }

            override fun writeToParcel(dest: Parcel, flags: Int) {

            }
            override fun isValid(date: Long): Boolean {
                val calendar = Calendar.getInstance()
                calendar.timeInMillis = date
                val today = Calendar.getInstance()

                val day = calendar.get(Calendar.DAY_OF_WEEK)

                var isAfterDepartureDate = true
                if(isDeparture.not()){
                    val cal = Calendar.getInstance()
                    cal.timeInMillis = flightViewModel.departureDate.value?.time!!
                    isAfterDepartureDate = calendar.compareTo(cal) >=0
                }


                return (calendar.compareTo(today) >= 0) && (availableDays.contains(day)) && isAfterDepartureDate
            }


        })
        val calendarConstraints: CalendarConstraints = constraintsBuilder.build()
        val materialDatePicker = MaterialDatePicker.Builder.datePicker()
            .setCalendarConstraints(calendarConstraints)
            .setTitleText("Select ${if (isDeparture) "Departure" else "Return"} Date")
            .build()
        materialDatePicker.show(parentFragmentManager, "DatePicker");
        materialDatePicker.addOnPositiveButtonClickListener {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = it
            if(isDeparture)
                flightViewModel.departureDate.postValue(calendar.time)
            else
                flightViewModel.returnDate.postValue(calendar.time)
        }
    }

    private fun addViewListeners() {
        binding.swapCardView.setOnClickListener {
            if (flightViewModel.fromAirport.value!=null || flightViewModel.toAirport.value !=null){
                flightViewModel.swapAirports()
                val temp =  binding.fromAutoComplete.text
                binding.fromAutoComplete.text = binding.toAutoComplete.text
                binding.toAutoComplete.text = temp
            }
        }
        binding.departureCalenderIcon.setOnClickListener {
            if(flightViewModel.fromAirport.value!=null && flightViewModel.toAirport.value!=null)
                showCalender(true, flightViewModel.fetchCalenderResponse.value?.data?.get("to")!! )
            else
                showToast("Select Locations first")
        }
        binding.returnCalenderIcon.setOnClickListener {
            if(flightViewModel.fromAirport.value!=null && flightViewModel.toAirport.value!=null)
                showCalender(false, flightViewModel.fetchCalenderResponse.value?.data?.get("from")!! )
            else
                showToast("Select Locations first")
        }
        binding.passengerCount.setOnClickListener {
            findNavController().navigate(R.id.action_searchFlightHomeFragment_to_passengerCountFragment)
        }
        binding.searchFlight.setOnClickListener {
            val res =flightViewModel.isValidData()
            if(res.first){
                findNavController().navigate(R.id.action_searchFlightHomeFragment_to_resultWebView)
            }else{
                showToast(res.second)
            }
        }
    }


    private fun bindListenersOnAutoCompleteText() {
        binding.fromAutoComplete.apply {
            setOnDismissListener {
                if (text.isEmpty())
                    flightViewModel.fromAirport.postValue(null)
            }
        }
        binding.toAutoComplete.apply {
            setOnDismissListener {
                if (text.isEmpty())
                    flightViewModel.toAirport.postValue(null)
            }
        }
    }

    private fun loadDataIntoDropDown(autoComplete: AutoCompleteTextView, airports: ArrayList<CityAirport>) {
        autoComplete.setAdapter(DropDownItemAdapter(requireContext(),airports))
        autoComplete.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                val clickedCity = autoComplete.adapter.getItem(position) as CityAirport
                if(autoComplete == binding.fromAutoComplete){
                    flightViewModel.fromAirport.postValue(clickedCity)
                }else{
                    flightViewModel.toAirport.postValue(clickedCity)
                }

            }

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
        flightViewModel.fromAirport.observe(viewLifecycleOwner){
            binding.fromAutoComplete.apply {
                if(it==null) {
                    flightViewModel.toAirportList.postValue(ArrayList())
                    flightViewModel.toAirport.postValue(null)
                    setText("")
                }
                else{
                    hideSoftKeyboard()
                    flightViewModel.fetchToAirports()
                }
            }

        }
        flightViewModel.toAirport.observe(viewLifecycleOwner){
            binding.toAutoComplete.apply {
                if(it==null) {
                    flightViewModel.departureDate.postValue(null)
                    setText("")
                }
                else{
                    hideSoftKeyboard()
                    flightViewModel.fetchCalender();
                }

            }

        }
        flightViewModel.toAirportList.observe(viewLifecycleOwner){
            loadDataIntoDropDown(binding.toAutoComplete,it)
        }
        flightViewModel.departureDate.observe(viewLifecycleOwner){
            flightViewModel.returnDate.postValue(null)
            if(it==null){
                binding.departureDate.setText("")
            }else{
                binding.departureDate.setText(dateFormat.format(it))
            }
        }
        flightViewModel.returnDate.observe(viewLifecycleOwner){
            if(it==null){
                binding.returnDate.setText("")
            }else{
                binding.returnDate.setText(dateFormat.format(it))
            }
        }
        flightViewModel.groupsByCountryResponse.observe(viewLifecycleOwner){
            hideLoader()
            when(it){
                is NetworkResult.Error -> {
                    showToast(it.message)
                }
                is NetworkResult.Loading -> showLoader()
                is NetworkResult.Success -> {
                    binding.toAutoComplete.requestFocus()
                    val toAirportList = ResponseUtils.getToAirportListFromResponse(it.data)
                    if (toAirportList.isEmpty()){
                        binding.toAutoComplete.isEnabled = false
                    }else{
                        flightViewModel.toAirportList.postValue(toAirportList)
                        binding.toAutoComplete.isEnabled = true
                    }
                }
                else -> {

                }
            }
        }
        flightViewModel.fetchCalenderResponse.observe(viewLifecycleOwner){
            hideLoader()
            when(it){
                is NetworkResult.Error -> {
                    showToast(it.message)
                }
                is NetworkResult.Loading -> showLoader()
                is NetworkResult.Success -> {

                }
                else -> {

                }
            }
        }
        flightViewModel.passengersCount.observe(viewLifecycleOwner){
            binding.passengerCount.setText(it!!.toString())
        }
    }

    private fun showToast(message: String?) {
        Toast.makeText(requireActivity(),message,Toast.LENGTH_SHORT).show()
    }

    private fun hideLoader() {
        binding.loadingLayout.root.isVisible = false
    }

    private fun showLoader() {
        binding.loadingLayout.root.isVisible = true
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