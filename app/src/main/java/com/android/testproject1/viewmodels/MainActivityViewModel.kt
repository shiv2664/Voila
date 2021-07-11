package com.android.testproject1.viewmodels

import android.app.Application
import androidx.annotation.NonNull
import androidx.lifecycle.AndroidViewModel
import com.android.testproject1.Repository


class MainActivityViewModel(application: Application) : AndroidViewModel(application) {


    private val authAppRepository: Repository = Repository(application)
}


//    private val mRepository=Repository.getInstance(application.applicationContext)
//
//    fun getInstance(){
//        Repository.getInstance(getApplication())
//    }


//    var mNotesList: List<NoteEntity>? = null
//    private var mRepository: Repository? = null

//    fun MainActivityViewModel(application: Application) {
//        mRepository = Repository.getInstance(application.applicationContext)
////        mNotesList = mRepository!!.mNotesList
//    }



