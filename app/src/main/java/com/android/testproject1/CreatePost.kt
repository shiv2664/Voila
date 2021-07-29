package com.android.testproject1

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.android.testproject1.adapter.PagerPhotosAdapter
import com.android.testproject1.services.UploadServicePosts
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.nguyenhoanglam.imagepicker.model.Image
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_create_post.*

class CreatePost : AppCompatActivity() {

    private var imagesList1: ArrayList<Image> = java.util.ArrayList()
    private var uploadedImagesUrl = ArrayList<String>()

    private lateinit var sharedPreferences: SharedPreferences
    private var serviceCount = 0
    val myTag="MyTag"
    private var postID: String? = null
    var currentId: String? = null
    private lateinit var firestore: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_post)

        setSupportActionBar(toolbarPost)

        imagesList1 = intent.getParcelableArrayListExtra<Image>("imagesList") as ArrayList<Image>

        if (imagesList1.isEmpty()) {
            finish()
        }

        firebaseAuth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        currentId = firebaseAuth.currentUser?.uid
        Log.d(myTag, "image list size is" + imagesList1.size.toString())

        postID = currentId + System.currentTimeMillis().toString()
        sharedPreferences = getSharedPreferences("uploadservice", Context.MODE_PRIVATE)
        serviceCount = sharedPreferences.getInt("count", 0)



        val indicator = indicator
        val indicatorHolder = indicator_holder
        indicator.dotsClickable = true
        val adapter = PagerPhotosAdapter(this, imagesList1)
        pager.adapter = adapter

        if (imagesList1.size > 1) {
            indicatorHolder.visibility = View.VISIBLE
            indicator.setViewPager(pager)
        } else {
            indicatorHolder.visibility = View.GONE
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_post,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId==R.id.Post){


            if (descriptionTextMain.text.isEmpty()) {
//                AnimationUtil.shakeView(mEditText, activity)

            } else {

                sharedPreferences.edit().putInt("count", ++serviceCount).apply()
                Log.d(myTag, "On click  sp $serviceCount")

                val intent = Intent(this, UploadServicePosts::class.java)

                intent.putExtra("count", serviceCount)

                intent.putStringArrayListExtra("uploadedImagesUrl", uploadedImagesUrl)

                intent.putParcelableArrayListExtra("imagesList", imagesList1 as java.util.ArrayList<out Parcelable?>?)

                intent.putExtra("notification_id", System.currentTimeMillis().toInt())

                intent.putExtra("current_id", currentId)

                intent.putExtra("postID", postID)

                intent.putExtra("description",descriptionTextMain.text.toString().trim())

                intent.action = UploadServicePosts.ACTION_START_FOREGROUND_SERVICE_UPLOAD_POST

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startForegroundService(intent)

                    Log.d(myTag, "Build Version OP")
//                startForegroundService(activity!!,intent)
                } else {

                    Log.d(myTag, "Build Version NP")
//                activity!!.startService(intent)
                    startService(intent)
                }
                Toasty.info(this, "Uploading images..", Toasty.LENGTH_SHORT, true).show()
                finish()
            }
        }

        return super.onOptionsItemSelected(item)
    }
}