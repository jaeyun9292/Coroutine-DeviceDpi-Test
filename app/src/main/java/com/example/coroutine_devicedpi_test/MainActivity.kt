package com.example.coroutine_devicedpi_test

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.system.measureTimeMillis

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val screenManager = ScreenManager(this)
        screenManager.setFullScreen()

        val testTv: TextView = findViewById(R.id.test_tv)
        testTv.text = resources.getInteger(R.integer.test_value).toString()

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


        CoroutineScope(Dispatchers.IO).launch {
            val time = measureTimeMillis {
                launch {
                    test(1)
                }
                launch {
                    test(2)
                }
                launch {
                    test(3)
                }
                test(0)
            }
            Log.e(TAG, "1 - Time : $time")
        }

//        CoroutineScope(Dispatchers.IO).launch {
//            val time = measureTimeMillis {
//                test(2)
//                test(2)
//            }
//            Log.e(TAG, "2 - Time : $time")
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
        Log.e(TAG, "index: $index  |  currentThread : ${Thread.currentThread().name}")
    }

}