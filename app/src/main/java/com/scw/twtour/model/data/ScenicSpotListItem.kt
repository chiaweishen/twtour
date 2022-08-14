package com.scw.twtour.model.data

sealed class ScenicSpotListItem

data class TitleItem(val text: String) : ScenicSpotListItem()
data class SubtitleItem(val text: String) : ScenicSpotListItem()
data class MultipleItems(val scenicSpots: List<ScenicSpotInfo>) : ScenicSpotListItem()
