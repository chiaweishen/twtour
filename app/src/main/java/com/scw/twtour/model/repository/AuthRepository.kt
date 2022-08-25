package com.scw.twtour.model.repository

import com.scw.twtour.MyApplication
import com.scw.twtour.model.entity.AuthTokenEntity
import com.scw.twtour.network.api.AuthApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun getAuthToken(): Flow<AuthTokenEntity>
}

@FlowPreview
class AuthRepositoryImpl(private val authApi: AuthApi) : AuthRepository {
    override fun getAuthToken(): Flow<AuthTokenEntity> {
        // Bad Smell
        return authApi.token(MyApplication.getClientId(), MyApplication.getClientSecret())
    }
}