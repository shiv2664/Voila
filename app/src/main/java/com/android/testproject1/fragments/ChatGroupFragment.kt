package com.android.testproject1.fragments

import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.testproject1.R
import com.android.testproject1.databinding.FragmentChatBinding
import com.android.testproject1.databinding.FragmentChatGroupBinding
import com.android.testproject1.model.Users
import com.android.testproject1.room.enteties.ChatRoomEntity
import com.android.testproject1.room.enteties.UsersChatListEntity
import com.android.testproject1.viewmodels.ChatFragmentViewModel
import com.android.testproject1.viewmodels.GroupChatFragmentViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_chat.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.HashMap

class ChatGroupFragment : Fragment() {

    private lateinit var binding: FragmentChatGroupBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var mViewModel: GroupChatFragmentViewModel
    private lateinit var groupItem: UsersChatListEntity
    private var currentUserId: String? = null
    var groupId: String? = null
    private var name: String? = null


    var isScrolling = false
    var currentItems = 0
    var totalItems: Int = 0
    var scrollOutItems: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatGroupBinding.inflate(inflater, container, false)

//        val toolbar = binding.myToolbar
//        (activity as AppCompatActivity).setSupportActionBar(toolbar)
//        (activity as AppCompatActivity).supportActionBar?.title = "Enjoyah"
//        setHasOptionsMenu(true)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        currentUserId = auth.currentUser?.uid


        val bundle = this.arguments
        groupItem = bundle?.getParcelable<UsersChatListEntity>("userItem")!!
        groupId = groupItem.createdBy

        Log.d("MyTag", "Group Id is $groupId")
        Log.d("MyTag", "post ID  is " + groupItem.postId)

        mViewModel = ViewModelProvider(
            this, ViewModelProvider.AndroidViewModelFactory
                .getInstance(requireActivity().application)
        ).get(GroupChatFragmentViewModel::class.java)

//       GroupsOnOfferFragment.postId?.let { groupId?.let { it1 -> mViewModel.loadGroupChat(it, it1) } }
//        groupItem.postId.let { groupItem.groupId.let { it1 -> mViewModel.loadChat(it, it1) } }

//        groupItem.postId.let { groupItem.groupId.let { it1 -> mViewModel.queryLoad(it, it1) } }

        CoroutineScope(Dispatchers.IO).launch {
            groupItem.postId.let { groupItem.groupId.let { it1 -> mViewModel.loadChat(it, it1) } }
        }

        CoroutineScope(Dispatchers.IO).launch {
            groupItem.postId.let { groupItem.groupId.let { it1 -> mViewModel.queryLoad(it, it1) } }
        }


        mViewModel.getGroupChatList(groupItem.postId,groupItem.groupId)?.observe(viewLifecycleOwner, {
                binding.groupChatList = it

            })

        currentUserId?.let { it1 ->
            firestore.collection("Users").document(it1).get().addOnSuccessListener {
                if (it != null) {
                    val notifPojo: Users? = it.toObject(Users::class.java)
                    if (notifPojo != null) {
                        name = notifPojo.name
                    }
                }
            }
        }

        binding.btnSend2.setOnClickListener {

            val message = binding.textSend2.text.toString().trim()

            Log.d("MyTag", "message is  $message")

            if (message.isEmpty()) {
                Toast.makeText(activity, "Empty Message Can't be sent", Toast.LENGTH_SHORT).show()
            } else {
                groupId?.let { it1 -> sendMessage(currentUserId, message, it1) }
                binding.textSend2.setText("")
            }

        }


        binding.chatGroupRecyclerview.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                currentItems = binding.chatGroupRecyclerview.layoutManager?.childCount!!
                totalItems = binding.chatGroupRecyclerview.layoutManager?.itemCount!!
                scrollOutItems =
                    (binding.chatGroupRecyclerview.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()

                Log.d(
                    "MyTag",
                    " currentItems : $currentItems  totalItems : $totalItems scrollOutItems :$scrollOutItems"
                )

                if (isScrolling && currentItems + scrollOutItems == totalItems) {
                    isScrolling = false
//                    mViewModel.removelistener()
                    groupItem.postId.let { groupId?.let { it1 -> mViewModel.queryLoad(it, it1) } }
                }


            }
        })


        return binding.root
    }

    private fun sendMessage(
        sender: String?,
        message: String?,
        groupId: String,
        receiver: String = ""
    ) {

        val chatRef = firestore.collection("Offers").document(groupItem.postId).collection("Groups")
            .document(groupId)
            .collection("Chats")

        val messageId = chatRef.document().id

        val hashMap = HashMap<String?, Any?>()
        hashMap["sender"] = sender
        hashMap["receiver"] = receiver
        hashMap["message"] = message
        hashMap["name"] = name
        hashMap["postId"]= groupItem.postId
        hashMap["groupId"]=groupItem.groupId
        hashMap["id"] =messageId
        hashMap["timestamp"] = FieldValue.serverTimestamp()
        hashMap["isseen"] = false

        chatRef.document(messageId).set(hashMap, SetOptions.merge()).addOnSuccessListener {

            chatRef.document(messageId).get().addOnSuccessListener {
                val chat: ChatRoomEntity? = it?.toObject(ChatRoomEntity::class.java)

                val hashMap2 = HashMap<String?, Any?>()
                hashMap2["sender"] = sender
                hashMap2["receiver"] = receiver
                hashMap2["message"] = message
                hashMap2["name"] = name
                hashMap2["postId"]= groupItem.postId
                hashMap2["groupId"]=groupItem.groupId
                hashMap2["id"] =messageId
                hashMap2["timestamp"] = chat?.timestamp
                hashMap2["isseen"] = false

                chatRef.document("recentMessage").set(hashMap2)

            }
        }

    }
}


                //            ChatFragment.chatKey?.let { chatreference.collection("Chats")
    //                .document("UserChats").collection(it).add(hashMap) }


                /*
                    final DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("Chatlist")
                            .child(fuser.getUid())
                            .child(userid);

                    chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (!dataSnapshot.exists()) {
                                if (!fuser.getUid().equals(userid)) {
                                    chatRef.child("id").setValue(userid);

                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                     */
    //            val chatRefReceiver = userid?.let {
    //                FirebaseDatabase.getInstance().getReference("Chatlist")
    //                    .child(it)
    //                    .child(fuser.getUid())
    //            }
    //            if (userid != fuser.getUid()) {
    //                if (chatRefReceiver != null) {
    //                    chatRefReceiver.child("id").setValue(true)
    //                }
    //            }
    //            val chatRef = userid?.let {
    //                FirebaseDatabase.getInstance().getReference("Chatlist")
    //                    .child(fuser.getUid())
    //                    .child(it)
    //            }
    //            if (fuser.getUid() != userid) {
    //                if (chatRef != null) {
    //                    chatRef.child("id").setValue(true)
    //                }
    //            }





