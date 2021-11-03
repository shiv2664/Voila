package com.android.testproject1.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.android.testproject1.Repository
import com.android.testproject1.room.enteties.AppDatabase
import com.android.testproject1.room.enteties.ChatRoomEntity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GroupChatFragmentViewModel(application: Application) : AndroidViewModel(application) {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val authAppRepository: Repository = Repository(application)
    private val groupChatList: MutableLiveData<MutableList<ChatRoomEntity>> = MutableLiveData()
    private val myTAG="MyTag"
    private val localDatabase: AppDatabase = AppDatabase.getInstance(application)!!
    val list2 = mutableListOf<ChatRoomEntity>()

//    @JvmName("getGroupChatList")
//    fun getGroupChatList(): MutableLiveData<MutableList<ChatRoomEntity>> {
//        return groupChatList
//    }

    private var lastResult: DocumentSnapshot? = null

    @JvmName("getChatList")
    fun getGroupChatList(postId: String,groupId: String): LiveData<List<ChatRoomEntity>>? {
        return localDatabase.appDao()?.getGroupMessages(postId,groupId)
    }

    fun queryLoad(postId: String,groupId:String) {

        val query: Query = if (lastResult == null) {
            db.collection("Offers").document(postId).collection("Groups")
                .document(groupId).collection("Chats")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(15)
        } else {
            db.collection("Offers").document(postId).collection("Groups")
                .document(groupId).collection("Chats")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .startAfter(lastResult?.toObject(ChatRoomEntity::class.java)?.timestamp)
//                .startAfter(lastResult)
                .limit(15)
        }

        query.get().addOnSuccessListener {
            for (d in it){
                val chat: ChatRoomEntity = d.toObject(ChatRoomEntity::class.java)
                viewModelScope.launch(Dispatchers.IO) {
                    localDatabase.appDao()?.insertMessage(chat)
                }
                lastResult = d
                Log.d(myTAG,"query 0 is : "+chat.message)
            }

        }
    }

    fun loadChat(postId: String,groupId:String) {

//        localDatabase.appDao()?.deleteAllMessages()

        db.collection("Offers").document(postId).collection("Groups")
            .document(groupId).collection("Chats")
            .document("recentMessage").addSnapshotListener { value, error ->

                val chat: ChatRoomEntity? = value?.toObject(ChatRoomEntity::class.java)
                viewModelScope.launch(Dispatchers.IO) {
                    if (chat != null) {
                        localDatabase.appDao()?.insertMessage(chat)
                    }
                }

                Log.d(
                    myTAG,
                    "Last Result is : " + lastResult?.toObject(ChatRoomEntity::class.java)?.timestamp
                )

            }
    }


//    fun loadGroupChat(postId: String,groupId:String){
//
//
//        db.collection("Offers").document(postId).collection("Groups")
//            .document(groupId).collection("Chats")
//            .orderBy("timestamp", Query.Direction.ASCENDING)
//            .addSnapshotListener { snapshots, e ->
//                if (e != null) {
//                    Log.w(myTAG, "listen:error", e)
//                    return@addSnapshotListener
//                }
//
//                for (dc in snapshots!!.documentChanges) {
//                    when (dc.type) {
//
//                        DocumentChange.Type.ADDED ->{
//                            dc.document.toObject(ChatRoomEntity::class.java).let {
//                                list2.add(it)
//                            }
//                            groupChatList.postValue(list2)
//                        }
//                        DocumentChange.Type.MODIFIED ->{
//
//                        }
//                        DocumentChange.Type.REMOVED ->{
//                            Log.d(myTAG, "Removed city: ${dc.document.data}")
//                        }
//                    }
//                }
//            }
//
//
//    }

}