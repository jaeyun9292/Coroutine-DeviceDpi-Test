package com.example.coroutine_devicedpi_test.manager

import android.app.Activity
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController

class ScreenManager(val activity: Activity) {
    companion object {
        private const val TAG = "ScreenManager"
    }

    fun setFullScreen() {
        Log.d(TAG, "setFullScreen()")
        activity.window.decorView.setOnSystemUiVisibilityChangeListener { visibility ->
            // Note that system bars will only be "visible" if none of the
            // LOW_PROFILE, HIDE_NAVIGATION, or FULLSCREEN flags are set.
            if (visibility and View.SYSTEM_UI_FLAG_FULLSCREEN == 0) {
                Handler(Looper.getMainLooper()).postDelayed({
                    setFullScreen()
                }, 2000)
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // ActionBar hide
            activity.actionBar?.hide()
            activity.window.setDecorFitsSystemWindows(false) // 이 값이 true 면 기본세팅으로 화면 구성됨. 따라서, false로 해줘야함
            val controller = activity.window.insetsController
            if (controller != null) {
                controller.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
                controller.systemBarsBehavior =
                    WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE// 화면 끝을 스와이프 했을 때에만 시스템바 나타남.
            }
        } else {
            activity.actionBar?.hide()
            activity.window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_FULLSCREEN)
        }
    }
}