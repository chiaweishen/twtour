package com.scw.twtour.domain

import com.scw.twtour.network.api.AuthApi
import com.scw.twtour.model.entity.AuthTokenEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

interface AuthUseCase {
    fun getAuthToken(): Flow<AuthTokenEntity>
}

class AuthUseCaseImpl(
    private val authApi: AuthApi,
    private val clientId: String,
    private val clientSecret: String
) : AuthUseCase {
    override fun getAuthToken(): Flow<AuthTokenEntity> {
        return authApi
            .token(clientId, clientSecret)
            .flowOn(Dispatchers.IO)
    }
}