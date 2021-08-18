package com.android.testproject1.fragments

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.android.testproject1.R
import com.android.testproject1.databinding.FragmentSearchBinding
import com.android.testproject1.model.DataProvider
import com.android.testproject1.viewmodels.HomeFragmentViewModel
import com.android.testproject1.viewmodels.SearchFragmentViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_search.*

class SearchFragment : Fragment() {

    private lateinit var binding :FragmentSearchBinding
    private lateinit var mViewModel:SearchFragmentViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        context?.theme?.applyStyle(R.style.AppTheme, true);
        binding= FragmentSearchBinding.inflate(inflater,container,false)

        mViewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory
                .getInstance(requireActivity().application)
        ).get(SearchFragmentViewModel::class.java)

        mViewModel.loadNotes()

        mViewModel.getPostList().observe(viewLifecycleOwner, {
            binding.offerList = it
        })

        binding.productExploreList = DataProvider.productExploreList

        val toolbar = binding.toolbarSearch
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar?.setTitle(R.string.Explore)
        toolbar.inflateMenu(R.menu.menuhome)
        setHasOptionsMenu(true)


        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(com.android.testproject1.R.menu.menuhome,menu)
        super.onCreateOptionsMenu(menu, inflater)

    }


}