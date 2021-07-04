package com.android.testproject1.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.android.testproject1.Repository
import com.android.testproject1.model.Post
import com.google.firebase.auth.FirebaseUser
import java.util.ArrayList

class HomeFragmentViewModel(application: Application): AndroidViewModel(application) {

    private val authAppRepository: Repository = Repository(application)
    private val postList: MutableLiveData<MutableList<Post>> = authAppRepository.getPostList()
    private val userLiveData: MutableLiveData<FirebaseUser> = authAppRepository.getUserLiveData()


    @JvmName("getPostList")
    fun getPostList(): MutableLiveData<MutableList<Post>> {
        return postList
    }

    fun loadDataPost() {
            authAppRepository.loadDataPost()
    }
}