package com.scw.twtour.http.data

class ODataParams : HashMap<String, String>() {

    companion object {

        class Builder(private val top: Int) {
            private val params = ODataParams()

            fun select(select: String): Builder {
                params["\$select"] = select
                return this
            }

            fun filter(filter: String): Builder {
                params["\$filter"] = filter
                return this
            }

            fun orderby(orderby: String): Builder {
                params["\$orderby"] = orderby
                return this
            }

            fun skip(skip: Int): Builder {
                params["\$skip"] = skip.toString()
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