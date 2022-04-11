package com.alzio.mycompassapp

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import com.alzio.mycompassapp.databinding.ActivityMainBinding
import android.os.Bundle
import android.util.Log
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import android.Manifest
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlin.math.round

class MainActivity : AppCompatActivity(), SensorEventListener, OnMapReadyCallback {
    val vbinding by lazy { ActivityMainBinding.inflate(layoutInflater) }



    // 센서
    lateinit var sensor_manager : SensorManager

    val accelerometerReding = FloatArray(3)
    val magetometerReding = FloatArray(3)

    val rotationMatrix = FloatArray(9)
    val orientationAngles = FloatArray(3)

    var angle: Double = 0.0


    // 구글 맵
    private lateinit var mMap: GoogleMap
    val permission = Manifest.permission.ACCESS_FINE_LOCATION

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(vbinding.root)

        // 센서
        sensor_manager = getSystemService(SENSOR_SERVICE) as SensorManager

        if(isPermitted()){
            startGoogleMapProcess()
        }else{
            var permissions = arrayOf(permission)
            ActivityCompat.requestPermissions(this, permissions, 67)
        }
    }

    fun isPermitted() : Boolean{
        if (ContextCompat.checkSelfPermission(this, permission) != PERMISSION_GRANTED){
            return false
        }
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when(requestCode){
            67 -> {
                if(grantResults.get(0) != PERMISSION_GRANTED){
                    Toast.makeText(this, "권한을 승인하셔야지만 앱을 사용하실 수 있습니다.", Toast.LENGTH_LONG).show()
                    finish()
                }else{
                    startGoogleMapProcess()
                }
            }
        }
    }


    /// 이제 위치 추적할 차례!  준비는 끝!  ++ 그리고 출시!  하면 끝 



    fun startGoogleMapProcess(){
        // 구글 맵
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    // 구글 맵
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(37.5283169, 126.9294254)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }



    // 센서
    override fun onResume() {
        super.onResume()
        sensor_manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)?.also { accelerometer ->
            sensor_manager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_DELAY_UI)
        }
        sensor_manager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)?.also { magneticField ->
            sensor_manager.registerListener(this, magneticField, SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onPause() {
        super.onPause()
        sensor_manager.unregisterListener(this)
    }

    override fun onSensorChanged(p0: SensorEvent?) {
        // 1. 센서 이벤트가 제대로 들어온게 맞는가?
        if (p0 == null) return
        // 2. 이벤트 처리 (1. 센서의 값을 수집, 2. 수집한 값으로 현재 기기가 향하고 있는 방향을 계산합니다, 3. 방향을 화면에 표시(이미지 회전) )

        // 2-1. 센서의 값을 수집
        if (p0.sensor.type == Sensor.TYPE_ACCELEROMETER){
            System.arraycopy(p0.values, 0, accelerometerReding, 0, accelerometerReding.size)
        }
        else if(p0.sensor.type == Sensor.TYPE_MAGNETIC_FIELD){
            System.arraycopy(p0.values, 0, magetometerReding, 0, magetometerReding.size)
        }

        // 2-2. 수집한 값으로 현재 기기가 향하고 있는 방향을 계산합니다
        updateCompassAngles()

        // 2-3. 방향을 화면에 표시(이미지 회전)
        vbinding.compassImageView.rotation = angle.toFloat() * -1

    }

    private fun updateCompassAngles() {
        // 기기의 방향을 계산
        SensorManager.getRotationMatrix(rotationMatrix, null, accelerometerReding, magetometerReding)

        val orientation = SensorManager.getOrientation(rotationMatrix, orientationAngles)

        val degrees = (Math.toDegrees(orientation.get(0).toDouble()) + 360.0) % 360.0

        angle = round(degrees*100)/100
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

    }
}