package com.android.testproject1.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.android.testproject1.R
import com.android.testproject1.adapter.MessageViewPager2Adapter
import com.android.testproject1.adapter.ViewPager2Adapter
import com.android.testproject1.anim.DepthPageTransform
import com.android.testproject1.databinding.FragmentMessagesBinding
import com.android.testproject1.viewmodels.ViewPagerFragmentChatViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_messages.*
import java.util.*


class MessagesFragment : Fragment() {

    private lateinit var binding: FragmentMessagesBinding
    private lateinit var adapter:MessageViewPager2Adapter
    private lateinit var mViewModel:ViewPagerFragmentChatViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View {
//        context?.theme?.applyStyle(R.style.AppTheme, true);
        binding = FragmentMessagesBinding.inflate(inflater, container, false)


        mViewModel = ViewModelProvider(
            this, ViewModelProvider.AndroidViewModelFactory
                .getInstance(requireActivity().application)
        ).get(ViewPagerFragmentChatViewModel::class.java)
        mViewModel.loadUserGroups()

        mViewModel.loadUserChatList()

        mViewModel.getUserChatsList()?.observe(viewLifecycleOwner, {
            binding.userChatList = it
        })

        return binding.root
    }


}



//        val toolbar = binding.toolbarMessage
//        (activity as AppCompatActivity).setSupportActionBar(toolbar)
//        (activity as AppCompatActivity).supportActionBar?.title = "Messages"
//        setHasOptionsMenu(true)
//
//        val fmc: FragmentManager = childFragmentManager

//        val fm: FragmentManager? = activity?.supportFragmentManager
//        adapter = fm?.let { ViewPager2Adapter(it, viewLifecycleOwner.lifecycle) }!!


//        adapter = MessageViewPager2Adapter(fmc, viewLifecycleOwner.lifecycle)
//        binding.MessageViewPager.adapter = adapter
//        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Chats"));
//        binding.tabLayout.addTab(binding.tabLayout.newTab().setText("Groups"));
//        binding.MessageViewPager.setPageTransformer(DepthPageTransform())
//
//        binding.tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
//            override fun onTabSelected(tab: TabLayout.Tab) {
//                binding.MessageViewPager.currentItem = tab.position
//            }
//
//            override fun onTabUnselected(tab: TabLayout.Tab) {}
//            override fun onTabReselected(tab: TabLayout.Tab) {}
//        })
//
//        binding.MessageViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
//            override fun onPageSelected(position: Int) {
//                binding.tabLayout.selectTab(binding.tabLayout.getTabAt(position))
//            }
//        })