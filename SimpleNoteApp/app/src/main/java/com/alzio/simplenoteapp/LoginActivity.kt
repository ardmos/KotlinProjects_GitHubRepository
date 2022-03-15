package com.alzio.simplenoteapp

import android.app.Activity
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.alzio.simplenoteapp.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth
    private lateinit var db : FirebaseFirestore
    private val vbinding by lazy { ActivityLoginBinding.inflate(layoutInflater) }

    // 구글 로그인 관련 변수
    lateinit var googleSignInClient : GoogleSignInClient
    // startActivityForResult 대신 사용하는 ActivityResultLauncher
    lateinit var getResult: ActivityResultLauncher<Intent>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(vbinding.root)

        // Firebase Auth 초기화
        auth = Firebase.auth
        // Firebase Firestore 초기화
        db = Firebase.firestore

        // 1. 인증  (로그인)  -
        // 2. uid값 추출  -
        // 3. uid값을 단서로 데이터를 읽어온다.



        // 구글 요청 준비
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)


        getResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            if(it.resultCode == RESULT_OK){
                // 구글 로그인에 성공을 하면, 로그인 사용자의 정보를 저희 firebase auth에 전달을 합니다.
                val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)

                try {
                    val account = task.getResult(ApiException::class.java)!!
                    //Log.d("test", "구글 로그인에 성공했습니다")
                    Toast.makeText(this, "구글 로그인에 성공했습니다", Toast.LENGTH_LONG).show()
                    // account(로그인에 성공한 사용자의 정보)를 firebase auth에 넘겨줘서 로그인 성공 처리를 해준다.
                    // 로그인된 사용자의 정보를 뽑아낸다.
                    val credential = GoogleAuthProvider.getCredential(account.idToken!!, null)

                    // 뽑아낸 정보를 Firebase auth에 넘겨줘서 로그인 처리를 해준다.
                    auth.signInWithCredential(credential)
                        .addOnCompleteListener(this) {
                            if(it.isSuccessful){
                                // Firebase auth 로그인 성공
                                Toast.makeText(this, "Firebase auth 로그인 성공: ${auth.currentUser?.email.toString()}", Toast.LENGTH_LONG).show()
                            }
                            else{
                                // Firebase auth 로그인 실패
                                Toast.makeText(this, "Firebase auth 로그인 실패", Toast.LENGTH_LONG).show()
                            }
                        }


                } catch (e: ApiException){
                    //Log.d("test", "구글 로그인에 실패했습니다 ${e.message}")
                    Toast.makeText(this, "구글 로그인에 실패했습니다 ${e.message}", Toast.LENGTH_LONG).show()
                }


            }else{
                Toast.makeText(this, "구글 로그인 요청에 실패했습니다", Toast.LENGTH_LONG).show()
            }

        }

        // google sign in
        vbinding.buttonSigninGoogle.setOnClickListener {
            // 구글에 구글 로그인을 하고싶다는 요청을 보냅니다.
            //startActivityForResult(googleSignInClient.signInIntent, 1234)
            getResult.launch(googleSignInClient.signInIntent)
        }



        vbinding.buttonCreateAccount.setOnClickListener {
            // 1. intent 만들기
            val intent = Intent(this, JoinActivity::class.java)
            // 2. startAcivity()
            startActivity(intent)
        }


        vbinding.buttonSignInWithEmail.setOnClickListener {
            auth.signInWithEmailAndPassword(vbinding.editTextID.text.toString(),vbinding.editTextPassword.text.toString())
                .addOnCompleteListener(this) {
                    if (it.isSuccessful){
                        // 로그인 성공
                        Toast.makeText(this, "로그인에 성공했습니다! 계정명: ${auth.currentUser}", Toast.LENGTH_LONG ).show()

                        vbinding.textView.text = auth.currentUser!!.uid

                        db.collection("users").document(auth.currentUser!!.uid)
                            .get()
                            .addOnSuccessListener {
                                if (it != null){
                                    // 해당 uid의 문서를 찾은 경우.
                                    Toast.makeText(this, "${ it.data}", Toast.LENGTH_LONG ).show()
                                }
                                else{
                                    // 해당 uid값을 가진 문서가  존재하지 않는 경우
                                    Toast.makeText(this, "해당 uid값을 가진 문서가  존재하지 않는 경우", Toast.LENGTH_LONG).show()
                                }
                            }
                            .addOnFailureListener {
                                Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                            }

                        // 1. intent 만들기
                        val intent = Intent(this, MainActivity::class.java)
                        // 2. startAcivity()
                        startActivity(intent)


                    }
                    else{
                        // 로그인 실패
                        Toast.makeText(this, "로그인에 실패했습니다! ${it.exception.toString()}", Toast.LENGTH_LONG ).show()


                    }
                }
        }


        vbinding.buttonSignOut.setOnClickListener {
            Firebase.auth.signOut()
            vbinding.textView.text = auth.currentUser?.email
        }

    }

/*    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // 그다음 구글 로그인을 진행을 하고

        if(requestCode == 1234){
            if(resultCode == Activity.RESULT_OK){
                // 구글 로그인에 성공을 하면, 로그인 사용자의 정보를 저희 firebase auth에 전달을 합니다.
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)

                try {
                    val account = task.getResult(ApiException::class.java)!!
                    //Log.d("test", "구글 로그인에 성공했습니다")
                    Toast.makeText(this, "구글 로그인에 성공했습니다", Toast.LENGTH_LONG).show()
                    // account(로그인에 성공한 사용자의 정보)를 firebase auth에 넘겨줘서 로그인 성공 처리를 해준다.
                    // 로그인된 사용자의 정보를 뽑아낸다.
                    val credential = GoogleAuthProvider.getCredential(account.idToken!!, null)

                    // 뽑아낸 정보를 Firebase auth에 넘겨줘서 로그인 처리를 해준다.
                    auth.signInWithCredential(credential)
                        .addOnCompleteListener(this) {
                            if(it.isSuccessful){
                                // Firebase auth 로그인 성공
                                Toast.makeText(this, "Firebase auth 로그인 성공: ${auth.currentUser?.email.toString()}", Toast.LENGTH_LONG).show()
                            }
                            else{
                                // Firebase auth 로그인 실패
                                Toast.makeText(this, "Firebase auth 로그인 실패", Toast.LENGTH_LONG).show()
                            }
                        }


                } catch (e: ApiException){
                    //Log.d("test", "구글 로그인에 실패했습니다 ${e.message}")
                    Toast.makeText(this, "구글 로그인에 실패했습니다 ${e.message}", Toast.LENGTH_LONG).show()
                }


            }
        }

    }*/


}