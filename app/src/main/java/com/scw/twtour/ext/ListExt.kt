package com.scw.twtour.ext

fun <T> MutableList<T>.removeDuplicateValue(): MutableList<T> {
    return ArrayList(LinkedHashSet<T>(this))
}