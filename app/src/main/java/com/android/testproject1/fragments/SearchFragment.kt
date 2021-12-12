package com.android.testproject1.fragments

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.AbsListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.testproject1.R
import com.android.testproject1.databinding.FragmentSearchBinding
import com.android.testproject1.model.DataProvider
import com.android.testproject1.viewmodels.SearchFragmentViewModel
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_search.*

class SearchFragment : Fragment() {

    private lateinit var binding :FragmentSearchBinding
    private lateinit var mViewModel:SearchFragmentViewModel
    val myTag="MyTag"

    var isScrolling = false
    var currentItems = 0
    var totalItems:Int = 0
    var scrollOutItems:Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

//        context?.theme?.applyStyle(R.style.AppTheme, true);
        binding= FragmentSearchBinding.inflate(inflater,container,false)

        mViewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory
                .getInstance(requireActivity().application)
        ).get(SearchFragmentViewModel::class.java)

        mViewModel.loadOffers()
        mViewModel.loadRecentOffer()

        mViewModel.getPostList()?.observe(viewLifecycleOwner, {
            binding.offerList = it
        })

        binding.productExploreList = DataProvider.productExploreList

//        val toolbar = binding.toolbarSearch
//        (activity as AppCompatActivity).setSupportActionBar(toolbar)
//        (activity as AppCompatActivity).supportActionBar?.setTitle(R.string.Explore)
//        toolbar.inflateMenu(R.menu.menuhome)
//        setHasOptionsMenu(true)

        binding.DiscoverRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                currentItems = DiscoverRecyclerView.layoutManager?.childCount!!
                totalItems = DiscoverRecyclerView.layoutManager?.itemCount!!
                scrollOutItems = (DiscoverRecyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()

                Log.d(myTag," currentItems : $currentItems  totalItems : $totalItems scrollOutItems :$scrollOutItems")

//                isScrolling &&
                if (isScrolling && currentItems + scrollOutItems == totalItems) {
                    isScrolling = false
                    mViewModel.loadOffers()

                }
            }
        })


        return binding.root
    }



//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        inflater.inflate(com.android.testproject1.R.menu.menuhome,menu)
//        super.onCreateOptionsMenu(menu, inflater)
//
//    }




}