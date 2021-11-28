package com.android.testproject1.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.testproject1.R
import com.android.testproject1.databinding.FragmentViewPagerCurrentUserPostsBinding
import com.android.testproject1.databindingadapters.bind
import com.android.testproject1.viewmodels.ViewPagerFragmentCurrentUserPostsViewModel
import com.android.testproject1.viewmodels.ViewPagerFragmentPostViewModel

class ViewPagerFragmentCurrentUserPosts : Fragment() {

    private lateinit var binding:FragmentViewPagerCurrentUserPostsBinding
    private lateinit var mViewModel:ViewPagerFragmentCurrentUserPostsViewModel

    var myTag: String = "MyTag"

    var isScrolling = false
    var currentItems = 0
    var totalItems:Int = 0
    var scrollOutItems:Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding= FragmentViewPagerCurrentUserPostsBinding.inflate(inflater,container,false)

        mViewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application))
            .get(ViewPagerFragmentCurrentUserPostsViewModel::class.java)

        mViewModel.loadUserPosts()

        mViewModel.getUserPostList()?.observe(viewLifecycleOwner, {
            binding.dataList = it
            Log.d(myTag,"data list size is "+it.size)

        })

        binding.postRecyclerview.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                currentItems = binding.postRecyclerview.layoutManager?.childCount!!
                totalItems = binding.postRecyclerview.layoutManager?.itemCount!!
                scrollOutItems = (binding.postRecyclerview.layoutManager as GridLayoutManager).findFirstVisibleItemPosition()

                Log.d(myTag, " currentItems : $currentItems  totalItems : $totalItems scrollOutItems :$scrollOutItems"
                )

//                isScrolling &&
                if (isScrolling && currentItems + scrollOutItems == totalItems) {
                    isScrolling = false
                    mViewModel.loadUserPosts()
//                    getData()

                }
            }
        })



        return binding.root
    }

}