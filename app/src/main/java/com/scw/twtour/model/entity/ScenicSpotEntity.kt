package com.scw.twtour.model.entity

import androidx.room.*
import com.google.gson.annotations.SerializedName
import com.scw.twtour.network.data.City

class ScenicSpotEntity : ArrayList<ScenicSpotEntityItem>()

@Entity(tableName = "scenic_spot_table")
data class ScenicSpotEntityItem(
    @SerializedName("ScenicSpotID")
    @ColumnInfo(name = "scenic_spot_id")
    @PrimaryKey
    var scenicSpotID: String = "",

    @SerializedName("ScenicSpotName")
    @ColumnInfo(name = "scenic_spot_name")
    var scenicSpotName: String = "",

    @SerializedName("DescriptionDetail")
    @Ignore
    var descriptionDetail: String? = null,

    @SerializedName("Description")
    var description: String? = null,

    @SerializedName("Phone")
    @Ignore
    var phone: String? = null,

    @SerializedName("Address")
    @Ignore
    var address: String? = null,

    @SerializedName("ZipCode")
    @ColumnInfo(name = "zip_code")
    var zipCode: Int? = null,

    @SerializedName("TravelInfo")
    @Ignore
    var travelInfo: String? = null,

    @SerializedName("OpenTime")
    @Ignore
    var openTime: String? = null,

    @SerializedName("Picture")
    @Embedded
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
    var city: City? = null
)

data class Picture(
    @SerializedName("PictureUrl1")
    @ColumnInfo(name = "pic_url_1")
    var pictureUrl1: String? = null,

    @SerializedName("PictureUrl2")
    @ColumnInfo(name = "pic_url_2")
    var pictureUrl2: String? = null,

    @SerializedName("PictureUrl3")
    @ColumnInfo(name = "pic_url_3")
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