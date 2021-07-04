package com.android.testproject1.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.android.testproject1.Repository
import com.android.testproject1.model.Chat

class GroupChatFragmentViewModel(application: Application) : AndroidViewModel(application) {

    private val authAppRepository: Repository = Repository(application)
    private val groupChatList: MutableLiveData<MutableList<Chat>> = authAppRepository.getGroupChatList()


    fun loadGroupChat(postId: String,groupId:String){
        authAppRepository.loadGroupChat(postId,groupId)
    }

    @JvmName("getGroupChatList")
    fun getGroupChatList(): MutableLiveData<MutableList<Chat>> {
        return groupChatList
    }
}