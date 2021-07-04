package com.android.testproject1.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.android.testproject1.Repository
import com.android.testproject1.model.Chat
import com.android.testproject1.model.Post

class ViewPagerFragmentPostViewModel(application: Application) :AndroidViewModel(application) {

    private val authAppRepository: Repository = Repository(application)
    private val userPostList: MutableLiveData<MutableList<Post>> = authAppRepository.getUserPostList()


    fun userPosts(){
        authAppRepository.userPosts()
    }

    @JvmName("getUserPostList")
    fun getUserPostList(): MutableLiveData<MutableList<Post>> {
        return userPostList
    }

}