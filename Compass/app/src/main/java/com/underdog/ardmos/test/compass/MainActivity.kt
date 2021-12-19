package com.underdog.ardmos.test.compass

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import com.underdog.ardmos.test.compass.databinding.ActivityMainBinding
import kotlin.math.round

//0. 센서 없는지 체크하는 과정 추가하기
//1. 상단 네비게이션바 없애기
//2. 이미지 만들기
//	- 내부(나침반)
//	- 아이콘
//	- 마켓 스크린샷

class MainActivity : AppCompatActivity(), SensorEventListener {

    val vbinding by lazy{ ActivityMainBinding.inflate(layoutInflater) }

    private lateinit var sensor_manager : SensorManager

    private val accelerometerReading = FloatArray(3)
    private val magnetometerReading = FloatArray(3)

    private val rotationMatrix = FloatArray(9)
    private val orientationAngles = FloatArray(3)

    var angle : Double = 0.0
    var direction : String = ""



    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        // If the Android version is lower than Jellybean, use this call to hide
        // the status bar.
        if (Build.VERSION.SDK_INT < 16) {
            //window.setFlags(
              //  WindowManager.LayoutParams.FLAG_FULLSCREEN,
                //WindowManager.LayoutParams.FLAG_FULLSCREEN)
        }else{
            // Hide the status bar.
            //window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            // Remember that you should never show the action bar if the
            // status bar is hidden, so hide that too if necessary.
            //actionBar?.hide()
            //actionBar?.hide()
            // This example uses decor view, but you can use any visible view.
            //activity?.window?.decorView?.apply {
            //    systemUiVisibility = View.SYSTEM_UI_FLAG_LOW_PROFILE
            //}
            //window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
            //MAINLAYOUT.setPadding(0, statusBarHeight(this), 0, 0)
            //actionBar?.hide()

        }

        setContentView(vbinding.root)

        // 1
        sensor_manager = getSystemService(SENSOR_SERVICE) as SensorManager


        //window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        //vbinding.mainActivityLayout.setPadding(0, this.resources.getDimensionPixelSize(this.resources.getIdentifier("status_bar_height", "dimen", "android")), 0, 0)


    }

    override fun onResume() {
        super.onResume()

        // Hide the status bar.
        //window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        //window.decorView.system
        //View 공부중
        // Remember that you should never show the action bar if the
        // status bar is hidden, so hide that too if necessary.
        //actionBar?.hide()


        // 2
        sensor_manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)?.also { accelerometer ->
            sensor_manager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_DELAY_UI)
        }
        // 3
        sensor_manager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)?.also { magneticField ->
            sensor_manager.registerListener(this, magneticField, SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onSensorChanged(p0: SensorEvent?) {
        // 1
        if (p0 == null) {
            return
        }
        // 2
        if (p0.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            // 3
            System.arraycopy(p0.values, 0, accelerometerReading, 0, accelerometerReading.size)
        } else if (p0.sensor.type == Sensor.TYPE_MAGNETIC_FIELD) {
            System.arraycopy(p0.values, 0, magnetometerReading, 0, magnetometerReading.size)
        }

        updateOrientationAngles()

        val angleWithDirection = "$angle  $direction"

        Log.d("myDebug", "angleWithDeirection : $angleWithDirection")
        // 2
        vbinding.compassImageView.rotation = angle.toFloat() * -1

    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

    }

    fun updateOrientationAngles() {
        // 1
        SensorManager.getRotationMatrix(rotationMatrix, null, accelerometerReading, magnetometerReading)
        // 2
        val orientation = SensorManager.getOrientation(rotationMatrix, orientationAngles)
        // 3
        val degrees = (Math.toDegrees(orientation.get(0).toDouble()) + 360.0) % 360.0
        // 4
        angle = round(degrees * 100) / 100

        direction = getDirection(degrees)
    }

    private fun getDirection(angle: Double): String {
        var direction = ""

        if (angle >= 350 || angle <= 10)
            direction = "N"
        if (angle < 350 && angle > 280)
            direction = "NW"
        if (angle <= 280 && angle > 260)
            direction = "W"
        if (angle <= 260 && angle > 190)
            direction = "SW"
        if (angle <= 190 && angle > 170)
            direction = "S"
        if (angle <= 170 && angle > 100)
            direction = "SE"
        if (angle <= 100 && angle > 80)
            direction = "E"
        if (angle <= 80 && angle > 10)
            direction = "NE"

        return direction
    }

    override fun onPause() {
        // Be sure to unregister the sensor when the activity pauses.
        super.onPause()
        sensor_manager.unregisterListener(this)
    }
}


