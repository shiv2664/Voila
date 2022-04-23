package com.android.testproject1.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.android.testproject1.Repository
import com.android.testproject1.model.Post
import com.android.testproject1.room.enteties.AppDatabase
import com.android.testproject1.room.enteties.OffersSavedRoomEntity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SavedFragmentViewModel(application: Application) :AndroidViewModel(application) {

    private val authAppRepository: Repository = Repository(application)
    private val localDatabase: AppDatabase = AppDatabase.getInstance(application)!!
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val savedOfferList = MutableLiveData<List<OffersSavedRoomEntity>>()
    private val myTAG:String="MyTag"
    private var checkduplicate2:String?=null
    private var lastResult: DocumentSnapshot? = null
    private val currentUserID= firebaseAuth.currentUser?.uid
    val list2 = mutableListOf<OffersSavedRoomEntity>()

    @JvmName("savedOffersList")
    fun getSavedOffers(): LiveData<MutableList<OffersSavedRoomEntity>>? {
        return localDatabase.appDao()?.getSavedOffers()
    }

    fun loadSavedPosts(){

        val query: Query = if (lastResult == null) {
            db.collection("Users").document(currentUserID!!).collection("Saved")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(10)
        } else {
            db.collection("Posts").orderBy("timestamp", Query.Direction.DESCENDING)
                .startAfter(lastResult?.toObject(Post::class.java)?.timestamp)
                .limit(10)
        }

        query.get()
            .addOnSuccessListener { queryDocumentSnapshots ->
                for (documentSnapshot in queryDocumentSnapshots) {
                    val offerSaved: OffersSavedRoomEntity = documentSnapshot.toObject(OffersSavedRoomEntity::class.java)

                    viewModelScope.launch(Dispatchers.IO) {
                        localDatabase.appDao()?.saveOffer(offerSaved)
                    }

//                    savedOfferList.postValue(list2)
                }

                if (queryDocumentSnapshots.size() > 0) {
                    lastResult = queryDocumentSnapshots.documents[queryDocumentSnapshots.size() - 1]
                }

            }.addOnFailureListener {
                Log.d(myTAG,"Exception is : "+it.localizedMessage)
            }

    }

}