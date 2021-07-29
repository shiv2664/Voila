package com.android.testproject1.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.testproject1.R
import com.android.testproject1.databinding.FragmentSearchBinding
import com.android.testproject1.databinding.FragmentSearchPeopleBinding

class SearchPeople : Fragment() {

    private lateinit var binding: FragmentSearchPeopleBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentSearchPeopleBinding.inflate(inflater,container,false)





        return binding.root
    }

}