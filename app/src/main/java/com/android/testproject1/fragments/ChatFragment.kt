package com.android.testproject1.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.testproject1.databinding.FragmentChatBinding
import com.android.testproject1.model.Users
import com.android.testproject1.room.enteties.ChatRoomEntity
import com.android.testproject1.room.enteties.UsersChatListEntity
import com.android.testproject1.viewmodels.ChatFragmentViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.android.synthetic.main.fragment_chat.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.notifications_item.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.HashMap

class ChatFragment : Fragment() {

    private lateinit var binding:FragmentChatBinding
    private lateinit var chatreference: FirebaseFirestore
    private lateinit var userId:String
    private var name:String?=null
    private lateinit var currentUserId:String
    private lateinit var firebseauth: FirebaseAuth
    private lateinit var mViewModel:ChatFragmentViewModel
    private var checkChatList:Boolean=false
    private var checkKeyboard:Boolean=false

    var isScrolling = false
    var currentItems = 0
    var totalItems:Int = 0
    var scrollOutItems:Int = 0
    var scrollOutItemsDOWN:Int=0

    companion object {
        var chatKey: String? = null
        var Key: String? = null
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentChatBinding.inflate(inflater, container, false)

        val bundle = this.arguments
        val chatsOpened = bundle?.getString("chatsOpened")

        if (chatsOpened == "fromGroups") {
            val groupItem = bundle.getParcelable<UsersChatListEntity>("userItem")
            Log.d("MyTag", "userItem is : " + groupItem?.id)
            if (groupItem != null) {
                userId = groupItem.id
            }
        } else if (chatsOpened == "fromMessages") {
            val groupItem = bundle.getParcelable<UsersChatListEntity>("userItem")
            if (groupItem != null) {
                userId = groupItem.id
            }
        } else if (chatsOpened == "fromProfile") {
            val userIdIntent = arguments?.getString("userId")
            if (userIdIntent != null) {
                userId = userIdIntent
            }

            firebseauth = FirebaseAuth.getInstance()
            currentUserId = firebseauth.currentUser?.uid.toString()
            chatreference = FirebaseFirestore.getInstance()

            val i = currentUserId.compareTo(userId)
            if (+i >= 1) {
                chatKey = userId + currentUserId
            } else if (+i < 1) {
                chatKey = currentUserId + userId
            } else if (+i == 0) {
                chatKey = ""
            }

            mViewModel = ViewModelProvider(this,
                ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application))
                .get(ChatFragmentViewModel::class.java)
//            CoroutineScope(Dispatchers.IO).launch {}

            chatKey?.let {
                mViewModel.getChatList(it)?.observe(viewLifecycleOwner, {
                    binding.chatList = it
                })
            }






            chatreference.collection("Users").document(currentUserId).get().addOnSuccessListener {
                if (it != null) {
                    val notifPojo: Users? = it.toObject(Users::class.java)
                    if (notifPojo != null) {
                        name = notifPojo.name
                    }
                }
            }

            chatreference.collection("ChatList").document(currentUserId).get()
                .addOnSuccessListener {

                    val group = it.get("ChatList") as List<String>?
                    if (group != null) {
                        for (i in group) {
                            if (i == userId) {
                                checkChatList = true
                            }
                        }
                    }

                }

            CoroutineScope(Dispatchers.IO).launch {
                chatKey?.let { mViewModel.loadChat(it) }
            }


            binding.btnSend.setOnClickListener {

                Log.d("MyTag", "check chat list is  $checkChatList")
                val message = binding.textSend.text.toString().trim()
                if (message.isEmpty()) {
                    Toast.makeText(activity, "Empty Message Can't be sent", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    sendMessage(currentUserId, userId, message)
                    binding.textSend.setText("")

                    if (!checkChatList) {
                        if (userId != currentUserId) {
                            chatreference.collection("ChatList").document(currentUserId)
                                .update("ChatList", FieldValue.arrayUnion(userId))
                                .addOnSuccessListener {
                                    checkChatList = true
                                }.addOnFailureListener {
                                    Log.d("MyTag", "error is  " + it.localizedMessage)
                                }
                        }

                    }


                }

            }

            chatKey?.let { mViewModel.queryLoad(it) }

            binding.messageRecyclerview.addOnScrollListener(object :
                RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                        isScrolling = true
                        Log.d("MyTag","is scrolling "+isScrolling)
                    }
                }

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    currentItems = messageRecyclerview.layoutManager?.childCount!!
                    totalItems = messageRecyclerview.layoutManager?.itemCount!!
                    scrollOutItems = (messageRecyclerview.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                    scrollOutItemsDOWN = (messageRecyclerview.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()

                    Log.d("MyTag", " currentItems : $currentItems scrollOutItems :$scrollOutItems totalItems : $totalItems")
//                    Log.d("MyTag", " currentItems : $currentItems  totalItems : $totalItems scrollOutItemsDown :$scrollOutItemsDOWN")

                    if (isScrolling &&currentItems + scrollOutItems == totalItems) {
                        isScrolling = false
                        chatKey?.let { mViewModel.queryLoad(it) }
                    }
//                    if (isScrolling &&currentItems + scrollOutItemsDOWN == totalItems) {
//                        isScrolling = false
//                        chatKey?.let { mViewModel.queryLoad(it) }
//                    }


                }
            })

