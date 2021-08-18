package com.android.testproject1.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.android.testproject1.model.Post
import com.android.testproject1.model.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class SearchPeopleViewModel(application: Application) :AndroidViewModel(application) {

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val userList: MutableLiveData<MutableList<Users>> = MutableLiveData()
    private val myTag ="MyTag"
    private val list2 = mutableListOf<Users>()
    private var lastResult: DocumentSnapshot? = null

    @JvmName("getUserList")
    fun getUserList(): MutableLiveData<MutableList<Users>> {
        return userList
    }

    fun setNull(){
        lastResult=null
    }

    fun loadUsers(userName:String) {

        list2.clear()

        val query: Query = if (lastResult == null) {
            db.collection("Users").whereEqualTo("name",userName)
//                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(5)
        } else {
            db.collection("Users").whereEqualTo("name",userName)
//                .orderBy("timestamp", Query.Direction.DESCENDING)
                .startAfter(lastResult)
//                .startAfter(lastResult?.toObject(Users::class.java)?.id)
                .limit(5)
        }


//        Log.d(myTAG, " lastResult is  : ${lastResult?.toObject(Post::class.java)?.postId}")

        query.get()
            .addOnSuccessListener { queryDocumentSnapshots ->
                for (documentSnapshot in queryDocumentSnapshots) {
                    val users: Users = documentSnapshot.toObject(Users::class.java)
                    list2.addAll(listOf(users))
//                    Log.d(myTAG,"post Ids are "+post.postId)
                    userList.postValue(list2)
                }

//                Log.d(myTAG,"query Snapshot size "+queryDocumentSnapshots.size())
                if (queryDocumentSnapshots.size() > 0) {
                    lastResult = queryDocumentSnapshots.documents[queryDocumentSnapshots.size() - 1]
                }

            }.addOnFailureListener {
                Log.d(myTag,"Exception is : "+it.localizedMessage)
            }
    }


}