package com.android.testproject1

import android.content.Intent
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
    private lateinit var firebaseAuth:FirebaseAuth
//    private val firebaseAuth: FirebaseAuth?=null
    var myTag:String="MyTag"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        firebaseAuth=FirebaseAuth.getInstance()

        firebaseAuth.currentUser?.toString()?.let { Log.d(myTag, it) }

        Handler(Looper.getMainLooper()).postDelayed({
            run {

                if (firebaseAuth.currentUser !=null){
                    val intent=Intent(this,MainActivity2::class.java)
//                    Log.d(myTag,firebaseAuth.currentUser!!.toString())
                    startActivity(intent)
                    finish()
                }else{
                    val intent=Intent(this,RegisterActivity::class.java)
                    startActivity(intent)
                    finish()
                }

            }
        }, 2500); // Millisecond 1000 = 1 sec






    }

}