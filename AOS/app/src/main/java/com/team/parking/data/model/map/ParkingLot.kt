package com.team.parking.data.model.map


import com.google.gson.annotations.SerializedName

data class ParkingLot(
    @SerializedName("feeBasic")
    val feeBasic: Int,
    @SerializedName("lat")
    val lat: Double,
    @SerializedName("lng")
    val lng: Double,
    @SerializedName("parkType")
    val parkType: Int
)