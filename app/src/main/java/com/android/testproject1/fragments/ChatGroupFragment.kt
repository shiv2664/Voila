package com.android.testproject1.fragments

import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.android.testproject1.R
import com.android.testproject1.databinding.FragmentChatBinding
import com.android.testproject1.databinding.FragmentChatGroupBinding
import com.android.testproject1.model.Users
import com.android.testproject1.viewmodels.ChatFragmentViewModel
import com.android.testproject1.viewmodels.GroupChatFragmentViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_chat.*
import java.util.HashMap

class ChatGroupFragment : Fragment() {

    private lateinit var binding:FragmentChatGroupBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var mViewModel:GroupChatFragmentViewModel
    private var currentUserId:String?=null
    var groupId:String?=null
    private var name:String?=null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        binding=FragmentChatGroupBinding.inflate(inflater,container,false)

//        val toolbar = binding.myToolbar
//        (activity as AppCompatActivity).setSupportActionBar(toolbar)
//        (activity as AppCompatActivity).supportActionBar?.title = "Enjoyah"
//        setHasOptionsMenu(true)

        auth= FirebaseAuth.getInstance()
        firestore= FirebaseFirestore.getInstance()
        currentUserId=auth.currentUser?.uid


        val bundle = this.arguments
        val groupItem= bundle?.getParcelable<Users>("userItem")
        if (groupItem != null) {
            groupId=groupItem.id
        }

        mViewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory
            .getInstance(requireActivity().application)).get(GroupChatFragmentViewModel::class.java)

        DetailsFragment.postId?.let { groupId?.let { it1 -> mViewModel.loadGroupChat(it, it1) } }

        mViewModel.getGroupChatList().observe(viewLifecycleOwner, {
            binding.groupChatList = it

        })

        currentUserId?.let { it1 ->
            firestore.collection("Users").document(it1).get().addOnSuccessListener {
                if (it!=null){
                    val notifPojo: Users? = it.toObject(Users::class.java)
                    if (notifPojo != null) {
                        name=notifPojo.name
                    }
                }
            }
        }

        binding.btnSend2.setOnClickListener {

            val message=binding.textSend2.text.toString().trim()

            Log.d("MyTag","message is  $message" )

            if (message.isEmpty()){
                Toast.makeText(activity,"Empty Message Can't be sent",Toast.LENGTH_SHORT).show()
            }else{
                groupId?.let { it1 -> sendMessage(currentUserId,message, it1) }
                binding.textSend2.setText("")
            }

        }

        return binding.root
    }

    private fun sendMessage(sender: String?, message: String?,groupId:String,receiver:String="") {

        val hashMap = HashMap<String?, Any?>()
        hashMap["sender"] = sender
        hashMap["receiver"] = receiver
        hashMap["message"] = message
        hashMap["name"] = name
        hashMap["timestamp"] = FieldValue.serverTimestamp()
        hashMap["isseen"] = false

        DetailsFragment.postId?.let { firestore.collection("Posts").document(it)
            .collection("Groups").document(groupId).collection("Chats").add(hashMap) }



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



    }

}