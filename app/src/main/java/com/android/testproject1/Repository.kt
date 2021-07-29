package com.android.testproject1

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.testproject1.model.Chat
import com.android.testproject1.model.Post
import com.android.testproject1.model.Users
import com.android.testproject1.room.enteties.AppDatabase
import com.android.testproject1.room.enteties.PostRoomEntity
import com.android.testproject1.room.enteties.AppDao
import com.android.testproject1.services.App
import com.google.firebase.auth.*
import com.google.firebase.firestore.*
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class Repository(private val application: Application)
{
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    private val postList: MutableLiveData<MutableList<Post>> = MutableLiveData()
    private val userPostList: MutableLiveData<MutableList<Post>> = MutableLiveData()
    private val userList: MutableLiveData<MutableList<Users>> = MutableLiveData()
    private val groupList: MutableLiveData<MutableList<Users>> = MutableLiveData()
    private val chatList: MutableLiveData<MutableList<Chat>> = MutableLiveData()
    private val groupChatList: MutableLiveData<MutableList<Chat>> = MutableLiveData()
    private val userChatList: MutableLiveData<MutableList<Users>> = MutableLiveData()


    private val userLiveData: MutableLiveData<FirebaseUser> = MutableLiveData()
    private val loggedOutLiveData: MutableLiveData<Boolean> = MutableLiveData()
    private val postUploaded: MutableLiveData<Boolean> = MutableLiveData()
    private val detailsRegisteredData: MutableLiveData<Boolean> = MutableLiveData()



    private val myTAG:String="MyTag"
    private var checkduplicate:String?=null
    private var checkduplicate2:String?=null
    private val currentUserID= firebaseAuth.currentUser?.uid
//    private var bitmap: Bitmap? = null
//    var resized: Bitmap? = null
//
//    private var count: Int = 0
//    private var postID :String?=null

    private var mChatMessageEventListener: ListenerRegistration? = null
    private var mUserListEventListener:ListenerRegistration? = null
    private val mMessageIds: Set<String> = HashSet()


//    private var arr:ArrayList<String> = ArrayList()
//    private var arr2:ArrayList<String> = ArrayList()
//    private var arr3:ArrayList<String> = ArrayList()


    init {
        if (firebaseAuth.currentUser != null) {
            userLiveData.postValue(firebaseAuth.currentUser)
            loggedOutLiveData.postValue(false)
            loadDataPost()
//            loadUserChatList()
        }
    }

    fun loadChat(chatKey:String) {
        val list2 = mutableListOf<Chat>()

//        db.collection("Chats").document("UserChats").collection(chatKey)
        db.collection("Chats").document(chatKey).collection("UserChats")
            .orderBy("timestamp",Query.Direction.ASCENDING)
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    Log.w(myTAG, "listen:error", e)
                    return@addSnapshotListener
                }

                for (dc in snapshots!!.documentChanges) {
                    when (dc.type) {

                        DocumentChange.Type.ADDED ->{
                            dc.document.toObject(Chat::class.java).let {
                                list2.add(it)
                            }
                            chatList.postValue(list2)
                        }
                        DocumentChange.Type.MODIFIED ->{

                        }
                        DocumentChange.Type.REMOVED ->{
                            Log.d(myTAG, "Removed city: ${dc.document.data}")
                        }
                    }
                }
            }
    }

    fun loadGroupChat(postId: String,groupId:String){

        val list2 = mutableListOf<Chat>()

        db.collection("Posts").document(postId).collection("Groups")
            .document(groupId).collection("Chats")
            .orderBy("timestamp",Query.Direction.ASCENDING)
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    Log.w(myTAG, "listen:error", e)
                    return@addSnapshotListener
                }

                for (dc in snapshots!!.documentChanges) {
                    when (dc.type) {

                        DocumentChange.Type.ADDED ->{
                            dc.document.toObject(Chat::class.java).let {
                                list2.add(it)
                            }
                            groupChatList.postValue(list2)
                        }
                        DocumentChange.Type.MODIFIED ->{

                        }
                        DocumentChange.Type.REMOVED ->{
                            Log.d(myTAG, "Removed city: ${dc.document.data}")
                        }
                    }
                }
            }


    }

    fun loadGroupsOnPost(postId:String){
        val list2 = mutableListOf<Users>()
        db.collection("Posts").document(postId).collection("Groups")
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    Log.w(myTAG, "listen:error", e)
                    return@addSnapshotListener
                }
                for (dc in snapshots!!.documentChanges) {
                    when (dc.type) {
                        DocumentChange.Type.ADDED ->{
                            db.collection("Users")
                                .whereEqualTo("id", dc.document.id)
                                .get()
                                .addOnCompleteListener {
                                    if (it.isSuccessful){
                                        val objects2: List<Users> = it.result?.toObjects(Users::class.java) as List<Users>
                                        list2.addAll(objects2)
                                        userList.postValue(list2)

                                    }
                                }

                        }
                        DocumentChange.Type.MODIFIED ->{

                        }
                        DocumentChange.Type.REMOVED ->{
                            Log.d(myTAG, "Removed city: ${dc.document.data}")
                        }
                    }
                }
            }


    }



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

                        val list2 = mutableListOf<Users>()
                        if (group != null) {
                            for (id in group) {
                                id.trim()
                                db.collection("Users")
                                    .whereEqualTo("id", id)
                                    .get()
                                    .addOnCompleteListener {
                                        if (it.isSuccessful) {
                                            val objects2: List<Users> =
                                                it.result?.toObjects(Users::class.java) as List<Users>
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

    fun loadUserChatList(){


        val docRef = currentUserID?.let { db.collection("ChatList").document(it) }
        docRef?.get()?.addOnSuccessListener { document ->
            if (document != null) {

                val group = document["ChatList"] as List<String>?
                Log.d(myTAG, "Document data is  $group")

                if (checkduplicate2!=document.data.toString()) {
                    checkduplicate2=document.data.toString()

                    val list2 = mutableListOf<Users>()
                    if (group != null) {
                        for (id in group) {
                            id.trim()
                            db.collection("Users")
                                .whereEqualTo("id", id)
                                .get()
                                .addOnCompleteListener {
                                    if (it.isSuccessful) {
                                        val objects2: List<Users> =
                                            it.result?.toObjects(Users::class.java) as List<Users>
                                        list2.addAll(objects2)
                                        Log.d(myTAG, "list in chatList is : $list2")
                                        //                                    userList.value = list2
                                        userChatList.postValue(list2)

                                    }
                                }
                        }
                    }

                }

            } else {
                Log.d(myTAG, "No such document")
            }
        }?.addOnFailureListener { exception ->
            Log.d(myTAG, "get failed with ", exception)
        }

    }

    fun loadDataPost() {
//        db.collection("Posts")
//            .get()
//            .addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    // Convert the whole snapshot to a POJO list
//                    val objects: List<Post> = task.result?.toObjects(Post::class.java) as List<Post>
//                    val list = mutableListOf<Post>()
//                    list.addAll(objects)
//                    postList.value = list
//
//                } else {
//                    Log.d(myTAG, "Error getting documents: ", task.exception)
//                }
//            }
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
                            postList.postValue(list2)
                        }
                        DocumentChange.Type.MODIFIED ->{

                        }
                        DocumentChange.Type.REMOVED ->{
                            Log.d(myTAG, "Removed city: ${dc.document.data}")
                        }
                    }
                }
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



    fun logOut() {
        firebaseAuth.signOut()
        loggedOutLiveData.postValue(true)
    }

    @JvmName("getPostList")
    fun getPostList(): MutableLiveData<MutableList<Post>> {
        return postList
    }

    @JvmName("getUserPostList")
    fun getUserPostList(): MutableLiveData<MutableList<Post>> {
        return userPostList
    }

    @JvmName("getUserList")
    fun getUserList(): MutableLiveData<MutableList<Users>> {
        return userList
    }

    @JvmName("getGroupList")
    fun getGroupList(): MutableLiveData<MutableList<Users>> {
        return groupList
    }

    @JvmName("userChatsList")
    fun getUserChatsList(): MutableLiveData<MutableList<Users>> {
        return userChatList
    }

    @JvmName("getChatList")
    fun getChatList(): MutableLiveData<MutableList<Chat>> {
        return chatList
    }

    @JvmName("getGroupChatList")
    fun getGroupChatList(): MutableLiveData<MutableList<Chat>> {
        return groupChatList
    }



    @JvmName("getUserLiveData1")
    fun getUserLiveData(): MutableLiveData<FirebaseUser> {
        return userLiveData
    }

    @JvmName("detailsRegisteredCheck")
    fun getDetailsRegisteredData(): MutableLiveData<Boolean> {
        return detailsRegisteredData
    }


}



//                            for (document in ) {
////                    Log.d(myTAG, "${document.id} => ${document.data}")
//                                db.collection("Users")
//                                    .whereEqualTo("id", document.id)
//                                    .get()
//                                    .addOnCompleteListener {
//                                        if (it.isSuccessful){
//                                            val objects2: List<Users> = it.result?.toObjects(Users::class.java) as List<Users>
//                                            list2.addAll(objects2)
//                                            userList.postValue(list2)
//
//                                        }
//                                    }
//                            }


//        db.collection("Posts").document(postId).collection("Groups")
//            .get()
//            .addOnSuccessListener { result ->
//
//                for (document in result) {
////                    Log.d(myTAG, "${document.id} => ${document.data}")
//                    db.collection("Users")
//                        .whereEqualTo("id", document.id)
//                        .get()
//                        .addOnCompleteListener {
//                            if (it.isSuccessful){
//                                val objects2: List<Users> = it.result?.toObjects(Users::class.java) as List<Users>
//                                list2.addAll(objects2)
//                                userList.postValue(list2)
//
//                            }
//                        }
//                }
//            }
//            .addOnFailureListener { exception ->
//                Log.d(myTAG, "Error getting documents: ", exception)
//            }

