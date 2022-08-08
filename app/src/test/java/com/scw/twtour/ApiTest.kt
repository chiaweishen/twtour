package com.scw.twtour

import com.scw.twtour.di.module.apiModule
import com.scw.twtour.http.BasicApiService
import com.scw.twtour.http.api.AuthApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.koin.test.KoinTest
import org.koin.test.KoinTestRule
import org.koin.test.inject

@OptIn(ExperimentalCoroutinesApi::class)
open class ApiTest : KoinTest {

    @get:Rule
    val koinTestRule = KoinTestRule.create {
        modules(
            listOf(
                apiModule
            )
        )
    }

    companion object {
        // Api key for testing
        private const val CLIENT_ID = "ethan.scw-7aed9146-ac21-4fdb"
        private const val CLIENT_SECRET = "21999926-d334-4a84-b239-0fdda3f91893"
    }

    private val authApi: AuthApi by inject()
    private val basicApiService: BasicApiService by inject()

    @Before
    fun setup() = runTest {
        authApi.token(CLIENT_ID, CLIENT_SECRET)
            .catch { e ->
                Assert.fail(e.message)
            }
            .collect {
                basicApiService.setAccessToken(it.accessToken)
            }
    }
}