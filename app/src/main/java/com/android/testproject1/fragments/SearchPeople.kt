package com.android.testproject1.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.testproject1.databinding.FragmentSearchPeopleBinding
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider

import com.android.testproject1.viewmodels.SearchPeopleViewModel


class SearchPeople : Fragment() {

    private lateinit var binding: FragmentSearchPeopleBinding
    private lateinit var mViewModel: SearchPeopleViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentSearchPeopleBinding.inflate(inflater,container,false)



        mViewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory
            .getInstance(requireActivity().application))
            .get(SearchPeopleViewModel::class.java)

        mViewModel.getUserList().observe(viewLifecycleOwner, {
            binding.userList = it

        })

        binding.FindPeopleSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {

                if (query != null) {
                    mViewModel.setNull()
                    mViewModel.loadUsers(query)

                }
//                if (list.contains(query)) {
//                    adapter.getFilter().filter(query)
//                } else {
//                    Toast.makeText(this@MainActivity, "No Match found", Toast.LENGTH_LONG).show()
//                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
//                if (newText != null) {
//                    mViewModel.loadUsers(newText)
//                }
                return false
            }
        })




        return binding.root
    }

}