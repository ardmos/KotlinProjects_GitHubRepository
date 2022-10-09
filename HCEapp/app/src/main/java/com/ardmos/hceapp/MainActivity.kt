package com.ardmos.hceapp

import android.content.ComponentName
import android.nfc.NfcAdapter
import android.nfc.cardemulation.CardEmulation
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ardmos.hceapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var vbinding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vbinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(vbinding.root)

        val myComponentName = ComponentName(this, MyHostApduService::class.java)
        val adapter = NfcAdapter.getDefaultAdapter(this)
        val cardEmulation = CardEmulation.getInstance(adapter)
        var dynamicAIDList = arrayListOf("F0010203040506", "F0394148148100") // cardEmulation.getAidsForService(myComponentName, "other") as ArrayList<String>  이것과 같다 //
        val aidStat = cardEmulation.registerAidsForService(myComponentName, "other", dynamicAIDList)
        //Log.d("printlog", "${dynamicAIDList[0]}")
    }


}