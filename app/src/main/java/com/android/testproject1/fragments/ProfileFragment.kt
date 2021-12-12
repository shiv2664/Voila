package com.android.testproject1.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.android.testproject1.ChatActivity
import com.android.testproject1.MainActivity2
import com.android.testproject1.R
import com.android.testproject1.adapter.ViewPager2Adapter
import com.android.testproject1.anim.DepthPageTransform
import com.android.testproject1.databinding.FragmentProfileBinding
import com.android.testproject1.model.Offer
import com.android.testproject1.model.Users
import com.android.testproject1.room.enteties.AppDatabase
import com.android.testproject1.room.enteties.OfferRoomEntity
import com.android.testproject1.room.enteties.UserImagesRoomEntity
import com.android.testproject1.viewmodels.ViewPagerFragmentPostViewModel
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ProfileFragment : Fragment() {

    private lateinit var binding:FragmentProfileBinding
    private lateinit var adapter:ViewPager2Adapter
    private lateinit var firebaseFirestore: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var mViewModel: ViewPagerFragmentPostViewModel


    companion object{
        var userIdOpened:String=""
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding= FragmentProfileBinding.inflate(inflater,container,false)

//        requireActivity().bottomNav.menu.getItem(4).isChecked=true

        val offerItem=arguments?.getParcelable<OfferRoomEntity>("offerItem")

       val localDatabase: AppDatabase? = AppDatabase.getInstance(requireActivity())

        val fmc: FragmentManager = childFragmentManager
        firebaseFirestore= FirebaseFirestore.getInstance()
        firebaseAuth= FirebaseAuth.getInstance()
        val currentUserId=firebaseAuth.currentUser?.uid

        if (offerItem != null) {
            if (currentUserId==offerItem.userId){

                binding.sendMessage.visibility=View.GONE
            }
        }


        mViewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application))
            .get(ViewPagerFragmentPostViewModel::class.java)

        if (offerItem != null) {
            userIdOpened = offerItem.userId
        }


//        val toolbar = binding.toolbarProfile
//        (activity as AppCompatActivity).setSupportActionBar(toolbar)
//        (activity as AppCompatActivity).supportActionBar?.title = "Profile"
//        setHasOptionsMenu(true)

        if (offerItem != null) {
            binding.name.text = offerItem.name
        }

        val reference=FirebaseFirestore.getInstance()
//        val fUser=FirebaseAuth.getInstance().currentUser?.uid
        val fUser= offerItem?.userId
        reference.collection("Users").document(fUser.toString()).get().addOnSuccessListener {
            if (it != null) {

                if (it.exists()) {
                    // convert document to POJO
                    val notifPojo: Users? = it.toObject(Users::class.java)

                    if (notifPojo != null) {
                        if (notifPojo.profileimage.isNotEmpty()) {
                            activity?.let { it1 -> Glide.with(it1).load(notifPojo.profileimage).into(binding.profilepic) }
                            binding.name.text = notifPojo.name
                            binding.email.text = notifPojo.email

                            val userImageRoomEntity: UserImagesRoomEntity? = it.toObject(UserImagesRoomEntity::class.java)
                            CoroutineScope(Dispatchers.IO).launch {
                                if (userImageRoomEntity != null) {
                                    localDatabase?.appDao()?.insertImage(userImageRoomEntity)
                                }
                            }

                        }
                        Log.d("MyTag"," Image Url is "+notifPojo.profileimage)
                    }
                }
            }

        }

        adapter = ViewPager2Adapter(fmc, viewLifecycleOwner.lifecycle)
        binding.pager.adapter = adapter
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Offers"));
//        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Saved"));
        binding.pager.setPageTransformer(DepthPageTransform())

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                binding.pager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        binding.pager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                binding.tabLayout.selectTab(binding.tabLayout.getTabAt(position))
            }
        })

//        if (userItem != null) {
//            firebaseFirestore
//                .collection("Users")
//                .document(userItem)
//                .get().addOnSuccessListener {
//                    binding.name.text=it.getString("name")
//                }
//
//        }



        binding.editProfileBtn.setOnClickListener {

            findNavController().navigate(R.id.action_profileFragment_to_editProfileFragment)

//            val fragment:Fragment=EditProfileFragment()
//            requireActivity()
//                .supportFragmentManager
//                .beginTransaction()
//                .addToBackStack("Message Fragment")
//                .setCustomAnimations(R.anim.right_enter,R.anim.left_out)
//                .replace(R.id.container, fragment)
//                .commit()

        }

        val bundle = Bundle()
        if (offerItem != null) {
            bundle.putString("userId",offerItem.userId)
            bundle.putString("chatsOpened","fromProfile")
        }

        binding.sendMessage.setOnClickListener {

            val intent = Intent(activity,ChatActivity::class.java)
            if (offerItem != null) {
                intent.putExtra("userId",offerItem.userId)
                intent.putExtra("chatsOpened","fromProfile")
                intent.putExtra("openChat","openUserChat")
                intent.putExtra("name",offerItem.name)
                startActivity(intent)
            }

//            findNavController().navigate(R.id.action_profileFragment_to_chatFragment,bundle)
        }



        return binding.root
    }

}