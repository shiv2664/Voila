package com.android.testproject1.fragments

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.android.testproject1.R
import com.android.testproject1.databinding.FragmentSearchBinding
import com.android.testproject1.model.DataProvider
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_search.*

class SearchFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        context?.theme?.applyStyle(R.style.AppTheme, true);


        val binding= FragmentSearchBinding.inflate(inflater,container,false)
        binding.productExploreList = DataProvider.productExploreList

//        requireActivity().bottomNav.menu.getItem(1).isChecked=true

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