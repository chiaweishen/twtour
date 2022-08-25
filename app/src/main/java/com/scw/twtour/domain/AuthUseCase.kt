package com.scw.twtour.domain

import com.scw.twtour.model.entity.AuthTokenEntity
import com.scw.twtour.model.repository.AuthRepository
import kotlinx.coroutines.flow.Flow

interface AuthUseCase {
    fun getAuthToken(): Flow<AuthTokenEntity>
}

class AuthUseCaseImpl(
    val repository: AuthRepository
) : AuthUseCase {
    override fun getAuthToken(): Flow<AuthTokenEntity> {
        return repository.getAuthToken()
    }
}