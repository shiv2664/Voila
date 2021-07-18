package com.android.testproject1.fragments

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.android.testproject1.R
import com.android.testproject1.databinding.FragmentDashboardBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomnavigation.BottomNavigationView.OnNavigationItemReselectedListener
import com.google.firebase.auth.FirebaseAuth
import com.marcoscg.dialogsheet.DialogSheet
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_main2_content.*

class Dashboard : Fragment(), BottomNavigationView.OnNavigationItemSelectedListener,
    OnNavigationItemReselectedListener {

    private lateinit var binding:FragmentDashboardBinding


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding= FragmentDashboardBinding.inflate(inflater,container,false)

        val bundle = this.arguments
        val navSelected= bundle?.getString("bottom_nav","0")

        val bottomNavigationView= binding.bottomNav
        bottomNavigationView.setOnNavigationItemSelectedListener(this)
        bottomNavigationView.setOnNavigationItemReselectedListener(this)

        if (navSelected=="1"){
            loadfragment(SearchFragment())
            binding.bottomNav.selectedItemId= R.id.action_discover
        }else{
            loadfragment(HomeFragment())
            binding.bottomNav.selectedItemId= R.id.action_home
        }


        return binding.root
    }


    fun loadfragment(fragment: Fragment?) {
        (activity as AppCompatActivity).supportFragmentManager
            .beginTransaction()
            .replace(R.id.frame_container, fragment!!)
            .commit()
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_home -> {
                loadfragment(HomeFragment())
                activity?.toolbar?.title ="Home"
            }
            R.id.action_discover -> {
//                add_post.setVisible(false)
                loadfragment(SearchFragment())
                activity?.toolbar?.title ="Discover"
            }
        }
        return true
    }

    override fun onNavigationItemReselected(item: MenuItem) {
        when (item.itemId) {
            R.id.action_home -> {}
            R.id.action_discover -> {}
        }
    }

    fun showDialog() {
        DialogSheet(requireView().context)
            .setTitle("Information")
            .setMessage("Email has not been verified, please verify and continue. If you have verified we recommend you to logout and login again")
            .setPositiveButton(
                "Send again"
            ) { v: View? ->
                FirebaseAuth.getInstance().currentUser.sendEmailVerification()
                    .addOnSuccessListener { aVoid: Void? ->
                        Toasty.success(
                            requireView().context,
                            "Verification email sent",
                            Toasty.LENGTH_SHORT,
                            true
                        ).show()
                    }
                    .addOnFailureListener { e: Exception ->
                        Log.e(
                            "Error",
                            e.message!!
                        )
                    }
            }
            .setNegativeButton("Ok") { v: View? -> }
            .setCancelable(true)
            .setRoundedCorners(true)
            .setColoredNavigationBar(true)
            .show()
    }
}