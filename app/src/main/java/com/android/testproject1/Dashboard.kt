package com.android.testproject1

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.android.testproject1.databinding.FragmentDashboardBinding
import com.android.testproject1.fragments.HomeFragment
import com.android.testproject1.fragments.SearchFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomnavigation.BottomNavigationView.OnNavigationItemReselectedListener
import com.google.firebase.auth.FirebaseAuth
import com.marcoscg.dialogsheet.DialogSheet
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.fragment_dashboard.*

class Dashboard : Fragment(), BottomNavigationView.OnNavigationItemSelectedListener,
    OnNavigationItemReselectedListener {

    private lateinit var binding:FragmentDashboardBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentDashboardBinding.inflate(inflater,container,false)

        val bundle = this.arguments
        val navSelected= bundle?.getString("bottom_nav","0")

        val bottomNavigationView= binding.bottomNav
        bottomNavigationView.setOnNavigationItemSelectedListener(this)
        bottomNavigationView.setOnNavigationItemReselectedListener(this)

        if (navSelected=="1"){
            loadfragment(SearchFragment())
            binding.bottomNav.selectedItemId=R.id.action_discover
        }else{
            loadfragment(HomeFragment())
            binding.bottomNav.selectedItemId=R.id.action_home
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
//                add_post.setVisible(true)
                loadfragment(HomeFragment())
            }
            R.id.action_discover -> {
//                add_post.setVisible(false)
                loadfragment(SearchFragment())
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

    private var request_alert: CardView? = null
    private var request_alert_text: TextView? = null
    private fun checkforNotifications() {}

//    private fun checkFriendRequest() {
//        request_alert = view!!.findViewById(R.id.friend_req_alert)
//        request_alert_text = view!!.findViewById(R.id.friend_req_alert_text)
//        FirebaseFirestore.getInstance().collection("Users")
//            .document(FirebaseAuth.getInstance().currentUser.uid)
//            .collection("Friend_Requests")
//            .addSnapshotListener(
//                activity
//            ) { queryDocumentSnapshots: QuerySnapshot?, e: FirebaseFirestoreException? ->
//                if (e != null) {
//                    e.printStackTrace()
//                    return@addSnapshotListener
//                }
//                if (!queryDocumentSnapshots!!.isEmpty) {
//                    try {
//                        request_alert_text.setText(
//                            String.format(
//                                getString(R.string.you_have_d_new_friend_request_s),
//                                queryDocumentSnapshots.size()
//                            )
//                        )
//                        request_alert.setVisibility(View.VISIBLE)
//                        request_alert.setAlpha(0.0f)
//                        request_alert.animate()
//                            .setDuration(300)
//                            .scaleX(1.0f)
//                            .scaleY(1.0f)
//                            .alpha(1.0f)
//                            .start()
//                        request_alert.setOnClickListener(View.OnClickListener {
//                            if (FirebaseAuth.getInstance().currentUser.isEmailVerified) {
//                                toolbar.setTitle("Manage Friends")
//                                try {
//                                    (activity as AppCompatActivity).supportActionBar!!
//                                        .setTitle("Manage Friends")
//                                } catch (e: Exception) {
//                                    Log.e("Error", e.message!!)
//                                }
//                                showFragment(FriendsFragment.newInstance("request"))
//                            } else {
//                                showDialog()
//                            }
//                        })
//                    } catch (e1: Exception) {
//                        e1.printStackTrace()
//                    }
//                }
//            }
//    }

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