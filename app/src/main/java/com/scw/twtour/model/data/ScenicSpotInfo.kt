package com.scw.twtour.model.data

import com.scw.twtour.ext.removeDuplicateValue
import com.scw.twtour.model.entity.NoteEntity
import com.scw.twtour.model.entity.ScenicSpotEntityItem
import com.scw.twtour.constant.City
import com.scw.twtour.util.ZipCodeUtil
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

data class ScenicSpotInfo(
    var id: String = "",
    var name: String = "",
    var descriptionDetail: String? = null,
    var description: String? = null,
    var phone: String? = null,
    var address: String? = null,
    var zipCode: Int? = null,
    var zipCodeName: String? = null,
    var travelInfo: String? = null,
    var openTime: String? = null,
    var pictures: List<String> = mutableListOf(),
    var websiteUrl: String? = null,
    var position: Position? = null,
    var parkingInfo: String? = null,
    var parkingPosition: Position? = null,
    var ticketInfo: String? = null,
    var remarks: String? = null,
    var classes: List<String> = mutableListOf(),
    var city: City? = null,
    var distanceMeter: Int = 0,
    var star: Boolean = false,
    var pin: Boolean = false
) : KoinComponent {

    private val zipCodeUtil = get<ZipCodeUtil>()

    fun update(entity: ScenicSpotEntityItem): ScenicSpotInfo {
        id = entity.scenicSpotID
        name = entity.scenicSpotName
        descriptionDetail = entity.descriptionDetail
        description = entity.description
        phone = entity.phone
        address = entity.address
        zipCode = entity.zipCode
        zipCodeName = zipCodeUtil.getZipCodeName(entity.zipCode)
        travelInfo = entity.travelInfo
        openTime = entity.openTime
        pictures = mutableListOf<String>().apply {
            entity.picture?.pictureUrl1?.let { add(it) }
            entity.picture?.pictureUrl2?.let { add(it) }
            entity.picture?.pictureUrl3?.let { add(it) }
        }.removeDuplicateValue()
        websiteUrl = entity.websiteUrl
        position = Position().update(entity.position)
        parkingInfo = entity.parkingInfo
        parkingPosition = Position().update(entity.parkingPosition)
        ticketInfo = entity.ticketInfo
        remarks = entity.remarks
        classes = mutableListOf<String>().apply {
            entity.class1?.also { add(it) }
            entity.class2?.also { add(it) }
            entity.class3?.also { add(it) }
        }.removeDuplicateValue()
        entity.city?.also {
            city = City.fromValue(it)
        }
        return this
    }

    fun update(entity: NoteEntity): ScenicSpotInfo {
        star = entity.star
        pin = entity.pin
        return this
    }
}

data class Position(
    val lon: Double? = null,
    val lat: Double? = null
) {
    fun update(entity: com.scw.twtour.model.entity.Position?): Position {
        return this.copy(
            lon = entity?.positionLon,
            lat = entity?.positionLat
        )
    }
}