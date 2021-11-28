package com.android.testproject1.viewmodels

import android.app.Application
import android.util.Log
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.android.testproject1.Repository
import com.android.testproject1.model.Post
import com.android.testproject1.room.enteties.AppDatabase
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class HomeFragmentViewModel(application: Application): AndroidViewModel(application) {

    private val myTAG: String = "MyTag"
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var lastResult: DocumentSnapshot? = null
    private val authAppRepository: Repository = Repository(application)
    private val postList: MutableLiveData<MutableList<Post>> = MutableLiveData()
    private val list2 = mutableListOf<Post>()

    @JvmName("getPostList")
    fun getPostList(): MutableLiveData<MutableList<Post>> {
        return postList
    }

    fun loadDataPost() {
//        authAppRepository.loadDataPost()
        loadNotes()
    }

    private fun loadNotes(){

        Log.d(myTAG,"last result is : "+lastResult?.toObject(Post::class.java)?.postId)

        val query: Query= if (lastResult == null) {
            db.collection("Posts").orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(10)
        } else {
            db.collection("Posts").orderBy("timestamp", Query.Direction.DESCENDING)
                .startAfter(lastResult?.toObject(Post::class.java)?.timestamp)
                .limit(10)
        }


//        Log.d(myTAG, " lastResult is  : ${lastResult?.toObject(Post::class.java)?.postId}")

        query.get()
            .addOnSuccessListener { queryDocumentSnapshots ->
                for (documentSnapshot in queryDocumentSnapshots) {
                    val post: Post = documentSnapshot.toObject(Post::class.java)
                    list2.addAll(listOf(post))
//                    Log.d(myTAG,"post Ids are "+post.postId)
                    postList.postValue(list2)
                }

                Log.d(myTAG,"query Snapshot size "+queryDocumentSnapshots.size())
                if (queryDocumentSnapshots.size() > 0) {
                    lastResult = queryDocumentSnapshots.documents[queryDocumentSnapshots.size() - 1]
                }

            }.addOnFailureListener {
                Log.d(myTAG,"Exception is : "+it.localizedMessage)
            }
    }


}



//private val localDatabase: AppDatabase = AppDatabase.getInstance(application)!!
//private val mutablePostList = MutableLiveData<List<PostRoomEntity>>()
//    val postList: LiveData<List<PostRoomEntity>> = mutablePostList

//    @JvmName("getPostList1")
//    fun getPostList1(): LiveData<List<PostRoomEntity>> {
//        return postList
//    }

//    @JvmName("getPostList2")
//    fun getPostList2(): LiveData<MutableList<PostRoomEntity>>? {
//        return localDatabase.appDao()?.getPosts()
////        return postList
//    }


//    fun loadDataPost() {
//
//        db.collection("Posts")
//            .addSnapshotListener { snapshot, e ->
//                if (e != null) {
//                    Log.w(myTAG, "listen:error", e)
//                    return@addSnapshotListener
//                }
//
//                mutablePostList.value = snapshot!!.documents.map {
//                    it.toObject(PostRoomEntity::class.java)!!
//                }
//
//                viewModelScope.launch {
//                    for (dc in snapshot.documentChanges) {
//                        when (dc.type) {
//                            DocumentChange.Type.ADDED -> {
//                                val newPost = dc.document.toObject(PostRoomEntity::class.java)
//
//                                viewModelScope.launch(Dispatchers.IO) {
//                                    localDatabase.appDao()?.insertPost(newPost)
//                            }
//
//                            }
//                            DocumentChange.Type.MODIFIED -> {
//
//                            }
//                            DocumentChange.Type.REMOVED -> {
//                                Log.d(myTAG, "Removed city: ${dc.document.data}")
//                            }
//                        }
//                    }
////                    saveDataRoom(postListRoom, localDatabase) // don't know what this does
//                }
//            }
//
//    }






