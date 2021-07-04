package com.android.testproject1.viewmodels

import android.app.Application
import androidx.annotation.NonNull
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.android.testproject1.Repository
import com.google.firebase.auth.FirebaseUser


class MainActivityViewModel(@NonNull application: Application?) : AndroidViewModel(application!!) {
    private val authAppRepository: Repository = application?.let { Repository(it) }!!

}