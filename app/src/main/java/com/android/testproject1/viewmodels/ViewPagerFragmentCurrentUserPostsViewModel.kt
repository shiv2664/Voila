package com.android.testproject1.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.android.testproject1.Repository
import com.android.testproject1.fragments.ProfileFragment
import com.android.testproject1.model.Offer
import com.android.testproject1.model.Post
import com.android.testproject1.room.enteties.AppDatabase
import com.android.testproject1.room.enteties.OfferRoomEntity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ViewPagerFragmentCurrentUserPostsViewModel(application: Application) :AndroidViewModel(
    application) {

    private val authAppRepository: Repository = Repository(application)
    //    private val userPostList: MutableLiveData<MutableList<Post>> = authAppRepository.getUserPostList()
    private val userPostList: MutableLiveData<MutableList<Offer>> = MutableLiveData()
    private val localDatabase: AppDatabase = AppDatabase.getInstance(application)!!
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val myTAG:String="MyTag"
    private val currentUserID= firebaseAuth.currentUser?.uid
    private var lastResult: DocumentSnapshot? = null
    val list2 = mutableListOf<Offer>()


    @JvmName("getUserPostList")
    fun getUserPostList(): LiveData<MutableList<OfferRoomEntity>>? {
        return localDatabase.appDao()?.getOffersUser(currentUserID!!)
    }


    fun loadUserPosts() {

        val query: Query = if (lastResult == null) {
            db.collection("Offers").whereEqualTo("userId",currentUserID).orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(10)
        } else {
            db.collection("Offers") .whereEqualTo("userId",currentUserID).orderBy("timestamp", Query.Direction.DESCENDING)
                .startAfter(lastResult?.toObject(OfferRoomEntity::class.java)?.timestamp)
                .limit(10)

        }


//        Log.d(myTAG, " lastResult is  : ${lastResult?.toObject(Post::class.java)?.postId}")

        query.get()
            .addOnSuccessListener { queryDocumentSnapshots ->
                for (documentSnapshot in queryDocumentSnapshots) {
                    val offer: OfferRoomEntity = documentSnapshot.toObject(OfferRoomEntity::class.java)

                    viewModelScope.launch(Dispatchers.IO) {
                        localDatabase.appDao()?.addOfferUser(offer)
                    }

//                    list2.addAll(listOf(offer))
//                    userPostList.postValue(list2)
                }

                if (queryDocumentSnapshots.size() > 0) {
                    lastResult = queryDocumentSnapshots.documents[queryDocumentSnapshots.size() - 1]
                }

            }.addOnFailureListener {
                Log.d(myTAG,"Exception is : "+it.localizedMessage)
                Log.d(myTAG,"Query failed")
            }
    }


}