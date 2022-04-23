package com.android.testproject1.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.android.testproject1.Repository
import com.android.testproject1.model.Offer
import com.android.testproject1.room.enteties.AppDatabase
import com.android.testproject1.room.enteties.OfferRoomEntity
import com.android.testproject1.room.enteties.OffersSavedRoomEntity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchFragmentViewModel(application: Application) : AndroidViewModel(application) {

    private val myTAG: String = "MyTag"
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var lastResult: DocumentSnapshot? = null
    private val authAppRepository: Repository = Repository(application)
    private val offerList: MutableLiveData<MutableList<Offer>> = MutableLiveData()
    private val list2 = mutableListOf<Offer>()
    private val localDatabase: AppDatabase = AppDatabase.getInstance(application)!!
    private val currentUserID: String? =firebaseAuth.currentUser?.uid
    var counter =0


    @JvmName("getPostList")
    fun getPostList(): LiveData<MutableList<OfferRoomEntity>>? {
//        return offerList
        return localDatabase.appDao()?.getAllOffers()
    }

    fun loadOffers() {

        val query: Query = if (lastResult == null) {
            db.collection("Offers").orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(10)
        } else {
            db.collection("Offers").orderBy("timestamp", Query.Direction.DESCENDING)
                .startAfter(lastResult?.toObject(OfferRoomEntity::class.java)?.timestamp)
                .limit(10)
        }

//        Log.d(myTAG, " lastResult is  : ${lastResult?.toObject(Post::class.java)?.postId}")

        query.get()
            .addOnSuccessListener { queryDocumentSnapshots ->
                if (counter == 0) {
                    viewModelScope.launch(Dispatchers.IO) {
                        localDatabase.appDao()?.deleteTableOfferRoomEntity()
                    }
                }
                counter++
                for (documentSnapshot in queryDocumentSnapshots) {
//                    val offer: Offer = documentSnapshot.toObject(Offer::class.java)
                    val offer: OfferRoomEntity = documentSnapshot.toObject(OfferRoomEntity::class.java)
//                    val userImages: UserImagesRoomEntity = documentSnapshot.toObject(UserImagesRoomEntity::class.java)

                    viewModelScope.launch(Dispatchers.IO) {
                        localDatabase.appDao()?.addOfferUser(offer)
//                        localDatabase.appDao()?.insertImage(userImages)
                    }
//                    list2.addAll(listOf(offer))
//                    Log.d(myTAG,"post Ids are "+offer.postId)
//                    offerList.postValue(list2)
                }

//                Log.d(myTAG,"query Snapshot size "+queryDocumentSnapshots.size())
                if (queryDocumentSnapshots.size() > 0) {
                    lastResult = queryDocumentSnapshots.documents[queryDocumentSnapshots.size() - 1]
                }

            }.addOnFailureListener {
                Log.d(myTAG,"Exception is : "+it.localizedMessage)
            }
    }

    fun loadRecentOffer() {

        if (currentUserID != null) {
            db.collection("Offers").document("recentOffer")
                .addSnapshotListener { value, error ->
                    if (value != null) {
                        val offerRoomEntity: OfferRoomEntity? = value.toObject(OfferRoomEntity::class.java)
                        viewModelScope.launch(Dispatchers.IO) {
                            if (offerRoomEntity != null) {
                                localDatabase.appDao()?.addOfferUser(offerRoomEntity)
                            }
                        }

                    }

                }
        }
    }

    fun loadSavedPosts(){

        val query: Query = db.collection("Users").document(currentUserID!!).collection("Saved")
                .orderBy("timestamp", Query.Direction.DESCENDING)

        query.get()
            .addOnSuccessListener { queryDocumentSnapshots ->
                for (documentSnapshot in queryDocumentSnapshots) {
                    val offerSaved: OffersSavedRoomEntity = documentSnapshot.toObject(
                        OffersSavedRoomEntity::class.java)
                    viewModelScope.launch(Dispatchers.IO) {
                        localDatabase.appDao()?.saveOffer(offerSaved)
                    }
                }
            }.addOnFailureListener {
                Log.d(myTAG,"Exception is : "+it.localizedMessage)
            }

    }
}