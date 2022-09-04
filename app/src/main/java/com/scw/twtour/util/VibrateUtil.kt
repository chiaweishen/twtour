package com.scw.twtour.util

import android.app.Service
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator

object VibrateUtil {

    fun tick(context: Context) {
        val vibrator = context.getSystemService(Service.VIBRATOR_SERVICE) as Vibrator
        if (vibrator.hasVibrator()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                val effect = VibrationEffect.createPredefined(VibrationEffect.EFFECT_TICK)
                vibrator.cancel()
                vibrator.vibrate(effect)
            }
        }
    }

}