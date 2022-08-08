package com.scw.twtour.modole.entity

import com.google.gson.annotations.SerializedName

data class AuthTokenEntity(
    @SerializedName("access_token")
    val accessToken: String = "",
    @SerializedName("expires_in")
    val expiresIn: Long = 0
)