            //                if(!recyclerView.canScrollVertically()) {
//                    // LOAD MORE
//                    chatKey?.let { mViewModel.loadChat(it) }
//                }

        }

        return binding.root
    }




    private fun sendMessage(sender: String?, receiver: String?, message: String?) {
        if (sender == receiver) {
            Toast.makeText(activity, "Message Can't be sent", Toast.LENGTH_SHORT).show()
        } else {

            val chatRef = chatKey?.let { chatreference.collection("Chats")
                .document(it).collection("UserChats") }

            val messageID = chatRef?.document()?.id
            val hashMap = HashMap<String?, Any?>()
            hashMap["sender"] = sender
            hashMap["receiver"] = receiver
            hashMap["message"] = message
            hashMap["name"] = name
            hashMap["timestamp"] = FieldValue.serverTimestamp()
            hashMap["isseen"] = false
            hashMap["chatKey"] = chatKey
            hashMap["id"] =messageID

            if (messageID != null) {

//                checkPendingWrites(hashMap,chatRef,messageID,sender!!,receiver!!,message!!)
                chatRef.document(messageID).set(hashMap, SetOptions.merge()).addOnSuccessListener {

                    chatRef.document(messageID).get().addOnSuccessListener {

                        val chat: ChatRoomEntity? = it?.toObject(ChatRoomEntity::class.java)
                        val hashMap2 = HashMap<String?, Any?>()
                        hashMap2["sender"] = sender
                        hashMap2["receiver"] = receiver
                        hashMap2["message"] = message
                        hashMap2["name"] = name
                        hashMap2["timestamp"] = chat?.timestamp
                        hashMap2["isseen"] = false
                        hashMap2["chatKey"] = chatKey
                        hashMap2["id"] =messageID

                        chatRef.document("recentMessage").set(hashMap2)


                    }
                }

//                chatRef.document("recentMessage").set(hashMap)
            }
            Log.d("MyTag","chatRef Doc Id : "+messageID)

        }
    }

    private fun checkPendingWrites(hashMap: HashMap<String?, Any?>,chatRef: CollectionReference
                                   ,messageID:String,sender:String,receiver:String,message:String) {

        chatRef.document("recentMessage").get().addOnSuccessListener {
            if (it!=null){
                if (!it.metadata.hasPendingWrites()){

                    chatRef.document("recentMessage").set(hashMap).addOnSuccessListener {

                        chatRef.document("recentMessage").get().addOnSuccessListener {

                            val chat: ChatRoomEntity? = it?.toObject(ChatRoomEntity::class.java)
                            val hashMap2 = HashMap<String?, Any?>()
                            val timestamp= chat?.timestamp
                            hashMap2["sender"] = sender
                            hashMap2["receiver"] = receiver
                            hashMap2["message"] = message
                            hashMap2["name"] = name
                            hashMap2["timestamp"] = timestamp
                            hashMap2["isseen"] = false
                            hashMap2["chatKey"] = chatKey
                            hashMap2["id"] =messageID

                            chatRef.document("recentMessage2").set(hashMap)

                        }

                    }
                }else{
                    checkPendingWrites(hashMap,chatRef,messageID,sender,receiver,message)
                }
            }
        }

    }


}