package com.scw.twtour.model.data

sealed class HomeListItem

data class TitleItem(val text: String) : HomeListItem()
data class CityItems(val cities: List<CityInfo>) : HomeListItem()
data class NearbyItems(val scenicSpots: List<ScenicSpotInfo>) : HomeListItem()
object DiscoverNearByItem : HomeListItem()
