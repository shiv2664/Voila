package com.android.testproject1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.afollestad.materialdialogs.MaterialDialog
import com.android.testproject1.databinding.ActivityNotificationsBinding
import com.android.testproject1.interfaces.IMainActivity
import com.android.testproject1.model.Notifications
import com.android.testproject1.model.Offer
import com.android.testproject1.model.Users
import com.android.testproject1.room.enteties.UsersChatListEntity
import com.android.testproject1.viewmodels.NotificationsActivityViewModel
import com.google.android.material.button.MaterialButton

class NotificationsActivity : AppCompatActivity(),IMainActivity {

//    private val notificationsAdapter: NotificationsAdapter? = null
    private val mRecyclerView: RecyclerView? = null
    private val refreshLayout: SwipeRefreshLayout? = null
    private lateinit var binding:ActivityNotificationsBinding

    private lateinit var mViewModel:NotificationsActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding =DataBindingUtil.setContentView(this,R.layout.activity_notifications)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        mViewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory
            .getInstance(this.application))
            .get(NotificationsActivityViewModel::class.java)

        mViewModel.getNotifications()
        binding.refreshLayout.setOnRefreshListener { mViewModel.getNotifications() }

        mViewModel.getNotificationList().observe(this, {
            binding.dataList= it
        })


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.menu_notifications, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.action_clear) {


            MaterialDialog.Builder(this)
                .title("Clear all")
                .content("Are you sure do you want to clear all notifications?")
                .positiveText("Yes")
                .negativeText("No")
                .cancelable(true)
                .canceledOnTouchOutside(false)
                .neutralText("Cancel")
                .onPositive { _, _ ->
//                    binding.refreshLayout.isRefreshing=true
                    mViewModel.deleteAll()
//                    binding.refreshLayout.isRefreshing=false


                }
                .onNegative { dialog, _ ->
                    dialog.dismiss()
                }.show()

        }


//            DialogSheet(this)
//                    .setTitle("Clear all")
//                    .setMessage("Are you sure do you want to clear all notifications?")
//                    .setRoundedCorners(true)
//                    .setColoredNavigationBar(true)
//                    .setCancelable(true)
//                    .setPositiveButton("Yes") { mViewModel.deleteAll() }
//                    .setNegativeButton("No") { v: View? ->  }
//                    .show()
//
//        }
        return super.onOptionsItemSelected(item)
    }

//        return when (item.itemId) {
//            R.id.action_clear -> {
//                DialogSheet(this)
//                    .setTitle("Clear all")
//                    .setMessage("Are you sure do you want to clear all notifications?")
//                    .setRoundedCorners(true)
//                    .setColoredNavigationBar(true)
//                    .setCancelable(true)
//                    .setPositiveButton("Yes") { v: View? -> mViewModel.deleteAll() }
//                    .setNegativeButton("No") { v: View? -> }
//                    .show()
//                true
//            }
//            else -> super.onOptionsItemSelected(item)
//        }
//    }

    override fun onRegisterClick() {
        TODO("Not yet implemented")
    }

    override fun onRecyclerViewItemClick(offerItem: Offer) {
        TODO("Not yet implemented")
    }

    override fun onPlaceOrderClick(offerItem: Offer, Total: String, Quantity: String) {
        TODO("Not yet implemented")
    }

    override fun onAcceptClick(
        notificationsItem: Notifications,
        reject: MaterialButton,
        accept: MaterialButton,
        cancel: MaterialButton,
        ready: MaterialButton
    ) {
        reject.visibility=View.GONE
        accept.visibility=View.GONE

        cancel.visibility=View.VISIBLE
        ready.visibility=View.VISIBLE
    }

    override fun onOrderReadyClick(
        notificationsItem: Notifications,
        cancel: MaterialButton,
        ready: MaterialButton,
        waiting: MaterialButton
    ) {
        cancel.visibility=View.GONE
        ready.visibility=View.GONE
        waiting.visibility=View.VISIBLE
    }

    override fun onProfileOpenedDiscover(offerItem: Offer) {
        TODO("Not yet implemented")
    }

    override fun onJoinItemClick(userItem: UsersChatListEntity) {
        TODO("Not yet implemented")
    }

    override fun onGroupItemClick(userItem: UsersChatListEntity) {
        TODO("Not yet implemented")
    }

    override fun onGroupItemClick(userItem: Users) {
        TODO("Not yet implemented")
    }

    override fun onLocationClick() {
        TODO("Not yet implemented")
    }

    override fun onSendFriendReqClick(userItem: Users) {
        TODO("Not yet implemented")
    }

    override fun onNotificationItemClick(notificationsItem: Notifications) {
       val intent =Intent(this,MainActivity2::class.java)
        intent.putExtra("notificationsItem",notificationsItem)
        intent.putExtra("openFriend","openFriend")
        startActivity(intent)
    }

    override fun onSearchPeopleItemClick(userItem: Users) {
        TODO("Not yet implemented")
    }

    override fun onBookMarkItemClick(offerItem: Offer) {
        TODO("Not yet implemented")
    }

    override fun onGroupOpenFromMessages(userItem: UsersChatListEntity) {
        TODO("Not yet implemented")
    }

}