package com.example.coroutine_devicedpi_test.manager

import android.content.Context
import android.graphics.Point
import android.util.DisplayMetrics
import android.util.Log
import android.view.WindowManager
import kotlin.math.roundToInt

class MetricsManager {
    companion object {
        private const val TAG = "MetricsManager"
    }

    fun getMetrics(windowManager: WindowManager, context: Context) {
        // 기기 해상도 확인 코드
        var dpi = ""
        val display = windowManager.defaultDisplay
        val size = Point()
        display?.getRealSize(size)

        val metrics = DisplayMetrics()
        windowManager.defaultDisplay?.getRealMetrics(metrics)

        val a = context.resources.displayMetrics.densityDpi
        val b = context.resources.displayMetrics.density
        val c = context.resources.displayMetrics.scaledDensity
        Log.e(TAG, "densityDpi : $a   density : $b   scaledDensity : $c")


        // Galaxy S8 2560 x 1600

        //dpi 1인치당 들어가는 픽셀 수 ex) 160dpi = 1인치에 160픽셀
        if (metrics.densityDpi <= 160) { // mdpi
            dpi = "mdpi"
        } else if (metrics.densityDpi <= 240) { // hdpi
            dpi = "hdpi"
        } else if (metrics.densityDpi <= 320) { // xhdpi
            dpi = "xhdpi"
        } else if (metrics.densityDpi <= 480) { // xxhdpi
            dpi = "xxhdpi"
        } else if (metrics.densityDpi <= 640) { // xxxhdpi
            dpi = "xxxhdpi"
        }

        val px = (metrics.densityDpi.toDouble() / 160)
        Log.e(TAG, "@@ display => size.x : " + size.x + ", size.y : " + size.y)
        Log.e(TAG, "@@ dpi => " + metrics.densityDpi + "(" + dpi + ")")
        Log.e(TAG, "@@ 1dp => ${px}px")
        Log.e(TAG, "@@ dp size => ${(size.x / px).roundToInt()}dp x ${(size.y / px).roundToInt()}dp")
    }
}