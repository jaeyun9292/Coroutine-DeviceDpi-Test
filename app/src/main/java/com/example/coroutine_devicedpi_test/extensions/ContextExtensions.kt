package com.example.coroutine_devicedpi_test.extensions

import android.content.Context
import android.content.res.Configuration
import android.util.Log
import android.widget.Toast
import androidx.annotation.StringRes

fun Context.showToast(@StringRes stringRes: Int, duration: Int = Toast.LENGTH_SHORT) {
    showToast(getString(stringRes), duration)
}

fun Context.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Log.e("TEST", "showToast: $message")
    Toast.makeText(this, message, duration).show()
}

val Context.isLandscape: Boolean get() = resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

val Context.isTablet: Boolean get() = resources.configuration.smallestScreenWidthDp >= 600


