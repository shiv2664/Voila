package com.android.testproject1.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.android.testproject1.Repository
import com.android.testproject1.room.enteties.AppDatabase
import com.android.testproject1.room.enteties.UsersRoomEntity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ViewPagerFragmentChatViewModel(application: Application) :AndroidViewModel(application) {

    private val authAppRepository: Repository = Repository(application)
//    private val userChatList: MutableLiveData<MutableList<Users>> = authAppRepository.getUserChatsList()

    //    private val userChatList: MutableLiveData<MutableList<Users>> = MutableLiveData()
    private val localDatabase: AppDatabase = AppDatabase.getInstance(application)!!
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
//    private val userChatList: MutableLiveData<List<UsersRoomEntity>>()

    private val userChatList = MutableLiveData<List<UsersRoomEntity>>()

    private val myTAG:String="MyTag"
    private var checkduplicate2:String?=null
    private val currentUserID= firebaseAuth.currentUser?.uid



    @JvmName("userChatsList")
    fun getUserChatsList(): LiveData<MutableList<UsersRoomEntity>>? {
        return localDatabase.appDao()?.getUserChats()
    }

//    fun loadUserChatList(){
//        authAppRepository.loadUserChatList()
//    }

    fun loadUserChatList(){


        val docRef = currentUserID?.let { db.collection("ChatList").document(it) }
        docRef?.get()?.addOnSuccessListener { document ->
            if (document != null) {

                val group = document["ChatList"] as List<String>?
                Log.d(myTAG, "Document data is  $group")

                if (checkduplicate2!=document.data.toString()) {
                    checkduplicate2=document.data.toString()

                    val list2 = mutableListOf<UsersRoomEntity>()

                    if (group != null) {
                        for (id in group) {
                            id.trim()
                            db.collection("Users")
                                .whereEqualTo("id", id)
                                .get()
                                .addOnCompleteListener {
                                    if (it.isSuccessful) {
                                        val objects2: List<UsersRoomEntity> = it.result?.toObjects(UsersRoomEntity::class.java) as List<UsersRoomEntity>
                                        list2.addAll(objects2)
//                                        Log.d(myTAG, "list in chatList is : $list2")
                                        userChatList.postValue(list2)
//                                        userChatList.value = snapshot!!.documents.map {
//                                            it.toObject(PostRoomEntity::class.java)!!}

                                        viewModelScope.launch(Dispatchers.IO) {
                                            localDatabase.appDao()?.insertUsersChats(list2)
                                        }

                                    }
                                }
                        }
                    }

                }

            } else {
                Log.d(myTAG, "No such document")
            }
        }?.addOnFailureListener { exception ->
            Log.d(myTAG, "get failed with ", exception)
        }

    }
}