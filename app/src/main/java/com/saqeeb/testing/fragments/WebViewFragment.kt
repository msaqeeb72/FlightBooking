package com.saqeeb.testing.fragments

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.saqeeb.testing.databinding.WebViewFragementLayoutBinding
import com.saqeeb.testing.utils.TripType
import com.saqeeb.testing.viewmodels.FlightSearchViewModel
import java.text.SimpleDateFormat

class WebViewFragment:Fragment() {

    lateinit var binding: WebViewFragementLayoutBinding
    private val flightViewModel: FlightSearchViewModel by activityViewModels<FlightSearchViewModel>()
    private val backHandler = object : OnBackPressedCallback(false){
        override fun handleOnBackPressed() {
            if(binding.webView.canGoBack())
                binding.webView.goBack()
            else
                findNavController().popBackStack()
        }

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(requireActivity(),backHandler)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        binding = WebViewFragementLayoutBinding.inflate(inflater,container,false)
        flightViewModel.apply {
            var url = "https://book-daallo.crane.aero/ibe//availability?tripType=${selectedTrip.value}" +
                    "&depPort=${fromAirport.value?.code}" +
                    "&arrPort=${toAirport.value?.code}&" +
                    "departureDate=${SimpleDateFormat("dd/MM/yyyy").format(departureDate.value)}" +
                    "&adult=${passengersCount.value?.adult}&child=${passengersCount.value?.child}&infant=${passengersCount.value!!.infant}" +
                    "&cabinClass=${travellingClass.value}&currency=USD&lang=EN\n"
            if (selectedTrip.value == TripType.ROUND_TRIP){
                url = url + "&returnDate=${SimpleDateFormat("dd/MM/yyyy").format(returnDate.value)}"
            }
            binding.webView.loadUrl(url)
            binding.webView.settings.javaScriptEnabled = true
            binding.webView.webViewClient = object : WebViewClient(){
                override fun shouldOverrideUrlLoading(
                    view: WebView?,
                    request: WebResourceRequest?
                ): Boolean {
                    binding.webView.loadUrl(request?.url.toString())
                    return false
                }

                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    binding.loadingLayout.root.isVisible = true
                    super.onPageStarted(view, url, favicon)
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    binding.loadingLayout.root.isVisible = false
                    super.onPageFinished(view, url)
                }
            }
        }
        return binding.root
    }
}