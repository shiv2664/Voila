package com.android.testproject1.fragments


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.AbsListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.testproject1.R
import com.android.testproject1.RegisterActivity
import com.android.testproject1.adapter.PostsAdapter2
import com.android.testproject1.databinding.FragmentHomeBinding
import com.android.testproject1.databindingadapters.bind
import com.android.testproject1.model.DataProvider
import com.android.testproject1.viewmodels.HomeFragmentViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_home.*
import java.util.*


class HomeFragment : Fragment() {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var binding: FragmentHomeBinding

    private lateinit var scrollListener: RecyclerView.OnScrollListener



    var isScrolling = false
    var currentItems = 0
    var totalItems:Int = 0
    var scrollOutItems:Int = 0

    private lateinit var mViewModel: HomeFragmentViewModel
    var myTag: String = "MyTag"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        context?.theme?.applyStyle(R.style.AppTheme, true);
        binding = FragmentHomeBinding.inflate(inflater, container, false)


        firebaseAuth = FirebaseAuth.getInstance()
        mViewModel = ViewModelProvider(
            this, ViewModelProvider.AndroidViewModelFactory
                .getInstance(requireActivity().application)
        ).get(HomeFragmentViewModel::class.java)

        mViewModel.loadDataPost()

        binding.swipeRefresh.setOnRefreshListener {
            mViewModel.loadDataPost()
            binding.swipeRefresh.isRefreshing=false

        }

        binding.dataList2 = DataProvider.productList

        mViewModel.getPostList().observe(viewLifecycleOwner, {
            binding.dataList = it
        })

//        val toolbar = binding.toolbarHome
//        (activity as AppCompatActivity).setSupportActionBar(toolbar)
//        (activity as AppCompatActivity).supportActionBar?.title = "Home"
//        setHasOptionsMenu(true)


        binding.rvPosts.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                currentItems = rv_Posts.layoutManager?.childCount!!
                totalItems = rv_Posts.layoutManager?.itemCount!!
                scrollOutItems = (rv_Posts.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()

                Log.d(myTag," currentItems : $currentItems  totalItems : $totalItems scrollOutItems :$scrollOutItems")

//                isScrolling &&
                if (isScrolling && currentItems + scrollOutItems == totalItems) {
                    isScrolling = false
                    mViewModel.loadDataPost()
//                    getData()

                }
            }
        })

        return binding.root
    }


}