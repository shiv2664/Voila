package com.android.testproject1.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.android.testproject1.Repository
import com.android.testproject1.model.Chat
import com.android.testproject1.model.Post
import com.android.testproject1.room.enteties.AppDatabase
import com.android.testproject1.room.enteties.UsersRoomEntity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ViewPagerFragmentPostViewModel(application: Application) :AndroidViewModel(application) {

    private val authAppRepository: Repository = Repository(application)
//    private val userPostList: MutableLiveData<MutableList<Post>> = authAppRepository.getUserPostList()
    private val userPostList: MutableLiveData<MutableList<Post>> = MutableLiveData()

    private val localDatabase: AppDatabase = AppDatabase.getInstance(application)!!
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val myTAG:String="MyTag"
    private val currentUserID= firebaseAuth.currentUser?.uid
    private var lastResult: DocumentSnapshot? = null
    val list2 = mutableListOf<Post>()

    @JvmName("getUserPostList")
    fun getUserPostList(): MutableLiveData<MutableList<Post>> {
        return userPostList
    }

    fun loadUserPosts() {


        val query: Query = if (lastResult == null) {
            db.collection("Posts").orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(10)
        } else {
            db.collection("Posts").orderBy("timestamp", Query.Direction.DESCENDING)
                .startAfter(lastResult?.toObject(Post::class.java)?.timestamp)
                .limit(10)
        }


        Log.d(myTAG, " lastResult is  : ${lastResult?.toObject(Post::class.java)?.postId}")

        query.get()
            .addOnSuccessListener { queryDocumentSnapshots ->
                for (documentSnapshot in queryDocumentSnapshots) {
                    val post: Post = documentSnapshot.toObject(Post::class.java)
                    list2.addAll(listOf(post))
                    Log.d(myTAG,"post Ids are "+post.postId)
                    userPostList.postValue(list2)
                }

                if (queryDocumentSnapshots.size() > 0) {
                    lastResult = queryDocumentSnapshots.documents[queryDocumentSnapshots.size() - 1]
                }

            }.addOnFailureListener {
                Log.d(myTAG,"Exception is : "+it.localizedMessage)
            }
    }


    fun userPosts() {
        if (currentUserID != null) {
            db.collection("Users").document(currentUserID).collection("Posts")
                .document("Uploaded")
                .get().addOnSuccessListener {
                    if (it!=null){
                        val posts = it["UploadedPosts"] as List<String>?
                        Log.d(myTAG, "Document data is  $posts")

                        if (posts != null) {
                            for (i in posts){

                                val list2 = mutableListOf<Post>()
                                db.collection("Posts")
                                    .addSnapshotListener { snapshots, e ->
                                        if (e != null) {
                                            Log.w(myTAG, "listen:error", e)
                                            return@addSnapshotListener
                                        }

                                        for (dc in snapshots!!.documentChanges) {
                                            when (dc.type) {

                                                DocumentChange.Type.ADDED ->{
                                                    dc.document.toObject(Post::class.java).let {
                                                        list2.add(it)
                                                    }
                                                    userPostList.postValue(list2)
                                                }
                                                DocumentChange.Type.MODIFIED ->{

                                                }
                                                DocumentChange.Type.REMOVED ->{
                                                    Log.d(myTAG, "Removed : ${dc.document.data}")
                                                }
                                            }
                                        }
                                    }

                            }
                        }


                    }
                }
        }

    }

}