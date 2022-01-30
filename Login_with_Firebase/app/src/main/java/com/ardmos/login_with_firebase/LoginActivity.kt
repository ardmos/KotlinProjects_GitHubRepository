package com.ardmos.login_with_firebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.ardmos.login_with_firebase.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

// Login with Firebase by Email & Password
// 가입과 로그인이 되는것까지 확인했다. 액티비티들 추가할 차례

class LoginActivity : AppCompatActivity() {

    private val TAG: String = "My Log"
    private lateinit var auth: FirebaseAuth
    val vbinding by lazy { ActivityLoginBinding.inflate(layoutInflater)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(vbinding.root)

        // Initialize Firebase Auth
        auth = Firebase.auth

        // Set OnListener on buttons
        // button_login
        //with(vbinding){
        vbinding.buttonLogin.setOnClickListener {
                auth.signInWithEmailAndPassword(vbinding.inputEmailId.text.toString(), vbinding.inputPassword.text.toString()).addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success, user: ${auth.currentUser}")
                            val user = auth.currentUser
                            //updateUI(user)
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.exception)
                            Toast.makeText(baseContext, "Authentication failed.",
                                Toast.LENGTH_SHORT).show()
                            //updateUI(null)
                        }
                }
            }
            // button_join
        vbinding.buttonJoin.setOnClickListener {
            //intent
            val intent = Intent(this, JoinActivity::class.java)
            intent.putExtra("TAG", TAG)
            //startActivity
            startActivity(intent)
        }
        //}

    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        Log.d(TAG, currentUser.toString())
        if(currentUser != null){
            //reload();
        }
    }


}