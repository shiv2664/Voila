package com.android.testproject1.fragments

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.drawable.Drawable
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
import com.airbnb.lottie.LottieAnimationView
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
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.item_discover.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ProfileFragment : Fragment() {

    private lateinit var binding:FragmentProfileBinding
    private lateinit var adapter:ViewPager2Adapter
    private lateinit var firebaseFirestore: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var mViewModel: ViewPagerFragmentPostViewModel

    private lateinit var sharedPrefDynamic: SharedPreferences
    private lateinit var userId:String


    companion object{
        var userIdOpened:String=""
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding= FragmentProfileBinding.inflate(inflater,container,false)

//        requireActivity().bottomNav.menu.getItem(4).isChecked=true

        val offerItem=arguments?.getParcelable<OfferRoomEntity>("offerItem")
        val userIdBundle=arguments?.getString("userId")
        if (offerItem!=null){

            userId= offerItem.userId
        }else if (userIdBundle!=null){
            userId= userIdBundle
        }

        val localDatabase: AppDatabase? = AppDatabase.getInstance(requireActivity())

        sharedPrefDynamic= activity?.getSharedPreferences(userId,Context.MODE_PRIVATE)!!
        val dynamicEditor= sharedPrefDynamic?.edit()
        var firstTimeCheck= sharedPrefDynamic?.getInt("firstTimeCheck",0)
        firstTimeCheck++
        dynamicEditor?.putInt("firstTimeCheck",firstTimeCheck)
        dynamicEditor?.apply()

        val fmc: FragmentManager = childFragmentManager
        firebaseFirestore= FirebaseFirestore.getInstance()
        firebaseAuth= FirebaseAuth.getInstance()
        val currentUserId=firebaseAuth.currentUser?.uid

        if (currentUserId==userId){

            binding.sendMessage.visibility=View.GONE
        }


        mViewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application))
            .get(ViewPagerFragmentPostViewModel::class.java)

        userIdOpened = userId


//        val toolbar = binding.toolbarProfile
//        (activity as AppCompatActivity).setSupportActionBar(toolbar)
//        (activity as AppCompatActivity).supportActionBar?.title = "Profile"
//        setHasOptionsMenu(true)

        if (firstTimeCheck!=1){

//            binding.name.text = sharedPref?.getString("userName","")
//            binding.email.text = sharedPref?.getString("userEmail","")
//            binding.bio.text = sharedPref?.getString("userBio","")
//            Glide.with(requireActivity())
//                .load(sharedPref?.getString("profileimage",""))
//                .into(binding.imageView)
            binding.name.text = sharedPrefDynamic?.getString("username","")
            binding.email.text = sharedPrefDynamic?.getString("userEmail","")
//            binding.bio.text = sharedPrefDynamic?.getString("userBio","")
            Glide.with(requireActivity())
                .load(sharedPrefDynamic?.getString("profileimage",""))
                .addListener(imageLoadingListener(binding.lottiViewProfile))
                .into(binding.profilepic)
        }

        val reference=FirebaseFirestore.getInstance()
        val fUser= userId
        reference.collection("Users").document(fUser).get().addOnSuccessListener {
            if (it != null) {

                if (it.exists()) {
                    // convert document to POJO

                    if (firstTimeCheck==1){

                        binding.name.text = it.getString("username")
                        binding.email.text = it.getString("email")
//                        binding.bio.text = it.getString("bio")
                        Glide.with(requireActivity())
                            .load(it.getString("profileimage"))
                            .addListener(imageLoadingListener(binding.lottiViewProfile))
                            .into(binding.profilepic)
                    }

                    val userImageRoomEntity: UserImagesRoomEntity? = it.toObject(UserImagesRoomEntity::class.java)
                    CoroutineScope(Dispatchers.IO).launch {
                        if (userImageRoomEntity != null) {
                            localDatabase?.appDao()?.insertImage(userImageRoomEntity)
                        }
                    }

                    dynamicEditor?.putString("username",it.getString("username"))
                    dynamicEditor?.putString("userEmail",it.getString("email"))
                    dynamicEditor?.putString("profileimage",it.getString("profileimage"))
                    dynamicEditor?.putString("userBio",it.getString("bio"))
                    dynamicEditor?.apply()

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
        bundle.putString("userId",userId)
        bundle.putString("chatsOpened","fromProfile")

        binding.sendMessage.setOnClickListener {

            val intent = Intent(activity,ChatActivity::class.java)
            intent.putExtra("userId",userId)
            intent.putExtra("chatsOpened","fromProfile")
            intent.putExtra("openChat","openUserChat")
//            intent.putExtra("name",offerItem.name)
            startActivity(intent)

//            findNavController().navigate(R.id.action_profileFragment_to_chatFragment,bundle)
        }



        return binding.root
    }

    private fun imageLoadingListener(pendingImage: LottieAnimationView): RequestListener<Drawable?>? {
        return object : RequestListener<Drawable?> {
            override fun onLoadFailed(e: GlideException?, model: Any?, target: com.bumptech.glide.request.target.Target<Drawable?>?, isFirstResource: Boolean): Boolean {
                return false
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: com.bumptech.glide.request.target.Target<Drawable?>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                pendingImage.pauseAnimation()
                pendingImage.visibility = View.GONE
                return false
            }
        }
    }

}