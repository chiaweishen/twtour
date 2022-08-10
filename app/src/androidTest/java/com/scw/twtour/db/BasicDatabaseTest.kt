package com.scw.twtour.db

import com.scw.twtour.db.dao.ScenicSpotDao
import com.scw.twtour.db.util.ScenicSpotUtil
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.koin.java.KoinJavaComponent.get

@ExperimentalCoroutinesApi
class BasicDatabaseTest {

    private lateinit var database: BasicDatabase
    private lateinit var scenicSpotDao: ScenicSpotDao

    @Before
    fun setUp() {
        database = get(BasicDatabase::class.java)
        scenicSpotDao = database.scenicSpotDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun test() = runTest {
        val scenicSpot = ScenicSpotUtil.getScenicSpot()
        scenicSpotDao.deleteAll()
        scenicSpotDao.insertScenicSpot(scenicSpot)

        scenicSpotDao.queryScenicSpots().first().let { scenicSpots ->
            assertTrue(scenicSpots.size == 1)
            scenicSpots[0].apply {
                assertTrue(scenicSpotID == scenicSpot.scenicSpotID)
                assertTrue(scenicSpotName == scenicSpot.scenicSpotName)
                assertTrue(zipCode == scenicSpot.zipCode)
                assertTrue(city == scenicSpot.city)
            }
        }

        scenicSpotDao.deleteAll()
        scenicSpotDao.queryScenicSpots().first().let { scenicSpots ->
            assertTrue(scenicSpots.isEmpty())
        }
    }
}