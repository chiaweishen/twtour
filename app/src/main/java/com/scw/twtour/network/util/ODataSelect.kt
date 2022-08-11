package com.scw.twtour.network.util

object ODataSelect {

    class Builder {
        private val selectors = mutableListOf<String>()

        fun add(columnName: String): Builder {
            selectors.add(columnName)
            return this
        }

        fun build(): String {
            return selectors.joinToString(", ")
        }
    }
}

