package com.android.testproject1.model

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.android.testproject1.Repository
import com.google.firebase.auth.FirebaseUser

class GroupFragmentViewModel(application: Application) : AndroidViewModel(application) {

    private val authAppRepository: Repository = Repository(application)
    private val groupList: MutableLiveData<MutableList<Users>> = authAppRepository.getGroupList()



    fun loadGroupMembers(UserUid:String,postId: String){

        Log.d("MyTag", " function called")
        authAppRepository.loadGroupMembers(UserUid,postId)

    }

    @JvmName("getGroupList")
    fun getGroupList(): MutableLiveData<MutableList<Users>> {
        return groupList
    }

}