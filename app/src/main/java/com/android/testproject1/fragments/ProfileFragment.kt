package com.android.testproject1.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.android.testproject1.R
import com.android.testproject1.adapter.ViewPager2Adapter
import com.android.testproject1.anim.DepthPageTransform
import com.android.testproject1.databinding.FragmentProfileBinding
import com.android.testproject1.model.Users
import com.google.android.material.tabs.TabLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class ProfileFragment : Fragment() {

    private lateinit var binding:FragmentProfileBinding
    private lateinit var adapter:ViewPager2Adapter
    private lateinit var firebaseFirestore: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding= FragmentProfileBinding.inflate(inflater,container,false)

//        requireActivity().bottomNav.menu.getItem(4).isChecked=true

        val fmc: FragmentManager = childFragmentManager
        firebaseFirestore= FirebaseFirestore.getInstance()
        firebaseAuth= FirebaseAuth.getInstance()

        val userItem=firebaseAuth.currentUser?.uid
//        val toolbar = binding.toolbarProfile
//        (activity as AppCompatActivity).setSupportActionBar(toolbar)
//        (activity as AppCompatActivity).supportActionBar?.title = "Profile"
//        setHasOptionsMenu(true)

        adapter = ViewPager2Adapter(fmc, viewLifecycleOwner.lifecycle)
        binding.pager.adapter = adapter
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Posts"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Saved"));
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

        if (userItem != null) {
            firebaseFirestore
                .collection("Users")
                .document(userItem)
                .get().addOnSuccessListener {
                    binding.name.text=it.getString("name")
                }

        }


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

        return binding.root
    }

}