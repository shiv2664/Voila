package com.android.testproject1.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.android.testproject1.Repository
import com.android.testproject1.room.enteties.AppDatabase
import com.android.testproject1.room.enteties.ChatRoomEntity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ChatFragmentViewModel(application: Application) : AndroidViewModel(application) {

    private val authAppRepository: Repository = Repository(application)
    //    private val chatList: MutableLiveData<MutableList<Chat>> = authAppRepository.getChatList()
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    //    private val chatList: MutableLiveData<MutableList<ChatRoomEntity>> = MutableLiveData()
//    private val chatList = MutableLiveData<List<ChatRoomEntity>>()
    private val myTAG="MyTag"
    var counter =0

//    private lateinit var registration:ListenerRegistration

    private val localDatabase: AppDatabase = AppDatabase.getInstance(application)!!


//    fun loadChat(chatKey:String){
//        authAppRepository.loadChat(chatKey)
//    }

//    @JvmName("getChatList")
//    fun getChatList(): MutableLiveData<MutableList<ChatRoomEntity>> {
//        return chatList
//    }
val list2 = mutableListOf<ChatRoomEntity>()

    private var lastResult: DocumentSnapshot? = null

    @JvmName("getChatList")
    fun getChatList(chatKey: String): LiveData<List<ChatRoomEntity>>? {
        return localDatabase.appDao()?.getMessages(chatKey)
    }

    fun queryLoad(chatKey:String){
        if (counter==0){
            viewModelScope.launch (Dispatchers.IO){
                localDatabase.appDao()?.deleteAllChatMessages()
            }
        }

        counter++

        val query: Query= if (lastResult == null) {
            db.collection("Chats").document(chatKey).collection("UserChats")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(15)
        } else {
            db.collection("Chats").document(chatKey).collection("UserChats")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .startAfter(lastResult?.toObject(ChatRoomEntity::class.java)?.timestamp)
//                .startAfter(lastResult)
                .limit(15)
        }

        query.get().addOnSuccessListener {
            for (d in it){
                val chat: ChatRoomEntity = d.toObject(ChatRoomEntity::class.java)
//                list2.add(chat)
//                chatList.postValue(list2)
                viewModelScope.launch(Dispatchers.IO) {
                    localDatabase.appDao()?.insertMessage(chat)
                }
//                lastResult = d
//                Log.d(myTAG,"query 0 is : "+chat.message)
            }

            if (it.size() > 0) {
                lastResult = it.documents[it.size() - 1]
            }

        }

    }

    fun loadChat(chatKey:String) {

//        localDatabase.appDao()?.deleteAllMessages()

        db.collection("Chats").document(chatKey).collection("UserChats")
            .document("recentMessage").addSnapshotListener { value, error ->


//                if (value?.metadata?.hasPendingWrites()!=true){

                    val chat: ChatRoomEntity? = value?.toObject(ChatRoomEntity::class.java)
                    viewModelScope.launch(Dispatchers.IO) {
                        if (chat != null) {
                            localDatabase.appDao()?.insertMessage(chat)
                        }
                    }

                    Log.d(myTAG,"Last Result is : "+lastResult?.toObject(ChatRoomEntity::class.java)?.timestamp)



            }
    }



}


//                .limit(15)


//        registration = query.addSnapshotListener { snapshots, e ->
//            if (e != null) {
//                Log.w(myTAG, "listen:error", e)
//                return@addSnapshotListener
//            }
//
//            for (dc in snapshots!!.documentChanges) {
//                when (dc.type) {
//
//                    DocumentChange.Type.ADDED ->{
//                        dc.document.toObject(ChatRoomEntity::class.java).let {
//                            list2.add(it)
//
////                            viewModelScope.launch(Dispatchers.IO) {
////                                localDatabase.appDao()?.insertMessageList(listOf(it))
////                            }
//                            chatList.postValue(list2)
//                        }
//
//                    }
//                    DocumentChange.Type.MODIFIED ->{
//
//                    }
//                    DocumentChange.Type.REMOVED ->{
//                        Log.d(myTAG, "Removed city: ${dc.document.data}")
//                    }
//                }
//            }
//            if (snapshots.size() > 0) {
//                lastResult = snapshots.documents[snapshots.size() - 1]
//                Log.d(myTAG,"Last Result is : "+lastResult?.toObject(ChatRoomEntity::class.java)?.timestamp)
//            }
//        }