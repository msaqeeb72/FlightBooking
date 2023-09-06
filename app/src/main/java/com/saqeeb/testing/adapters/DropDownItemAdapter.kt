package com.saqeeb.testing.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import com.saqeeb.testing.R
import com.saqeeb.testing.databinding.DropDownItemLayoutBinding
import com.saqeeb.testing.filters.CityDropDownFilter
import com.saqeeb.testing.models.CityAirport

class DropDownItemAdapter(val ctx:Context, val dataList:ArrayList<CityAirport>)
    :ArrayAdapter<CityAirport>(ctx, R.layout.drop_down_item_layout,dataList) {
    val filteredList = ArrayList<CityAirport>()
    override fun getCount(): Int {
        return filteredList.size
    }

    override fun getFilter(): Filter {
        return CityDropDownFilter(this,dataList)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val binding = DropDownItemLayoutBinding.inflate(LayoutInflater.from(ctx))
        binding.cityName.text = filteredList[position].displayText!!
        return binding.root
    }
}