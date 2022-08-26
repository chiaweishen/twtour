package com.scw.twtour.model.data

sealed class Result<out R> {
    object Loading : Result<Nothing>()
    data class Success<out T>(val value: T) : Result<T>()
    data class Error(val e: Exception) : Result<Nothing>()
}