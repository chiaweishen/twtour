package com.scw.twtour.http.util

import com.scw.twtour.http.data.ScenicSpotColumn

object ODataSelect {

    object ScenicSpot {

        class Builder {
            private val selectors = mutableListOf<ScenicSpotColumn>()

            fun add(column: ScenicSpotColumn): Builder {
                selectors.add(column)
                return this
            }

            fun build(): String {
                return mutableListOf<String>().run {
                    selectors.map { it.value }.forEach { add(it) }
                    joinToString(", ")
                }
            }
        }
    }
}

