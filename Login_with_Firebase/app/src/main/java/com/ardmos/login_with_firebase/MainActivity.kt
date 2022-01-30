package com.ardmos.login_with_firebase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ardmos.login_with_firebase.databinding.ActivityLoginBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    val vbinding by lazy { ActivityLoginBinding.inflate(layoutInflater)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(vbinding.root)

        // Access a Cloud Firestore instance from your Activity
        val db = Firebase.firestore


    }
}