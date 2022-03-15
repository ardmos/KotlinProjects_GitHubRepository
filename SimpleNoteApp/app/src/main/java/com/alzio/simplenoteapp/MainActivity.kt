package com.alzio.simplenoteapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.alzio.simplenoteapp.databinding.ActivityLoginBinding
import com.alzio.simplenoteapp.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    private val vbinding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(vbinding.root)

        var auth = FirebaseAuth.getInstance()
        var db = FirebaseFirestore.getInstance()

        // 회원 삭제
        vbinding.buttonDeleteMainpage.setOnClickListener {
            // 1. db 삭제
            db.collection("users").document(auth.currentUser!!.uid)
                .delete()
                .addOnSuccessListener {
                    Toast.makeText(this, "DB 삭제에 성공했습니다!", Toast.LENGTH_LONG ).show()

                    // 2. auth 계정 삭제
                    auth.currentUser!!.delete()
                        .addOnCompleteListener {
                            if(it.isSuccessful){
                                Toast.makeText(this, "계정 삭제에 성공했습니다!", Toast.LENGTH_LONG ).show()

                                onBackPressed()
                            }
                        }
                }
                .addOnFailureListener {
                    Toast.makeText(this, "DB 삭제에 실패했습니다! ${it.message}", Toast.LENGTH_LONG ).show()
                }



        }

    }
}