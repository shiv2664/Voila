package com.android.testproject1.fragments


import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.android.testproject1.MainActivity
import com.android.testproject1.R
import com.android.testproject1.RegisterActivity
import com.android.testproject1.adapter.PostsAdapter2
import com.android.testproject1.databinding.FragmentHomeBinding
import com.android.testproject1.model.DataProvider
import com.android.testproject1.viewmodels.HomeFragmentViewModel
import com.google.firebase.auth.FirebaseAuth
import com.nguyenhoanglam.imagepicker.model.Image
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class HomeFragment : Fragment() {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var binding: FragmentHomeBinding


    private lateinit var mViewModel: HomeFragmentViewModel
    private lateinit var postAdapter2: PostsAdapter2
    var myTag: String = "MyTag"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        context?.theme?.applyStyle(R.style.AppTheme, true);
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        requireActivity().bottomNav.menu.getItem(0).isChecked=true

        firebaseAuth = FirebaseAuth.getInstance()
        mViewModel = ViewModelProvider(
            this, ViewModelProvider.AndroidViewModelFactory
                .getInstance(requireActivity().application)
        ).get(HomeFragmentViewModel::class.java)

        mViewModel.loadDataPost()



        binding.dataList2 = DataProvider.productList

        mViewModel.getPostList().observe(viewLifecycleOwner, {
            binding.dataList = it
        })


        val toolbar = binding.toolbarHome
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar?.title = "Home"
        setHasOptionsMenu(true)

        return binding.root

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menuhome, menu)
        super.onCreateOptionsMenu(menu, inflater)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.SignOut) {
            firebaseAuth.signOut()
            activity?.finish()
            val intent = Intent(activity, RegisterActivity::class.java)
            startActivity(intent)
            Toast.makeText(activity, "Sign Out", Toast.LENGTH_SHORT).show()
        }

        return super.onOptionsItemSelected(item)
    }

}