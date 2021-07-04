package com.android.testproject1.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.android.testproject1.Repository
import com.android.testproject1.model.Chat
import com.android.testproject1.model.Post

class ChatFragmentViewModel(application: Application) : AndroidViewModel(application) {

    private val authAppRepository: Repository = Repository(application)
    private val chatList: MutableLiveData<MutableList<Chat>> = authAppRepository.getChatList()


    fun loadChat(chatKey:String){
        authAppRepository.loadChat(chatKey)
    }

    @JvmName("getChatList")
    fun getChatList(): MutableLiveData<MutableList<Chat>> {
        return chatList
    }
}