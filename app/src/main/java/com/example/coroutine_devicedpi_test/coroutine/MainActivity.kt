package com.example.coroutine_devicedpi_test.coroutine

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.coroutine_devicedpi_test.manager.MetricsManager
import com.example.coroutine_devicedpi_test.R
import com.example.coroutine_devicedpi_test.manager.ScreenManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"



    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val screenManager = ScreenManager(this)
        screenManager.setFullScreen()

        val metricsManager = MetricsManager()
        metricsManager.getMetrics(windowManager, this)

        val testTv: TextView = findViewById(R.id.test_tv)
        testTv.text = resources.getInteger(R.integer.test_value).toString()

        CoroutineScope(Dispatchers.Main).launch {
            repeat(100) {
                Log.e(TAG, "1",)
                delay(100)
            }
            testTv.setBackgroundColor(R.color.purple_700)
        }

        CoroutineScope(Dispatchers.IO).launch {
            repeat(100) {
                Log.e(TAG, "2",)
                delay(100)
            }
        }

        CoroutineScope(Dispatchers.IO).launch {
            repeat(100) {
                Log.e(TAG, "3",)
                delay(100)
            }
        }

        // 스코프 함수
//        val p1 = Person("jaeyun", 26)
//        println("name: ${p1.name} age: ${p1.age}")
//
//        // apply, also 는 return값으로 자기 자신을 반환
//        p1.apply {
//            age = 10
//        }
//        println("name: ${p1.name} age: ${p1.age}")
//
//        p1.also {
//            it.age = 20
//        }
//        println("name: ${p1.name} age: ${p1.age}")
//
//        // with, let, run은 리턴값이 다름
//        with(p1) {
//            age = 30
//            return@with age
//        }
//        println("name: ${p1.name} age: ${p1.age}")
//
//        p1.let {
//            it.age = 40
//            return@let it.age
//        }
//        println("name: ${p1.name} age: ${p1.age}")
//
//        p1.run {
//            age = 50
//            return@run age
//        }
//        println("name: ${p1.name} age: ${p1.age}")


//        CoroutineScope(Dispatchers.IO).launch {
//            withContext(Dispatchers.Main) { launch { test(0) } }
//            launch { test(1) }
//            launch { test(2) }
//            launch { test(3) }
//            test(4)
//            test(5)
//            test(6)
//        }


//        repeat(70) {
//            CoroutineScope(Dispatchers.IO).launch {
//                test(1)
//            }
//        }
//
//        CoroutineScope(Dispatchers.IO).launch {
//            test2(100)
//        }


//
//        CoroutineScope(Dispatchers.IO).launch {
//            val time = measureTimeMillis {
//                launch {
//                    test(1)
//                }
//                launch {
//                    test(2)
//                }
//                launch {
//                    test(3)
//                }
//                test(0)
//            }
//            Log.e(TAG, "1 - Time : $time")
//        }

//        CoroutineScope(Dispatchers.IO).launch {
//            val time = measureTimeMillis {
//                test(2)
//                test(2)
//            }
//            Log.e(TAG, "2 - Time : $time")
//        }

//        val testCoroutine = CoroutineScope(Dispatchers.IO)
//
//        val ioDispatcher = Dispatchers.IO
//
//
//        GlobalScope.launch {
//            repeat(10) {
//                test(1)
//            }
//        }
//        GlobalScope.launch {
//            repeat(10) {
//                test(2)
//            }
//        }
    }

    // 함수 두개 만들어서 같은 디스패처 달고 하나 block 시켜보기
//    private suspend fun test(index: Int) {
//        repeat(2) {
//            Thread.sleep(3000)
//            Log.e(TAG, "index : $index   |   currentThread : ${Thread.currentThread().name}")
//        }
//    }
//
//    private suspend fun test2(index: Int) {
//        repeat(10) {
//            delay(1000)
//            Log.e(TAG, "index : $index   |   currentThread : ${Thread.currentThread().name}")
//        }
//    }

    suspend fun test(index: Int) {
        delay(1000)
//        Log.e(TAG, "index: $index  |  currentThread : ${Thread.currentThread().name}")
    }

}