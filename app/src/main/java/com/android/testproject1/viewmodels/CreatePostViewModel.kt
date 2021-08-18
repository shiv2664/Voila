package com.android.testproject1.viewmodels

import android.app.Application
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class CreatePostViewModel(application: Application) : AndroidViewModel(application) {

//    private val authAppRepository: Repository = Repository(application)
    private val postUploaded: MutableLiveData<Boolean> = MutableLiveData()
    private val mutableResults = MutableLiveData<Boolean?>()
    var postUploaded2:Boolean=false
    var checkUpload:Boolean?=null

    private var bitmap: Bitmap? = null
    private var resized: Bitmap? = null
    private var count: Int = 0

//    fun uploadImages(notification_id: Int, index: Int, imagesList: ArrayList<Image>, currentUser_id: String?,
//                     description: String?, uploadedImagesUrl: ArrayList<String>,postID:String,context: Context){
//
//        authAppRepository.uploadImages(notification_id,index, imagesList, currentUser_id,description, uploadedImagesUrl,postID,context)
//    }


    @JvmName("getPostUploaded")
    fun getPostUploaded(): MutableLiveData<Boolean> {
        return postUploaded
    }

    val results: LiveData<Boolean?>
        get() = mutableResults




}