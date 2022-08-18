package com.scw.twtour

import com.scw.twtour.model.entity.ScenicSpotEntityItem
import com.scw.twtour.network.api.TourismApi
import com.scw.twtour.network.util.ODataFilter
import com.scw.twtour.network.util.ODataParams
import com.scw.twtour.network.util.ODataSelect
import com.scw.twtour.util.City
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
        api.scenicSpot(ODataParams.Companion.Builder(50).skip(1234).build())
            .catch { e -> Assert.fail(e.message) }
            .collect { scenicSpots -> assert(scenicSpots) }
    }

    @Test
    fun `test get scenic spot by city`() = runTest {
        api.scenicSpot(
            ODataParams.Companion.Builder(1000)
                .filter(ODataFilter.ScenicSpot.byCity(City.TAIPEI))
                .select(
                    ODataSelect.Builder()
                        .add(ScenicSpotEntityItem::scenicSpotID.name)
                        .add(ScenicSpotEntityItem::scenicSpotName.name)
                        .build()
                )
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