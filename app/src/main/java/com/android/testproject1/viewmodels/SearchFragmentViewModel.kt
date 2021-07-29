package com.android.testproject1.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.android.testproject1.Repository
import com.android.testproject1.model.Offer
import com.android.testproject1.model.Post
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class SearchFragmentViewModel(application: Application) : AndroidViewModel(application) {

    private val myTAG: String = "MyTag"
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var lastResult: DocumentSnapshot? = null
    private val authAppRepository: Repository = Repository(application)
    private val offerList: MutableLiveData<MutableList<Offer>> = MutableLiveData()
    private val list2 = mutableListOf<Offer>()


    @JvmName("getPostList")
    fun getPostList(): MutableLiveData<MutableList<Offer>> {
        return offerList
    }

    fun loadNotes() {


        val query: Query = if (lastResult == null) {
            db.collection("Offers").orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(5)
        } else {
            db.collection("Offers").orderBy("timestamp", Query.Direction.DESCENDING)
                .startAfter(lastResult?.toObject(Post::class.java)?.timestamp)
                .limit(5)
        }


        Log.d(myTAG, " lastResult is  : ${lastResult?.toObject(Post::class.java)?.postId}")

        query.get()
            .addOnSuccessListener { queryDocumentSnapshots ->
                for (documentSnapshot in queryDocumentSnapshots) {
                    val offer: Offer = documentSnapshot.toObject(Offer::class.java)
                    list2.addAll(listOf(offer))
                    Log.d(myTAG,"post Ids are "+offer.postId)
                    offerList.postValue(list2)
                }

//                Log.d(myTAG,"query Snapshot size "+queryDocumentSnapshots.size())
                if (queryDocumentSnapshots.size() > 0) {
                    lastResult = queryDocumentSnapshots.documents[queryDocumentSnapshots.size() - 1]
                }

            }.addOnFailureListener {
                Log.d(myTAG,"Exception is : "+it.localizedMessage)
            }
    }
}