package com.android.testproject1.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.android.testproject1.room.enteties.AppDatabase
import com.android.testproject1.room.enteties.ChatRoomEntity
import com.android.testproject1.room.enteties.NotificationsRoomEntity
import com.android.testproject1.room.enteties.OrdersRoomEntity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class OrderHistoryFragmentViewModel(application: Application) :AndroidViewModel(application) {

    private val notificationList: MutableLiveData<MutableList<NotificationsRoomEntity>> = MutableLiveData()
    private val myTAG: String = "MyTag"
    private val list2 = mutableListOf<NotificationsRoomEntity>()
    private val firebaseAuth= FirebaseAuth.getInstance()
    private val firebaseFirestore= FirebaseFirestore.getInstance()
    private var lastResult: DocumentSnapshot? = null
    private val localDatabase: AppDatabase = AppDatabase.getInstance(application)!!

    private val myTag="MyTag"

    private val currentUserID: String? =firebaseAuth.currentUser?.uid

    @JvmName("getNotificationList")
    fun getNotificationList(): LiveData<List<OrdersRoomEntity>>? {
        return localDatabase.appDao()?.getOrders()
    }

    fun loadOrders() {

        Log.d(myTAG, "last result is : " + lastResult?.toObject(NotificationsRoomEntity::class.java)?.id)

        if (currentUserID != null) {

            val query: Query = if (lastResult == null) {
                firebaseFirestore.collection("Users").document(currentUserID).collection("Orders")
                    .orderBy("timestamp", Query.Direction.DESCENDING)
                    .limit(10)
            } else {
                firebaseFirestore.collection("Users").document(currentUserID).collection("Orders")
                    .orderBy("timestamp", Query.Direction.DESCENDING)
                    .startAfter(lastResult?.toObject(NotificationsRoomEntity::class.java)?.timestamp)
                    .limit(10)
            }


//        Log.d(myTAG, " lastResult is  : ${lastResult?.toObject(Post::class.java)?.postId}")

            query.get()
                .addOnSuccessListener { queryDocumentSnapshots ->
                    for (documentSnapshot in queryDocumentSnapshots) {
                        val orders: OrdersRoomEntity = documentSnapshot.toObject(OrdersRoomEntity::class.java)

                        viewModelScope.launch(Dispatchers.IO){
                            localDatabase.appDao()?.insertOrder(orders)
                        }
//                        list2.addAll(listOf(orders))
//                        Log.d(myTAG, "post Ids are " + orders.id)
//                        notificationList.postValue(list2)
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

    fun loadRecentOrder() {

        if (currentUserID != null) {
            firebaseFirestore.collection("Users").document(currentUserID).collection("Orders")
                .document("recentOrder").addSnapshotListener { value, error ->

                    val order: OrdersRoomEntity? = value?.toObject(OrdersRoomEntity::class.java)
                    viewModelScope.launch(Dispatchers.IO) {
                        if (order != null) {
                            localDatabase.appDao()?.insertOrder(order)
                        }
                    }

                    Log.d(myTAG,"Last Result is : "+lastResult?.toObject(OrdersRoomEntity::class.java)?.timestamp)



                }
        }
    }
}