package com.android.testproject1

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.animation.Animation
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.internal.ViewUtils.dpToPx
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_splash.*


class SplashActivity : AppCompatActivity() {

//    private val firebaseAuth2: FirebaseAuth = FirebaseAuth.getInstance()
//    private val firebaseAuth: FirebaseAuth?=null
    var myTag:String="MyTag"

    private lateinit var sharedPrefDynamic: SharedPreferences
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        firebaseAuth=FirebaseAuth.getInstance()
        firebaseAuth.currentUser?.toString()?.let { Log.d(myTag, it) }

        firebaseAuth= FirebaseAuth.getInstance()
        val currentUserID=firebaseAuth.currentUser?.uid.toString()
        sharedPrefDynamic= getSharedPreferences(currentUserID, Context.MODE_PRIVATE)!!
        val profileImage= sharedPrefDynamic.getString("profileimage","")

        Handler(Looper.getMainLooper()).postDelayed({
            run {
                if (firebaseAuth.currentUser !=null){
                    val intent=Intent(this,MainActivity2::class.java)
                            startActivity(intent)
                            finish()
                }else{
                    val intent=Intent(this,RegisterActivity::class.java)
                    startActivity(intent)
                    finish()
                }


//                if (firebaseAuth.currentUser !=null){
//                    if (profileImage != null) {
//                        if (profileImage.isNotBlank()){
//                            val intent=Intent(this,MainActivity2::class.java)
//                            startActivity(intent)
//                            finish()
//                        }else{
//                            val intent=Intent(this,RegisterActivity::class.java)
//                            startActivity(intent)
//                            finish()
//                        }
//                    }
//
//                }else{
//                    val intent=Intent(this,RegisterActivity::class.java)
//                    startActivity(intent)
//                    finish()
//                }

            }
        }, 2500); // Millisecond 1000 = 1 sec






    }

}