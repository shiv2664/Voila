package com.android.testproject1.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.android.testproject1.Repository
import com.android.testproject1.model.Chat
import com.android.testproject1.model.Users

class ViewPagerFragmentChatViewModel(application: Application) :AndroidViewModel(application) {

    private val authAppRepository: Repository = Repository(application)
    private val userChatList: MutableLiveData<MutableList<Users>> = authAppRepository.getUserChatsList()

    @JvmName("userChatsList")
    fun getUserChatsList(): MutableLiveData<MutableList<Users>> {
        return userChatList
    }

    fun loadUserChatList(){
        authAppRepository.loadUserChatList()
    }
}