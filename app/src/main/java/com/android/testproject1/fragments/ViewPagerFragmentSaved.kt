package com.android.testproject1.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.android.testproject1.databinding.FragmentViewpagerSavedBinding
import com.android.testproject1.viewmodels.SavedFragmentViewModel


class ViewPagerFragmentSaved : Fragment() {

    private lateinit var binding:FragmentViewpagerSavedBinding
    private lateinit var mViewModel:SavedFragmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentViewpagerSavedBinding.inflate(inflater,container,false)

        mViewModel = ViewModelProvider(viewModelStore, ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application))
            .get(SavedFragmentViewModel::class.java)

        mViewModel.loadSavedPosts()


        mViewModel.getSavedOffers()?.observe(viewLifecycleOwner, {
                binding.savedOfferList = it

        })

        return binding.root
    }

}