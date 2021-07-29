package com.android.testproject1.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.android.testproject1.R
import com.android.testproject1.Repository
import com.android.testproject1.databinding.FragmentGroupsOnOfferBinding
import com.android.testproject1.model.Offer
import com.android.testproject1.model.Post
import com.android.testproject1.model.Users
import com.android.testproject1.viewmodels.DetailsFragmentViewModel
import com.android.testproject1.viewmodels.GroupsOnFragmentViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import java.util.HashMap

class GroupsOnOfferFragment : Fragment() {

    private lateinit var binding:FragmentGroupsOnOfferBinding
    private lateinit var mViewModel:GroupsOnFragmentViewModel

    private lateinit var firebaseFirestore: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth

    private var myTag:String="MyTag"
    private var currentUserId: String?=null
    private lateinit var postItem : Offer
    companion object{
        var postId:String?=null
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentGroupsOnOfferBinding.inflate(inflater,container,false)

        val bundle = this.arguments
        postItem= bundle?.getParcelable<Offer>("OfferItem")!!
//        setUrls(binding,postItem)

        firebaseFirestore= FirebaseFirestore.getInstance()
        firebaseAuth= FirebaseAuth.getInstance()
        currentUserId = firebaseAuth.currentUser?.uid

        postId =postItem.postId

        mViewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory
            .getInstance(requireActivity().application))
            .get(GroupsOnFragmentViewModel::class.java)

        mViewModel.loadGroupsOnPost(postId!!)

        mViewModel.getUserList().observe(viewLifecycleOwner, {
            binding.userList=it

        })

        binding.extendedFab.setOnClickListener {
//            val postUserId: String? = postItem?.userId
            val postId: String = postItem.postId

            val postMap: MutableMap<String, Any?> = HashMap()
            val postIdMap: MutableMap<String, Any?> = HashMap()

            postIdMap["postId"]=postId

            if (currentUserId!=null) {

                postMap["createdBy"] = currentUserId
                postMap["timestamp"] = System.currentTimeMillis()
                Log.d(myTag, "$postId   $currentUserId")


                firebaseFirestore.collection("Offers").document(postId)
                    .collection("Groups").document(currentUserId!!).set(postMap, SetOptions.merge()).addOnSuccessListener {

                        firebaseFirestore.collection("Posts").document(postId)
                            .collection("Groups").document(currentUserId!!)
                            .update("Members", FieldValue.arrayUnion("$currentUserId"))

                    }
            }

        }

//        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
//            override fun handleOnBackPressed() {
//
//                activity!!.onBackPressed()
//
//
//                // in here you can do logic when backPress is clicked
//            }
//        })


        return binding.root
    }


}