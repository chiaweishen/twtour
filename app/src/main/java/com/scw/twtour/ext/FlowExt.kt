package com.scw.twtour.ext

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlin.coroutines.coroutineContext

suspend fun <T> Flow<T>.launchAndCollect(requestBlock: suspend (T) -> Unit) {
    with(CoroutineScope(coroutineContext)) {
        launch {
            collect {
                requestBlock.invoke(it)
            }
        }
    }
}