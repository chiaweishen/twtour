package com.scw.twtour.modole.entity

import androidx.room.*
import com.google.gson.annotations.SerializedName
import com.scw.twtour.http.data.City

class ScenicSpotEntity : ArrayList<ScenicSpotEntityItem>()

@Entity(tableName = "scenic_spot_table")
data class ScenicSpotEntityItem(
    @SerializedName("ScenicSpotID")
    @PrimaryKey
    @ColumnInfo(name = "id")
    val scenicSpotID: String = "",

    @SerializedName("ScenicSpotName")
    @ColumnInfo(name = "name")
    val scenicSpotName: String = "",

    @SerializedName("DescriptionDetail")
    @Ignore
    val descriptionDetail: String? = null,

    @SerializedName("Description")
    @Ignore
    val description: String? = null,

    @SerializedName("Phone")
    @Ignore
    val phone: String? = null,

    @SerializedName("Address")
    @Ignore
    val address: String? = null,

    @SerializedName("ZipCode")
    @ColumnInfo(name = "zip_code")
    val zipCode: Int? = null,

    @SerializedName("TravelInfo")
    @Ignore
    val travelInfo: String? = null,

    @SerializedName("OpenTime")
    @Ignore
    val openTime: String? = null,

    @SerializedName("Picture")
    @Ignore
    val picture: Picture? = null,

    @SerializedName("WebsiteUrl")
    @Ignore
    val websiteUrl: String? = null,

    @SerializedName("Position")
    @Embedded
    val position: Position? = null,

    @SerializedName("ParkingInfo")
    @Ignore
    val parkingInfo: String? = null,

    @SerializedName("ParkingPosition")
    @Ignore
    val parkingPosition: Position? = null,

    @SerializedName("TicketInfo")
    @Ignore
    val ticketInfo: String? = null,

    @SerializedName("Remarks")
    @Ignore
    val remarks: String? = null,

    @SerializedName("Class1")
    @Ignore
    val class1: String? = null,

    @SerializedName("Class2")
    @Ignore
    val class2: String? = null,

    @SerializedName("Class3")
    @Ignore
    val class3: String? = null,

    @SerializedName("City")
    val city: City? = null
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
    @ColumnInfo(name = "position_lon")
    val positionLon: Double? = null,

    @SerializedName("PositionLat")
    @ColumnInfo(name = "position_lat")
    val positionLat: Double? = null
)