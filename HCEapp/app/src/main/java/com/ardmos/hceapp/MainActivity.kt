package com.ardmos.hceapp

import android.content.Intent
import android.nfc.cardemulation.HostApduService
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.ardmos.hceapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var vbinding : ActivityMainBinding
    lateinit var cardService: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vbinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(vbinding.root)

        // cardService = Intent(this, CardService.class)
    }

    public fun onClickUpdate(_view: View){
        //vbinding.textView01.setText("Count : ${Counter.GetCurrentCount()}")
    }
}