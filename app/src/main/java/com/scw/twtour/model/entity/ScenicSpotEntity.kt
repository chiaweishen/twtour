package com.scw.twtour.model.entity

import androidx.room.*
import com.google.gson.annotations.SerializedName
import com.scw.twtour.constant.City

class ScenicSpotEntity : ArrayList<ScenicSpotEntityItem>()

@Entity(tableName = "scenic_spot_table")
data class ScenicSpotEntityItem(
    @SerializedName("ScenicSpotID")
    @ColumnInfo(name = "id")
    @PrimaryKey
    var scenicSpotID: String = "",

    @SerializedName("ScenicSpotName")
    @Ignore
    var scenicSpotName: String = "",

    @SerializedName("DescriptionDetail")
    @Ignore
    var descriptionDetail: String? = null,

    @SerializedName("Description")
    @Ignore
    var description: String? = null,

    @SerializedName("Phone")
    @Ignore
    var phone: String? = null,

    @SerializedName("Address")
    @Ignore
    var address: String? = null,

    @SerializedName("ZipCode")
    @Ignore
    var zipCode: Int? = null,

    @SerializedName("TravelInfo")
    @Ignore
    var travelInfo: String? = null,

    @SerializedName("OpenTime")
    @Ignore
    var openTime: String? = null,

    @SerializedName("Picture")
    @Ignore
    var picture: Picture? = null,

    @SerializedName("WebsiteUrl")
    @Ignore
    var websiteUrl: String? = null,

    @SerializedName("Position")
    @Embedded
    var position: Position? = null,

    @SerializedName("ParkingInfo")
    @Ignore
    var parkingInfo: String? = null,

    @SerializedName("ParkingPosition")
    @Ignore
    var parkingPosition: Position? = null,

    @SerializedName("TicketInfo")
    @Ignore
    var ticketInfo: String? = null,

    @SerializedName("Remarks")
    @Ignore
    var remarks: String? = null,

    @SerializedName("Class1")
    @Ignore
    var class1: String? = null,

    @SerializedName("Class2")
    @Ignore
    var class2: String? = null,

    @SerializedName("Class3")
    @Ignore
    var class3: String? = null,

    @SerializedName("City")
    @Ignore
    var city: City? = null
)

data class Picture(
    @SerializedName("PictureUrl1")
    var pictureUrl1: String? = null,

    @SerializedName("PictureUrl2")
    var pictureUrl2: String? = null,

    @SerializedName("PictureUrl3")
    var pictureUrl3: String? = null,
)

data class Position(
    @SerializedName("PositionLon")
    @ColumnInfo(name = "position_lon")
    var positionLon: Double? = null,

    @SerializedName("PositionLat")
    @ColumnInfo(name = "position_lat")
    var positionLat: Double? = null
)