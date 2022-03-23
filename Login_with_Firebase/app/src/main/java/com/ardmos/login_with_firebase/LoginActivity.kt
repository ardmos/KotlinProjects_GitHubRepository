package com.ardmos.login_with_firebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.ardmos.login_with_firebase.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

// Login with Firebase by Email & Password
// 가입과 로그인이 되는것까지 확인했다. 액티비티들 추가할 차례

class LoginActivity : AppCompatActivity() {

    companion object{
        val TAG: String = "My Log"
    }

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    val vbinding by lazy { ActivityLoginBinding.inflate(layoutInflater)}

    private lateinit var googleSignInClient: com.google.android.gms.auth.api.signin.GoogleSignInClient
    private lateinit var getResult: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(vbinding.root)

        // Initialize Firebase Auth
        auth = Firebase.auth
        // Access a Cloud Firestore instance from your Activity
        db = Firebase.firestore

        ///////////////////////////// 구글 로그인

        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        getResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            if(it.resultCode == RESULT_OK){
                val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
                try {
                    // Google Sign In was successful, authenticate with Firebase
                    val account = task.getResult(ApiException::class.java)!!
                    Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                    firebaseAuthWithGoogle(account.idToken!!)
                } catch (e: ApiException) {
                    // Google Sign In failed, update UI appropriately
                    Log.w(TAG, "Google sign in failed", e)
                }
            }else{
                Log.d(TAG, "why result fail")
            }
        }



        vbinding.buttonGoogleLogin.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            //startActivityForResult(signInIntent, 100)
            getResult.launch(signInIntent)
        }









        ////////////////////////////


        // Set OnListener on buttons
        // button_login
        //with(vbinding){
        vbinding.buttonLogin.setOnClickListener {
                auth.signInWithEmailAndPassword(vbinding.inputEmailId.text.toString(), vbinding.inputPassword.text.toString()).addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success, user: ${auth.currentUser!!.uid}")
                            val user = auth.currentUser
                            updateUI(user)
                            // move activity
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.exception)
                            Toast.makeText(baseContext, "Authentication failed. ${task.exception!!.message}", Toast.LENGTH_LONG).show()

                            updateUI(null)
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
        //val currentUser = auth.currentUser
        //Log.d(TAG, currentUser!!.uid)
        //if(currentUser != null){
            //reload();
        //}
    }

/*    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == 100) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
            }
        }
    }*/

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    updateUI(user)
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    updateUI(null)
                }
            }
    }
    //////////////여기 하는중. 안되길래.  SHA 인증 해봤다~  까지.

    private fun updateUI(userdata: FirebaseUser?){
        vbinding.textView.text = "email: " + userdata?.email.toString()
    }
}