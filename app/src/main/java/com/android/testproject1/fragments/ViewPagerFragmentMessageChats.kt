package com.android.testproject1.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.android.testproject1.R
import com.android.testproject1.databinding.FragmentViewPagerMessageChatsBinding
import com.android.testproject1.model.DataProvider
import com.android.testproject1.viewmodels.HomeFragmentViewModel
import com.android.testproject1.viewmodels.ViewPagerFragmentChatViewModel

class ViewPagerFragmentMessageChats : Fragment() {

    private lateinit var binding:FragmentViewPagerMessageChatsBinding
    private lateinit var mViewModel:ViewPagerFragmentChatViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentViewPagerMessageChatsBinding.inflate(inflater,container,false)

        mViewModel = ViewModelProvider(
            this, ViewModelProvider.AndroidViewModelFactory
                .getInstance(requireActivity().application)
        ).get(ViewPagerFragmentChatViewModel::class.java)

        mViewModel.loadUserChatList()

        mViewModel.getUserChatsList()?.observe(viewLifecycleOwner, {
            binding.userChatList = it
        })




        return binding.root
    }

}