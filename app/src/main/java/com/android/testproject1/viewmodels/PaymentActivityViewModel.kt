package com.android.testproject1.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.android.testproject1.Repository
import com.android.testproject1.room.enteties.UsersChatListEntity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class PaymentActivityViewModel(application: Application) :AndroidViewModel(application) {

    private val authAppRepository: Repository = Repository(application)
    private val groupList: MutableLiveData<MutableList<UsersChatListEntity>> = MutableLiveData()

    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val myTAG="MyTag"
    private var checkduplicate:String?=null
    private var checkduplicate2:String?=null
    private val currentUserID= firebaseAuth.currentUser?.uid


    fun loadGroupMembers(userUid:String,postId: String){

        val docRef = db.collection("Offers").document(postId).collection("Groups").document(userUid)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {

                    val group = document["Members"] as List<String>?
                    Log.d(myTAG, "Document data is  $group")

                    if (checkduplicate!=document.data.toString()) {
                        checkduplicate=document.data.toString()

//                        Log.d(myTAG,"check duplicate is "+ checkduplicate!!)
//                        Log.d(myTAG, "DocumentSnapshot data is : ${document.data}")

//                        val yourArray: List<String> = document.data.toString().split("{")
//                        val split1 = yourArray[1]
//                        val split2 = split1.split("}")
//                        val split3: String = split2[0]
//                        val lastValues = split3.split(",").map { it -> it.trim() }
//                        Log.d(myTAG, "lastvalues ${lastValues.size}")
//                        Log.d(myTAG, "lastvalues $lastValues")
//                        lastValues.forEach {
//                            val equalSplit = it.split("=")
//                            arr.add(equalSplit[0])
//                        }

                        val list2 = mutableListOf<UsersChatListEntity>()
                        if (group != null) {
                            for (id in group) {
                                id.trim()
                                db.collection("Users")
                                    .whereEqualTo("id", id)
                                    .get()
                                    .addOnCompleteListener {
                                        if (it.isSuccessful) {
                                            val objects2: List<UsersChatListEntity> = it.result?.toObjects(UsersChatListEntity::class.java) as List<UsersChatListEntity>
                                            list2.addAll(objects2)
                                            Log.d(myTAG, "Removed city: ${list2}")
                                            //                                    userList.value = list2
                                            groupList.postValue(list2)

                                        }
                                    }
                            }
                        }

                    }

                } else {
                    Log.d(myTAG, "No such document")
                }
            }
            .addOnFailureListener { exception ->
                Log.d(myTAG, "get failed with ", exception)
            }

    }

    @JvmName("getGroupList")
    fun getGroupList(): MutableLiveData<MutableList<UsersChatListEntity>> {
        return groupList
    }

}
