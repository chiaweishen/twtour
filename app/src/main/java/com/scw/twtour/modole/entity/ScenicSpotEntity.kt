package com.scw.twtour.modole.entity
import com.google.gson.annotations.SerializedName


class ScenicSpotEntity : ArrayList<ScenicSpotEntityItem>()


data class ScenicSpotEntityItem(
    @SerializedName("ScenicSpotID")
    val scenicSpotID: String = "",
    @SerializedName("ScenicSpotName")
    val scenicSpotName: String = "",
    @SerializedName("DescriptionDetail")
    val descriptionDetail: String = "",
    @SerializedName("Description")
    val description: String = "",
    @SerializedName("Phone")
    val phone: String? = null,
    @SerializedName("Address")
    val address: String? = null,
    @SerializedName("ZipCode")
    val zipCode: String? = null,
    @SerializedName("TravelInfo")
    val travelInfo: String? = null,
    @SerializedName("OpenTime")
    val openTime: String? = null,
    @SerializedName("Picture")
    val picture: Picture? = null,
    @SerializedName("WebsiteUrl")
    val websiteUrl: String? = null,
    @SerializedName("Position")
    val position: Position? = null,
    @SerializedName("ParkingInfo")
    val parkingInfo: String? = null,
    @SerializedName("ParkingPosition")
    val parkingPosition: Position? = null,
    @SerializedName("TicketInfo")
    val ticketInfo: String? = null,
    @SerializedName("Remarks")
    val remarks: String? = null,
    @SerializedName("Class1")
    val class1: String? = null,
    @SerializedName("Class2")
    val class2: String? = null,
    @SerializedName("Class3")
    val class3: String? = null,
    @SerializedName("City")
    val city: String? = null
)

data class Picture(
    @SerializedName("PictureUrl1")
    val pictureUrl1: String? = null,
    @SerializedName("PictureUrl2")
    val pictureUrl2: String? = null,
    @SerializedName("PictureUrl3")
    val pictureUrl3: String? = null,
)

data class Position(
    @SerializedName("PositionLon")
    val positionLon: Double = 0.0,
    @SerializedName("PositionLat")
    val positionLat: Double = 0.0
)