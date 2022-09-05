package com.scw.twtour.util

import android.util.Log
import android.widget.Toast
import com.scw.twtour.MyApplication
import com.scw.twtour.R
import kotlinx.coroutines.FlowPreview
import timber.log.Timber
import java.io.IOException

@FlowPreview
object ErrorUtil {

    fun networkError(e: Throwable) {
        if (e is IOException) {
            Toast.makeText(
                MyApplication.get(),
                MyApplication.get().getString(R.string.network_error),
                Toast.LENGTH_SHORT
            ).show()
        }

        Timber.e(Log.getStackTraceString(e))
    }

}