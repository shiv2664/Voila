package com.android.testproject1.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.testproject1.R
import com.android.testproject1.databinding.FragmentOrderHistoryBinding
import com.android.testproject1.databindingadapters.bind
import com.android.testproject1.viewmodels.HomeFragmentViewModel
import com.android.testproject1.viewmodels.OrderHistoryFragmentViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class OrderHistoryFragment : Fragment() {

    private lateinit var binding:FragmentOrderHistoryBinding
    private lateinit var mViewModel: OrderHistoryFragmentViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentOrderHistoryBinding.inflate(inflater,container,false)

        mViewModel = ViewModelProvider(
            this, ViewModelProvider.AndroidViewModelFactory
                .getInstance(requireActivity().application)
        ).get(OrderHistoryFragmentViewModel::class.java)

        CoroutineScope(Dispatchers.IO).launch {
            mViewModel.loadOrders()
            mViewModel.loadRecentOrder()
        }

        mViewModel.getNotificationList()?.observe(viewLifecycleOwner, {
            binding.dataList = it
        })

        return binding.root
    }

}