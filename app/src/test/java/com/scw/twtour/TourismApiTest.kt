package com.scw.twtour

import com.scw.twtour.http.api.TourismApi
import com.scw.twtour.http.data.City
import com.scw.twtour.http.data.ODataParams
import com.scw.twtour.http.data.ScenicSpotColumn
import com.scw.twtour.http.util.ODataFilter
import com.scw.twtour.http.util.ODataSelect
import com.scw.twtour.modole.entity.ScenicSpotEntityItem
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test
import org.koin.test.inject

@OptIn(ExperimentalCoroutinesApi::class)
class TourismApiTest : ApiTest() {

    private val api: TourismApi by inject()

    @Test
    fun `test get scenic spot`() = runTest {
        api.scenicSpot(ODataParams.Companion.Builder(500).build())
            .catch { e -> Assert.fail(e.message) }
            .collect { scenicSpots -> assert(scenicSpots) }
    }

    @Test
    fun `test get scenic spot by city`() = runTest {
        api.scenicSpot(
            ODataParams.Companion.Builder(1000)
                .select(
                    ODataSelect.ScenicSpot.Builder()
                        .add(ScenicSpotColumn.SCENIC_SPOT_ID)
                        .add(ScenicSpotColumn.SCENIC_SPOT_NAME)
                        .add(ScenicSpotColumn.ADDRESS)
                        .add(ScenicSpotColumn.ZIP_CODE)
                        .add(ScenicSpotColumn.POSITION)
                        .add(ScenicSpotColumn.PICTURE)
                        .build()
                )
                .filter(ODataFilter.ScenicSpot.byCity(City.TAIPEI))
                .build()
        )
            .catch { e -> Assert.fail(e.message) }
            .collect { scenicSpots -> assert(scenicSpots) }
    }

    @Test
    fun `test get scenic spot by id`() = runTest {
        api.scenicSpot(
            ODataParams.Companion.Builder(1)
                .filter(ODataFilter.ScenicSpot.byId("C1_379000000A_000425"))
                .build()
        )
            .catch { e -> Assert.fail(e.message) }
            .collect { scenicSpots -> assert(scenicSpots) }
    }

    private fun assert(scenicSpots: List<ScenicSpotEntityItem>) {
        Assert.assertTrue(scenicSpots.isNotEmpty())
        scenicSpots.forEach { item ->
            Assert.assertTrue(item.scenicSpotID.isNotEmpty())
            Assert.assertTrue(item.scenicSpotName.isNotEmpty())
        }
    }
}