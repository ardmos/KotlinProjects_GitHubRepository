package com.alzio.simplenoteapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.alzio.simplenoteapp.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private lateinit var auth : FirebaseAuth
    private lateinit var db : FirebaseFirestore
    private val vbinding by lazy { ActivityLoginBinding.inflate(layoutInflater) }

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
}