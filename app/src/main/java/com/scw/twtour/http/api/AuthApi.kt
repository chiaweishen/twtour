package com.scw.twtour.http.api

import com.scw.twtour.modole.entity.AuthTokenEntity
import kotlinx.coroutines.flow.Flow
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST


interface AuthApi {
    @POST("/auth/realms/TDXConnect/protocol/openid-connect/token")
    @FormUrlEncoded
    fun token(
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String,
        @Field("grant_type") grantType: String = "client_credentials"
    ): Flow<AuthTokenEntity>
}