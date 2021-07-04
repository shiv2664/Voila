package com.android.testproject1.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.android.testproject1.R
import com.android.testproject1.databinding.FragmentViewpagerPostsBinding
import com.android.testproject1.databindingadapters.bind
import com.android.testproject1.viewmodels.ChatFragmentViewModel
import com.android.testproject1.viewmodels.ViewPagerFragmentPostViewModel

class ViewpagerFragmentPosts : Fragment() {

    private lateinit var binding:FragmentViewpagerPostsBinding
    private lateinit var mViewModel:ViewPagerFragmentPostViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {
        binding= FragmentViewpagerPostsBinding.inflate(inflater,container,false)

        mViewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application))
            .get(ViewPagerFragmentPostViewModel::class.java)

        mViewModel.userPosts()

        mViewModel.getUserPostList().observe(viewLifecycleOwner, {
            binding.dataList = it

        })




        return binding.root
    }

}