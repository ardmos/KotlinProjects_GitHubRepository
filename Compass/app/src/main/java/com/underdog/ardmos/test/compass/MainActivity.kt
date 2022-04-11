package com.underdog.ardmos.test.compass

import android.app.DownloadManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.google.android.gms.ads.*
import com.kakao.adfit.ads.AdListener
import com.kakao.adfit.ads.ba.BannerAdView
import com.underdog.ardmos.test.compass.databinding.ActivityMainBinding
import java.util.*
import kotlin.math.round


class MainActivity : AppCompatActivity(), SensorEventListener {

    val vbinding by lazy{ ActivityMainBinding.inflate(layoutInflater) }

    private lateinit var sensor_manager : SensorManager

    private val accelerometerReading = FloatArray(3)
    private val magnetometerReading = FloatArray(3)

    private val rotationMatrix = FloatArray(9)
    private val orientationAngles = FloatArray(3)

    var angle : Double = 0.0
    var direction : String = ""


    //Google AdMob
    private lateinit var adView: AdView

    private var initialLayoutComplete = false
    // Determine the screen width (less decorations) to use for the ad width.
    // If the ad hasn't been laid out, default to the full screen width.
    private val adSize: AdSize
        get() {
            val display = windowManager.defaultDisplay
            val outMetrics = DisplayMetrics()
            display.getMetrics(outMetrics)

            val density = outMetrics.density

            var adWidthPixels = vbinding.adViewContainer.width.toFloat()
            if (adWidthPixels == 0f) {
                adWidthPixels = outMetrics.widthPixels.toFloat()
            }

            val adWidth = (adWidthPixels / density).toInt()
            return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(vbinding.root)

        // 1
        sensor_manager = getSystemService(SENSOR_SERVICE) as SensorManager

        //Google AdMob
        googleAdMob()

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


    private fun googleAdMob(){
        // Initialize the Mobile Ads SDK.
        MobileAds.initialize(this) { }

        // Set your test devices. Check your logcat output for the hashed device ID to
        // get test ads on a physical device. e.g.
        // "Use RequestConfiguration.Builder().setTestDeviceIds(Arrays.asList("ABCDEF012345"))
        // to get test ads on this device."
        MobileAds.setRequestConfiguration(
            RequestConfiguration.Builder()
                .setMaxAdContentRating("G")
                .setTagForChildDirectedTreatment(RequestConfiguration.TAG_FOR_CHILD_DIRECTED_TREATMENT_TRUE)
                //.setTestDeviceIds(Arrays.asList("CED7603B83B3F277F3B7BABF77EE2275"))    //For Test
                .build()
        )

        adView = AdView(this)
        vbinding.adViewContainer.addView(adView)
        // Since we're loading the banner based on the adContainerView size, we need to wait until this
        // view is laid out before we can get the width.
        vbinding.adViewContainer.viewTreeObserver.addOnGlobalLayoutListener {
            if (!initialLayoutComplete) {
                initialLayoutComplete = true
                loadBanner()
            }
        }
    }

    /** Called when leaving the activity  */
    public override fun onPause() {
        adView.pause()
        // Be sure to unregister the sensor when the activity pauses.
        sensor_manager.unregisterListener(this)
        super.onPause()

    }

    /** Called when returning to the activity  */
    public override fun onResume() {
        super.onResume()

        // 2
        sensor_manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)?.also { accelerometer ->
            sensor_manager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_DELAY_UI)
        }
        // 3
        sensor_manager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)?.also { magneticField ->
            sensor_manager.registerListener(this, magneticField, SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_DELAY_UI)
        }

        adView.resume()
    }

    /** Called before the activity is destroyed  */
    public override fun onDestroy() {
        adView.destroy()
        super.onDestroy()
    }

    private fun loadBanner() {
        adView.adUnitId = AD_UNIT_ID

        adView.adSize = adSize

        // Create an ad request.
        val adRequest = AdRequest.Builder().build()

        adView.adListener = object : com.google.android.gms.ads.AdListener() {
            override fun onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                Log.d("myLog", "Ad Loaded!")
                Toast.makeText(applicationContext, "Ad Loaded!", Toast.LENGTH_LONG)
            }

            override fun onAdFailedToLoad(adError: LoadAdError) {
                // Code to be executed when an ad request fails.
                Log.d("myLog", adError.toString())
            }

            override fun onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
                Log.d("myLog", "Ad Opened!")
            }

            override fun onAdClicked() {
                // Code to be executed when the user clicks on an ad.
                Log.d("myLog", "Ad Clicked!")
            }

            override fun onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
                Log.d("myLog", "Ad Closed!")
            }
        }

        // Start loading the ad in the background.
        adView.loadAd(adRequest)
    }

    companion object {
        // This is an ad unit ID for a test ad. Replace with your own banner ad unit ID.
        private val AD_UNIT_ID = "ca-app-pub-6561678831605592/9072947808"
    }
}


