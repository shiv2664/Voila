package com.android.testproject1.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.android.testproject1.R
import com.android.testproject1.databinding.FragmentChatBinding
import com.android.testproject1.model.Users
import com.android.testproject1.room.enteties.UsersRoomEntity
import com.android.testproject1.viewmodels.ChatFragmentViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.HashMap
import java.util.zip.Inflater

class ChatFragment : Fragment() {

    private lateinit var binding:FragmentChatBinding
    private lateinit var chatreference: FirebaseFirestore
    private lateinit var userId:String
    private var name:String?=null
    private lateinit var currentUserId:String
    private lateinit var firebseauth: FirebaseAuth
    private lateinit var mViewModel:ChatFragmentViewModel
    private var checkChatList:Boolean=false

    companion object {
        var chatKey: String? = null
        var Key: String? = null
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding= FragmentChatBinding.inflate(inflater,container,false)

        val bundle = this.arguments
        val chatsOpened=bundle?.getString("chatsOpened")

        if (chatsOpened=="fromGroups"){
            val groupItem= bundle.getParcelable<Users>("userItem")
            Log.d("MyTag","userItem is : "+groupItem?.id)
            if (groupItem != null) {
                userId=groupItem.id
            }
        }else if (chatsOpened=="fromMessages"){
            val groupItem= bundle.getParcelable<UsersRoomEntity>("userItem")
            if (groupItem != null) {
                userId=groupItem.id
            }

        }

        firebseauth= FirebaseAuth.getInstance()
        currentUserId= firebseauth.currentUser?.uid.toString()
        chatreference= FirebaseFirestore.getInstance()

        val i = currentUserId.compareTo(userId)
        if (+i >= 1) {
            chatKey = userId + currentUserId
        } else if (+i < 1) {
            chatKey = currentUserId + userId
        } else if (+i == 0) {
            chatKey = ""
        }

        mViewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application))
            .get(ChatFragmentViewModel::class.java)

        mViewModel.getChatList().observe(viewLifecycleOwner, {
            binding.chatList = it
        })

        chatreference.collection("Users").document(currentUserId).get().addOnSuccessListener {
            if (it!=null){
                val notifPojo: Users? = it.toObject(Users::class.java)
                if (notifPojo != null) {
                    name=notifPojo.name
                }
            }
        }

        chatreference.collection("ChatList").document(currentUserId).get().addOnSuccessListener {

            val group = it.get("ChatList") as List<String>?
            if (group != null) {
                for (i in group){
                    if (i ==userId){
                        checkChatList=true
                    }
                }
            }

        }

        CoroutineScope(Dispatchers.IO).launch {
            chatKey?.let { mViewModel.loadChat(it) }
        }


        binding.btnSend.setOnClickListener {

            Log.d("MyTag","check chat list is  $checkChatList")
            val message=binding.textSend.text.toString().trim()
            if (message.isEmpty()){
                Toast.makeText(activity,"Empty Message Can't be sent",Toast.LENGTH_SHORT).show()
            }else{
                sendMessage(currentUserId,userId,message)
                binding.textSend.setText("")

                if (!checkChatList){
                    if (userId!=currentUserId) {
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

        return binding.root
    }


    private fun sendMessage(sender: String?, receiver: String?, message: String?) {
        if (sender == receiver) {
            Toast.makeText(activity, "Message Can't be sent", Toast.LENGTH_SHORT).show()
        } else {

            val chatRef = chatKey?.let { chatreference.collection("Chats").document(it).collection("UserChats") }

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
                chatRef.document(messageID).set(hashMap, SetOptions.merge())
            }
            Log.d("MyTag","chatRef Doc Id : "+messageID)

        }
    }




}