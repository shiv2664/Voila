package com.android.testproject1.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.android.testproject1.Repository
import com.android.testproject1.model.Users

class DetailsFragmentViewModel(application: Application) : AndroidViewModel(application) {

    private val authAppRepository: Repository = Repository(application)
    private val userList: MutableLiveData<MutableList<Users>> = authAppRepository.getUserList()

    fun loadGroupsOnPost(postId:String){
        authAppRepository.loadGroupsOnPost(postId)
    }
    @JvmName("getUserList")
    fun getUserList(): MutableLiveData<MutableList<Users>> {
        return userList
    }
}