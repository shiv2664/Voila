package com.android.testproject1.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.testproject1.Repository
import com.android.testproject1.room.enteties.AppDatabase
import com.android.testproject1.room.enteties.ChatRoomEntity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class ChatFragmentViewModel(application: Application) : AndroidViewModel(application) {

    private val authAppRepository: Repository = Repository(application)
    //    private val chatList: MutableLiveData<MutableList<Chat>> = authAppRepository.getChatList()
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val chatList: MutableLiveData<MutableList<ChatRoomEntity>> = MutableLiveData()
//    private val chatList = MutableLiveData<List<ChatRoomEntity>>()
    private val myTAG="MyTag"

    private val localDatabase: AppDatabase = AppDatabase.getInstance(application)!!


//    fun loadChat(chatKey:String){
//        authAppRepository.loadChat(chatKey)
//    }

    @JvmName("getChatList")
    fun getChatList(): MutableLiveData<MutableList<ChatRoomEntity>> {
        return chatList
//        localDatabase.appDao()?.getMessages(chatKey)
    }

    fun loadChat(chatKey:String) {
        val list2 = mutableListOf<ChatRoomEntity>()
//        localDatabase.appDao()?.deleteAllMessages()

//        Log.d(myTAG,"local messages "+localDatabase.appDao()?.getMessagesAll())

//        db.collection("Chats").document("UserChats").collection(chatKey)

        db.collection("Chats").document(chatKey).collection("UserChats")
            .orderBy("timestamp", Query.Direction.ASCENDING)
            .limit(5)
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    Log.w(myTAG, "listen:error", e)
                    return@addSnapshotListener
                }

                for (dc in snapshots!!.documentChanges) {
                    when (dc.type) {

                        DocumentChange.Type.ADDED ->{
                            dc.document.toObject(ChatRoomEntity::class.java).let {
                                list2.add(it)
//                                viewModelScope.launch(Dispatchers.IO) {
//                                    localDatabase.appDao()?.insertMessageList(listOf(it))
//                                }
                            }

                            chatList.postValue(list2)
                        }
                        DocumentChange.Type.MODIFIED ->{

                        }
                        DocumentChange.Type.REMOVED ->{
                            Log.d(myTAG, "Removed city: ${dc.document.data}")
                        }
                    }
                }
            }
    }

}