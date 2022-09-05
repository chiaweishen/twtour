package com.scw.twtour.util

import android.content.Context
import android.os.Build
import android.util.DisplayMetrics
import android.view.WindowManager

object ScreenUtil {

    fun getScreenSize(context: Context): IntArray {
        return if (Build.VERSION.SDK_INT <= 29) {
            val dm = DisplayMetrics()
            (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager)
                .defaultDisplay
                .getMetrics(dm)
            intArrayOf(dm.widthPixels, dm.heightPixels)
        } else {
            val rect = (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager)
                .currentWindowMetrics.bounds
            intArrayOf(rect.width(), rect.height())
        }
    }

    fun getScreenWidth(context: Context): Int = getScreenSize(context)[0]

    fun getScreenHeight(context: Context): Int = getScreenSize(context)[1]

}