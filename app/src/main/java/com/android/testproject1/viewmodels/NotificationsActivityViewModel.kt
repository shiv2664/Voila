package com.android.testproject1.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.android.testproject1.model.Notifications
import com.android.testproject1.model.Post
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import es.dmoral.toasty.Toasty

class NotificationsActivityViewModel(application: Application) :AndroidViewModel(application) {

    private val notificationList: MutableLiveData<MutableList<Notifications>> = MutableLiveData()

    private val list2 = mutableListOf<Notifications>()
    private val firebaseAuth=FirebaseAuth.getInstance()
    private val firebaseFirestore=FirebaseFirestore.getInstance()
    private val myTag="MyTag"

    private val currentUserID: String? =firebaseAuth.currentUser?.uid


    @JvmName("getNotificationList")
    fun getNotificationList(): MutableLiveData<MutableList<Notifications>> {
        return notificationList
    }


    fun getNotifications() {
        if (currentUserID != null) {

            firebaseFirestore.collection("Users").document(currentUserID).collection("Info_Notifications")
                .orderBy("timestamp",Query.Direction.DESCENDING)
                .addSnapshotListener { snapshots, e ->
                    if (e != null) {
                        Log.d(myTag, "listen:error", e)
                        return@addSnapshotListener
                    }

                    if (snapshots != null) {

                            for (dc in snapshots.documentChanges) {
                                when (dc.type) {

                                    DocumentChange.Type.ADDED -> {
                                        dc.document.toObject(Notifications::class.java).let {
                                            Log.d(myTag, " It is : $it")
                                            list2.addAll(listOf(it))
                                            notificationList.postValue(list2)
                                        }

                                    }
                                    DocumentChange.Type.MODIFIED -> {

                                    }
                                    DocumentChange.Type.REMOVED -> {
                                        dc.document.toObject(Notifications::class.java).let {
                                            list2.remove(it)
                                            notificationList.postValue(list2)
                                        }
                                    }
                                }
                            }
                    }

                    Log.d(myTag,"List 2 value is : "+list2)

                }

                }

        }


    fun deleteAll() {
//        refreshLayout!!.isRefreshing = true
        if (currentUserID != null) {
            FirebaseFirestore.getInstance().collection("Users")
                .document(currentUserID)
                .collection("Info_Notifications")
                .get()
                .addOnSuccessListener { queryDocumentSnapshots ->
                    if (!queryDocumentSnapshots.isEmpty) {
                        for (documentChange in queryDocumentSnapshots.documentChanges) {
//                            refreshLayout.isRefreshing = true
                            FirebaseFirestore.getInstance().collection("Users")
                                .document(currentUserID)
                                .collection("Info_Notifications")
                                .document(documentChange.document.id)
                                .delete()
                                .addOnSuccessListener {
                                    Toasty.success(getApplication(), "Notifications cleared", Toasty.LENGTH_SHORT, true).show()
//                                    getNotifications()
                                }
                                .addOnFailureListener { e -> e.printStackTrace() }
                        }


                    //                        notificationList?.clear()
//                        findViewById<View>(R.id.default_item).visibility = View.VISIBLE

                    } else {
//                        findViewById<View>(R.id.default_item).visibility = View.VISIBLE
//                        refreshLayout.isRefreshing = false
                    }

                }
                .addOnFailureListener { e ->
//                    refreshLayout.isRefreshing = false
                    e.printStackTrace()
                }
        }
    }

}

//        findViewById<View>(R.id.default_item).visibility = View.GONE
//        notificationsList?.clear()
//        refreshLayout!!.isRefreshing = true


//            FirebaseFirestore.getInstance().collection("Users")
//                .document(currentUserID)
//                .collection("Info_Notifications")
//                .orderBy("timestamp", Query.Direction.DESCENDING)
//                .get()
//                .addOnSuccessListener { queryDocumentSnapshots: QuerySnapshot ->
//                    //                getSharedPreferences("Notifications", AppCompatActivity.MODE_PRIVATE).edit()
//                    //                    .putInt("count", queryDocumentSnapshots.size()).apply()
//
//                    if (!queryDocumentSnapshots.isEmpty) {
//                        for (documentChange in queryDocumentSnapshots.documentChanges) {
//                            if (documentChange.type == DocumentChange.Type.ADDED) {
//                                //                            refreshLayout.isRefreshing = false
//
//                                val notification: Notifications = documentChange.document.toObject(Notifications::class.java)
//                                //                                .withId(documentChange.document.id)
//                                list2.add(notification)
//                                notificationList.postValue(list2)
//                                //                            notificationsAdapter.notifyDataSetChanged()
//                            }
//                        }
////                        if (notificationList.size == 0) {
//////                            refreshLayout.isRefreshing = false
//////                            findViewById<View>(R.id.default_item).visibility = View.VISIBLE
////                        }
//                    } else {
////                        refreshLayout.isRefreshing = false
////                        findViewById<View>(R.id.default_item).visibility = View.VISIBLE
//                    }
//                }
//                .addOnFailureListener { e: Exception ->
////                    refreshLayout.isRefreshing = false
//                    e.printStackTrace()
//                }


