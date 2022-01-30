package com.ardmos.login_with_firebase

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.ardmos.login_with_firebase.LoginActivity.Companion.TAG
import com.ardmos.login_with_firebase.databinding.ActivityLoginBinding
import com.ardmos.login_with_firebase.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    val vbinding by lazy { ActivityMainBinding.inflate(layoutInflater)}
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(vbinding.root)

        // get a Firebase Auth instance
        auth = FirebaseAuth.getInstance()
        // get a Cloud Firestore instance
        db = FirebaseFirestore.getInstance()

        // check uid
        val user = auth.currentUser
        Log.d(TAG,"here is MainActivity and uid is ${user!!.uid}." )
        // get a Firestore data by uid
        db.collection("users").document(user.uid)  //.document("CK1CiHesYiafCjKT0HholQrCz6p1")
            .get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                } else {
                    Log.d(TAG, "No such document")
                }
               /* for(document in result){
                    Log.d(TAG, "${document.id} => ${document.data}")
                }*/
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
                Toast.makeText(baseContext, "Authentication failed. ${exception.message}", Toast.LENGTH_LONG).show()
            }


        //Log.d(TAG, "and here is ${user!!.uid}'s data: \n${user_data}")

    }
}