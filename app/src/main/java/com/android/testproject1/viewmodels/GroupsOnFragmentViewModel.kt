package com.android.testproject1.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.android.testproject1.Repository
import com.android.testproject1.model.Post
import com.android.testproject1.model.Users
import com.android.testproject1.room.enteties.UsersChatListEntity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class GroupsOnFragmentViewModel(application: Application) :AndroidViewModel(application) {

    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val myTag="MyTag"

    private var lastResult: DocumentSnapshot? = null
    private val userList: MutableLiveData<MutableList<UsersChatListEntity>> = MutableLiveData()
    private val list2 = mutableListOf<UsersChatListEntity>()
    private var checkduplicate:String?=null

    @JvmName("getUserList")
    fun getUserList(): MutableLiveData<MutableList<UsersChatListEntity>> {
        return userList
    }


    fun loadGroupsOnPost(postId:String){

        val query: Query = if (lastResult == null) {
            db.collection("Offers").document(postId).collection("Groups")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(2)
        } else {
            db.collection("Offers").document(postId).collection("Groups")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .startAfter(lastResult?.toObject(Users::class.java))
                .limit(2)
        }

        query.get()
            .addOnSuccessListener { queryDocumentSnapshots ->
                for (documentSnapshot in queryDocumentSnapshots) {
                    val users: UsersChatListEntity = documentSnapshot.toObject(UsersChatListEntity::class.java)

                    list2.add(users)
                    userList.postValue(list2)

//                    val objects2: List<UsersChatListEntity> = it.result?.toObjects(UsersChatListEntity::class.java) as List<UsersChatListEntity>
//

//                    Log.d(myTag, "query Snapshot  ${documentSnapshot.id}")
//                    db.collection("Users")
//                        .whereEqualTo("id",documentSnapshot.id)
//                        .get()
//                        .addOnCompleteListener {
//                            if (it.isSuccessful){
//                                val objects2: List<UsersChatListEntity> = it.result?.toObjects(UsersChatListEntity::class.java) as List<UsersChatListEntity>
//
//                                if (checkduplicate!=documentSnapshot.data.toString()) {
//                                    checkduplicate = documentSnapshot.data.toString()
//
//                                    list2.addAll(objects2)
//                                    Log.d(myTag, "List 2 is : $list2")
//                                    userList.postValue(list2)
//                                }
//
//                            }
//                        }

                }

//                Log.d(myTag,"query Snapshot size "+queryDocumentSnapshots.size())
                Log.d(myTag, "query Snapshot  $lastResult")
                if (queryDocumentSnapshots.size() > 0) {
                    lastResult = queryDocumentSnapshots.documents[queryDocumentSnapshots.size() - 1]
                }

            }.addOnFailureListener {
                Log.d(myTag,"Exception is : "+it.localizedMessage)
            }



//        db.collection("Posts").document(postId).collection("Groups")
//            .addSnapshotListener { snapshots, e ->
//                if (e != null) {
////                    Log.d(myTAG, "listen:error", e)
//                    return@addSnapshotListener
//                }
//                for (dc in snapshots!!.documentChanges) {
//                    when (dc.type) {
//                        DocumentChange.Type.ADDED ->{
//                            db.collection("Users")
//                                .whereEqualTo("id", dc.document.id)
//                                .get()
//                                .addOnCompleteListener {
//                                    if (it.isSuccessful){
//                                        val objects2: List<Users> = it.result?.toObjects(Users::class.java) as List<Users>
//                                        list2.addAll(objects2)
//                                        userList.postValue(list2)
//
//                                    }
//                                }
//
//                        }
//                        DocumentChange.Type.MODIFIED ->{
//
//                        }
//                        DocumentChange.Type.REMOVED ->{
////                            Log.d(myTAG, "Removed city: ${dc.document.data}")
//                        }
//                    }
//                }
//            }


    }
}