package com.android.testproject1.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.android.testproject1.Repository
import com.android.testproject1.room.enteties.ChatRoomEntity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class GroupChatFragmentViewModel(application: Application) : AndroidViewModel(application) {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val authAppRepository: Repository = Repository(application)
    private val groupChatList: MutableLiveData<MutableList<ChatRoomEntity>> = MutableLiveData()
    private val myTAG="MyTag"

    @JvmName("getGroupChatList")
    fun getGroupChatList(): MutableLiveData<MutableList<ChatRoomEntity>> {
        return groupChatList
    }

    fun loadGroupChat(postId: String,groupId:String){

        val list2 = mutableListOf<ChatRoomEntity>()

        db.collection("Posts").document(postId).collection("Groups")
            .document(groupId).collection("Chats")
            .orderBy("timestamp", Query.Direction.ASCENDING)
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
                            }
                            groupChatList.postValue(list2)
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