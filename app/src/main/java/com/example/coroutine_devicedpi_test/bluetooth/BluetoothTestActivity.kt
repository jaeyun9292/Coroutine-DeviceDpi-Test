package com.example.coroutine_devicedpi_test.bluetooth

import android.Manifest.permission.*
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattDescriptor
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothProfile
import android.bluetooth.BluetoothProfile.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.coroutine_devicedpi_test.R
import com.example.coroutine_devicedpi_test.extensions.showToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Arrays
import java.util.UUID


val UUID_HEART_RATE_SERVICE = UUID.fromString("0000180d-0000-1000-8000-00805f9b34fb")
val UUID_HEART_RATE_MEASUREMENT = UUID.fromString("00002a37-0000-1000-8000-00805f9b34fb")
val UUID_HEART_RATE_DESCRIPTOR = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb")

@RequiresApi(Build.VERSION_CODES.S)
@SuppressLint("MissingPermission")
class BluetoothTestActivity : AppCompatActivity() {

    // 블루투스 송수신 장치
    private val bluetoothAdapter: BluetoothAdapter? by lazy(LazyThreadSafetyMode.NONE) {
        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothManager.adapter
    }

    // 블루투시 스캔된 기기 리스트
    val bluetoothDevicesList = ArrayList<BluetoothDevice>()

    // 블루투스 켜기 옵션
    private val requestEnableBT = 222

    // 블루투스 권한
    private val permissions = arrayOf(BLUETOOTH_SCAN, BLUETOOTH_CONNECT)
    private val requestAllPermissions = 666

    private var devicesListView: ListView? = null
    private var scanButton: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bluetooth_test)

        devicesListView = findViewById(R.id.devicesListView)
        scanButton = findViewById(R.id.scanButton)

        if (bluetoothAdapter == null) {
            showToast("기기가 Bluetooth를 지원하지 않습니다")
            finish()
        }

        scanButton?.setOnClickListener {
            if (bluetoothAdapter?.isEnabled == true) {
                if (!hasPermissions(this, permissions)) {
                    requestPermissions(permissions, requestAllPermissions)
                    return@setOnClickListener
                }
                showToast("Bluetooth 기기 스캔 중")
                bluetoothAdapter!!.startDiscovery()
            } else {
                // 기기가 블루투스를 지원하지만 켜져있지 않은 경우
                showToast("기기의 Bluetooth가 켜져있지 않습니다")
                val bluetoothIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                startActivityForResult(bluetoothIntent, requestEnableBT);
            }
        }

        // Bluetooth 스캔 결과 처리 - ACTION_FOUND 속성은 찾은 기기를 전부 다 가져옴
        val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        registerReceiver(bluetoothReceiver, filter)
    }

    private fun hasPermissions(context: Context, permissions: Array<String>): Boolean {
        for (permission in permissions) {
            if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                showToast("권한이 거부되었습니다")
                return false
            }
        }
        return true
    }

    private val gattCallback = object : BluetoothGattCallback() {
        override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                gatt.discoverServices()
                showToast("기기와 연결이 되었습니다")
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                showToast("기기의 연결이 끊겼습니다")
            }
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
            // GATT 서비스 검색이 완료되면 실행할 코드
            if (status == BluetoothGatt.GATT_SUCCESS) {
                CoroutineScope(Dispatchers.Main).launch {
                    showToast("서비스 검색 완료")
                }

                // GATT 서비스를 찾습니다
                val service = gatt.getService(UUID_HEART_RATE_SERVICE)
                // GATT 특성을 찾습니다
                val characteristic = service.getCharacteristic(UUID_HEART_RATE_MEASUREMENT)

                // 블루투스 특성 알림 - 1
                gatt.setCharacteristicNotification(characteristic, true)

                // 해당 특성을 가르키는 디스크립터 값 설정
                val descriptor = characteristic.getDescriptor(UUID_HEART_RATE_DESCRIPTOR)
                // 블루투스 디스크립터 알림 - 2
                descriptor.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE

                // 특성 알림과 디스크립터 알림 2개 다 설정해야 안드로이드앱에서 데이터 수신 가능

                // 디스크립터 값을 BLE 장치로 전송
                gatt.writeDescriptor(descriptor)
            }
        }

        override fun onCharacteristicChanged(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic) {
            // 연결된 장치에서 데이터가 수신되면 실행할 코드
            val data = characteristic.value
            val dataString = Arrays.toString(data)
            val bpmData = dataString.split(",")[1].trim()
            CoroutineScope(Dispatchers.Main).launch {
                showToast(bpmData)
            }
        }
    }

    // Bluetooth 장치에 연결
    fun connectToBluetoothDevice(device: BluetoothDevice, context: Context) {
        // 연결 성공 여부 확인을 위해 gatt 변수를 사용합니다.
        // 연결 성공시 onConnectionStateChange() 콜백이 호출됩니다.
        device.connectGatt(context, false, gattCallback)
    }

    private val bluetoothReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                BluetoothDevice.ACTION_FOUND -> {
                    val device: BluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE) ?: return
//                    if (device.uuids?.any { it.uuid == UUID_HEART_RATE_SERVICE } == true) {
//                        Log.e("TAG", device.name)
                    if (device.name?.startsWith("Galaxy Watch4") == true) {
                        Log.e("TAG", "onReceive: ${device.type}", )
                        if (!bluetoothDevicesList.contains(device)) {
                            connectToBluetoothDevice(device, context)
                            bluetoothDevicesList.add(device)
                            devicesListView?.adapter = ArrayAdapter(
                                context, android.R.layout.simple_list_item_1, bluetoothDevicesList
                            )
                        }
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(bluetoothReceiver)
    }
}