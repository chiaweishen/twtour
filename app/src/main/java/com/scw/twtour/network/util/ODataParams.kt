package com.scw.twtour.network.util

class ODataParams : HashMap<String, String>() {

    companion object {

        class Builder(private val top: Int) {
            private val params = ODataParams()

            fun select(select: String?): Builder {
                select?.also {
                    params["\$select"] = it
                }
                return this
            }

            fun filter(filter: String?): Builder {
                filter?.also {
                    params["\$filter"] = it
                }
                return this
            }

            fun orderby(orderby: String?): Builder {
                orderby?.also {
                    params["\$orderby"] = it
                }
                return this
            }

            fun skip(skip: Int?): Builder {
                skip?.also {
                    params["\$skip"] = it.toString()
                }
                return this
            }

            fun build(): ODataParams {
                params["\$format"] = "JSON"
                params["\$top"] = top.toString()
                return params
            }
        }
    }

}