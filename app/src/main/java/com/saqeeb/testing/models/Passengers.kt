package com.saqeeb.testing.models

data class Passengers(
    var adult:String="1",
    var child:String="0",
    var infant:String="0"
){
    override fun toString(): String {
        return "$adult + $child + $infant"
    }
}
