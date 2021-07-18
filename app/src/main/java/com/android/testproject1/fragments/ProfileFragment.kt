package com.android.testproject1.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.widget.ViewPager2
import com.android.testproject1.R
import com.android.testproject1.adapter.ViewPager2Adapter
import com.android.testproject1.anim.DepthPageTransform
import com.android.testproject1.databinding.FragmentProfileBinding
import com.android.testproject1.databindingadapters.bind
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_main.*

class ProfileFragment : Fragment() {

    private lateinit var binding:FragmentProfileBinding
   private lateinit var adapter:ViewPager2Adapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding= FragmentProfileBinding.inflate(inflater,container,false)

//        requireActivity().bottomNav.menu.getItem(4).isChecked=true

        val fmc: FragmentManager = childFragmentManager

//        val toolbar = binding.toolbarProfile
//        (activity as AppCompatActivity).setSupportActionBar(toolbar)
//        (activity as AppCompatActivity).supportActionBar?.title = "Profile"
//        setHasOptionsMenu(true)

        adapter = ViewPager2Adapter(fmc, viewLifecycleOwner.lifecycle)
        binding.pager.adapter = adapter
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Posts"));
        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("User Info"));
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


        binding.editProfileBtn.setOnClickListener {

            val fragment:Fragment=EditProfileFragment()
            requireActivity()
                .supportFragmentManager
                .beginTransaction()
                .addToBackStack("Message Fragment")
                .setCustomAnimations(R.anim.right_enter,R.anim.left_out)
                .replace(R.id.container, fragment)
                .commit()

        }

        return binding.root
    }

}