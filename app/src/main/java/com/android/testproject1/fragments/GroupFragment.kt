package com.android.testproject1.fragments

import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.android.testproject1.ChatActivity
import com.android.testproject1.PaymentActivity
import com.android.testproject1.R
import com.android.testproject1.databinding.FragmentDetailsBinding
import com.android.testproject1.databinding.FragmentGroupBinding
import com.android.testproject1.model.GroupFragmentViewModel
import com.android.testproject1.model.Post
import com.android.testproject1.model.Users
import com.android.testproject1.room.enteties.UsersChatListEntity
import com.android.testproject1.viewmodels.DetailsFragmentViewModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_group.*


class GroupFragment : Fragment() {


    private lateinit var mViewModel: GroupFragmentViewModel
    private lateinit var firestore: FirebaseFirestore

     private lateinit var groupItem:UsersChatListEntity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        val binding= FragmentGroupBinding.inflate(inflater, container, false)

        mViewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory
            .getInstance(requireActivity().application))
            .get(GroupFragmentViewModel::class.java)

        firestore= FirebaseFirestore.getInstance()

//        val toolbar = binding.toolbarHome
//        (activity as AppCompatActivity).setSupportActionBar(toolbar)
//        (activity as AppCompatActivity).supportActionBar?.title = "Group"
//        setHasOptionsMenu(true)

        val bundle = this.arguments
        groupItem = bundle?.getParcelable<UsersChatListEntity>("userItem")!!


        groupItem.postId.let { mViewModel.loadGroupMembers(groupItem.createdBy, it) }

        mViewModel.getGroupList().observe(viewLifecycleOwner) {
            binding.userList = it
        }

        binding.extendedFabGroup.setOnClickListener {

            val intent=Intent(activity,ChatActivity::class.java)
            intent.putExtra("groupItem",groupItem)
            intent.putExtra("openChat","openGroupChat")
            startActivity(intent)

//            val fragment=ChatGroupFragment()
//            val bundle = Bundle()
//            bundle.putParcelable("userItem",groupItem)
//            fragment.arguments=bundle
//
//            requireActivity().
//            supportFragmentManager
//                .beginTransaction()
//                .addToBackStack("Groups Fragment")
//                .replace(R.id.container, fragment)
//                .commit()
        }

        binding.checkoutButton.setOnClickListener {
            val intent =Intent(requireActivity(),PaymentActivity::class.java)
            intent.putExtra("groupItem",groupItem)
            intent.putExtra("openChat","openGroupChat")
            startActivity(intent)
        }



        return binding.root
    }

}