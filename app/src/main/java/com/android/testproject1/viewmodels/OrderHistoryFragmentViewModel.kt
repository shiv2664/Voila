package com.android.testproject1.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.android.testproject1.model.Notifications
import com.android.testproject1.model.Post
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class OrderHistoryFragmentViewModel(application: Application) :AndroidViewModel(application) {

    private val notificationList: MutableLiveData<MutableList<Notifications>> = MutableLiveData()
    private val myTAG: String = "MyTag"
    private val list2 = mutableListOf<Notifications>()
    private val firebaseAuth= FirebaseAuth.getInstance()
    private val firebaseFirestore= FirebaseFirestore.getInstance()
    private var lastResult: DocumentSnapshot? = null
    private val myTag="MyTag"

    private val currentUserID: String? =firebaseAuth.currentUser?.uid

    @JvmName("getNotificationList")
    fun getNotificationList(): MutableLiveData<MutableList<Notifications>> {
        return notificationList
    }

    fun loadOrders() {

        Log.d(myTAG, "last result is : " + lastResult?.toObject(Notifications::class.java)?.id)

        if (currentUserID != null) {

            val query: Query = if (lastResult == null) {
                firebaseFirestore.collection("Users").document(currentUserID).collection("Orders")
                    .orderBy("timestamp", Query.Direction.DESCENDING)
                    .limit(10)
            } else {
                firebaseFirestore.collection("Posts")
                    .orderBy("timestamp", Query.Direction.DESCENDING)
                    .startAfter(lastResult?.toObject(Notifications::class.java)?.timestamp)
                    .limit(10)
            }


//        Log.d(myTAG, " lastResult is  : ${lastResult?.toObject(Post::class.java)?.postId}")

            query.get()
                .addOnSuccessListener { queryDocumentSnapshots ->
                    for (documentSnapshot in queryDocumentSnapshots) {
                        val orders: Notifications = documentSnapshot.toObject(Notifications::class.java)
                        list2.addAll(listOf(orders))
                        Log.d(myTAG, "post Ids are " + orders.id)
                        notificationList.postValue(list2)
                    }

                    Log.d(myTAG, "query Snapshot size " + queryDocumentSnapshots.size())
                    if (queryDocumentSnapshots.size() > 0) {
                        lastResult =
                            queryDocumentSnapshots.documents[queryDocumentSnapshots.size() - 1]
                    }

                }.addOnFailureListener {
                    Log.d(myTAG, "Exception is : " + it.localizedMessage)
                }
        }
    }
}