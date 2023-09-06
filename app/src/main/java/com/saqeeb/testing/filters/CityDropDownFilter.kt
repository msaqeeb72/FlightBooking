package com.saqeeb.testing.filters

import android.widget.Filter
import com.saqeeb.testing.adapters.DropDownItemAdapter
import com.saqeeb.testing.models.CityAirport

class CityDropDownFilter(val dropDownItemAdapter: DropDownItemAdapter,val originalList: ArrayList<CityAirport>) : Filter(){
    val filteredList = ArrayList<CityAirport>()
    override fun performFiltering(constraint: CharSequence?): FilterResults {
        filteredList.clear()
        val results = FilterResults()
        if (constraint.isNullOrEmpty()) {
            filteredList.addAll(originalList)
        } else {
            val filterPattern = constraint.toString().lowercase().trim()
            for (airports in originalList) {
                if (airports.displayText?.lowercase()?.contains(filterPattern)!!) {
                    filteredList.add(airports)
                }
            }
        }
        results.values = filteredList
        results.count = filteredList.size
        return results
    }

    override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
        dropDownItemAdapter.filteredList.clear()
        dropDownItemAdapter.filteredList.addAll(results!!.values as ArrayList<CityAirport>)
        dropDownItemAdapter.notifyDataSetChanged()
    }

}