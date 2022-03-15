package com.haeddoo.level

import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Color
import android.graphics.Color.red
import android.graphics.ColorFilter
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.widget.Toast
import com.google.ads.mediation.admob.AdMobAdapter
import com.google.android.gms.ads.*
import com.haeddoo.level.databinding.ActivityMainBinding
import java.util.*
import kotlin.math.abs

/** Main Activity. Inflates main activity xml and child fragments.  */
class MainActivity : AppCompatActivity(), SensorEventListener {

    val vbinding by lazy { ActivityMainBinding.inflate(layoutInflater) }


    private lateinit var sensor_manager: SensorManager
    private var sensor: Sensor? = null

    val sensor_center_precision = 10f

    //Google AdMob
    lateinit var ad_view: AdView
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

        sensor_manager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        //sensor = sensor_manager.getDefaultSensor(Sensor.TYPE_GAME_ROTATION_VECTOR)
        //sensor = sensor_manager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)
        sensor = sensor_manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)


        //Google AdMob
        initBannerAds()

    }

    /** Called when leaving the activity  */
    public override fun onPause() {
        //Sensor
        sensor_manager.unregisterListener(this)
        //Google AdMob
        ad_view.pause()
        super.onPause()

    }

    /** Called when returning to the activity  */
    public override fun onResume() {
        super.onResume()
        //Sensor
        sensor?.also { accel ->
            sensor_manager.registerListener(this, accel, SensorManager.SENSOR_DELAY_NORMAL)
        }
        //Google AdMob
        ad_view.resume()
    }

    /** Called before the activity is destroyed  */
    public override fun onDestroy() {
        //Google AdMob
        ad_view.destroy()
        super.onDestroy()
    }

    override fun onSensorChanged(p0: SensorEvent?) {
        if (p0 == null) return

        //vbinding.star.setPadding(0, p0.values[1].toInt()*10, p0.values[0].toInt()*10, 0 )
        //Main
        ObjectAnimator.ofFloat(vbinding.orangeMain, "translationX", p0.values[0] * 50f * -1).apply {
            //duration = 2000
            start()
        }
        ObjectAnimator.ofFloat(vbinding.orangeMain, "translationY", p0.values[1] * 50f).apply {
            //duration = 2000
            start()
        }

        //Left
        ObjectAnimator.ofFloat(vbinding.orangeLeft, "translationY", p0.values[1] * 50f).apply {
            //duration = 2000
            start()
        }
        //Bottom
        ObjectAnimator.ofFloat(vbinding.orangeBottom, "translationX", p0.values[0] * 50f * -1)
            .apply {
                //duration = 2000
                start()
            }

        //이동이 뻑뻑함
        //vbinding.orangeBottom.translationX = p0.values[0]*50f*-1

        //Log.d("myLog", "x:${abs(p0.values[0])}, y:${abs(p0.values[1])}, sensor: ${sensor_center_precision}")
        //Log.d(
        //   "myLog",
        //    "x:${vbinding.orangeBottom.translationX}, y:${vbinding.orangeLeft.translationY}, sensor: ${sensor_center_precision}"
        //)

        //중앙 체크
        val abs_orangeLeftY = abs(vbinding.orangeLeft.translationY)
        val abs_orangeBottomX = abs(vbinding.orangeBottom.translationX)

        //Main
        if (abs_orangeBottomX <= sensor_center_precision && abs_orangeLeftY <= sensor_center_precision) {
            vbinding.orangeMain.setColorFilter(resources.getColor(R.color.red))
            //Toast.makeText(this,"와! 중앙!", Toast.LENGTH_LONG).show()
        } else vbinding.orangeMain.colorFilter = null
        //Left
        if (abs_orangeLeftY <= sensor_center_precision) {
            vbinding.orangeLeft.setColorFilter(resources.getColor(R.color.red))
        } else vbinding.orangeLeft.colorFilter = null
        //Bottom
        if (abs_orangeBottomX <= sensor_center_precision) {
            vbinding.orangeBottom.setColorFilter(resources.getColor(R.color.red))
        } else vbinding.orangeBottom.colorFilter = null
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

    }

    //Google Admob
    private fun initBannerAds() {
        // Initialize the Mobile Ads SDK with an AdMob App ID.
        MobileAds.initialize(this) {}

        // Set your test devices. Check your logcat output for the hashed device ID to
        // get test ads on a physical device. e.g.
        // "Use RequestConfiguration.Builder().setTestDeviceIds(Arrays.asList("ABCDEF012345"))
        // to get test ads on this device."
        MobileAds.setRequestConfiguration(
            RequestConfiguration.Builder()
                .setMaxAdContentRating("G")
                .setTagForChildDirectedTreatment(RequestConfiguration.TAG_FOR_CHILD_DIRECTED_TREATMENT_TRUE)
                //.setTestDeviceIds(Arrays.asList("CED7603B83B3F277F3B7BABF77EE2275")) //for test
                .build()
        )

        ad_view = AdView(this)
        vbinding.adViewContainer.addView(ad_view)
        // Since we're loading the banner based on the adContainerView size, we need to wait until this
        // view is laid out before we can get the width.
        vbinding.adViewContainer.viewTreeObserver.addOnGlobalLayoutListener {
            if (!initialLayoutComplete) {
                initialLayoutComplete = true
                loadBanner()
            }
        }
    }

    private fun loadBanner() {
        ad_view.adUnitId = AD_UNIT_ID

        ad_view.adSize = adSize

        // Create an ad request.
        val adRequest = AdRequest.Builder().build()


        ad_view.adListener = object : AdListener() {
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
        ad_view.loadAd(adRequest)
    }

    companion object {
        // This is an ad unit ID for a test ad. Replace with your own banner ad unit ID.
        private val AD_UNIT_ID = "ca-app-pub-6561678831605592/3009074662"
    }

}