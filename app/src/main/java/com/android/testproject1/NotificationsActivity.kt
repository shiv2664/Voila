package com.android.testproject1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.afollestad.materialdialogs.MaterialDialog
import com.android.testproject1.databinding.ActivityNotificationsBinding
import com.android.testproject1.interfaces.IMainActivity
import com.android.testproject1.room.enteties.NotificationsRoomEntity
import com.android.testproject1.model.Users
import com.android.testproject1.room.enteties.OfferRoomEntity
import com.android.testproject1.room.enteties.UsersChatListEntity
import com.android.testproject1.viewmodels.NotificationsActivityViewModel
import com.google.android.material.button.MaterialButton
import com.google.firebase.firestore.FirebaseFirestore
import es.dmoral.toasty.Toasty


class NotificationsActivity : AppCompatActivity(),IMainActivity {

//    private val notificationsAdapter: NotificationsAdapter? = null
    private val mRecyclerView: RecyclerView? = null
    private val refreshLayout: SwipeRefreshLayout? = null
    private lateinit var binding:ActivityNotificationsBinding
    private lateinit var mViewModel:NotificationsActivityViewModel

    private lateinit var firebaseFirestore:FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding =DataBindingUtil.setContentView(this,R.layout.activity_notifications)

        firebaseFirestore= FirebaseFirestore.getInstance()

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        mViewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory
            .getInstance(this.application))
            .get(NotificationsActivityViewModel::class.java)

        mViewModel.getNotifications()
//        binding.refreshLayout.setOnRefreshListener { mViewModel.getNotifications() }

        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        mViewModel.getNotificationList()?.observe(this, {
            binding.dataList= it
        })

        val simpleCallback: ItemTouchHelper.SimpleCallback = object : ItemTouchHelper
        .SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder
                                , target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                when (direction) {
                    ItemTouchHelper.LEFT -> {
//                        deletedMovie = moviesList.get(position)
//                        moviesList.remove(position)
                        binding.notificationsRecyclerView.adapter?.notifyItemRemoved(position)
//                        Snackbar.make(recyclerView, deletedMovie, Snackbar.LENGTH_LONG)
//                            .setAction("Undo", View.OnClickListener {
//                                moviesList.add(position, deletedMovie)
//                                recyclerAdapter.notifyItemInserted(position)
//                            }).show()
                    }
                    ItemTouchHelper.RIGHT -> {
//                        val movieName: String = moviesList.get(position)
//                        archivedMovies.add(movieName)
//                        moviesList.remove(position)
                        binding.notificationsRecyclerView.adapter?.notifyItemRemoved(position)
//                        Snackbar.make(recyclerView, "$movieName, Archived.", Snackbar.LENGTH_LONG)
//                            .setAction("Undo") {
//                                archivedMovies.remove(archivedMovies.lastIndexOf(movieName))
//                                moviesList.add(position, movieName)
//                                recyclerAdapter.notifyItemInserted(position)
//                            }.show()
                    }
                }
            }

//            @RequiresApi(Build.VERSION_CODES.N)
//            override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder
//                                     , dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean
//            ) {
//                GestureDescription.Bui(this@NotificationsActivity, c,recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
//                    .addSwipeLeftBackgroundColor(
//                        ContextCompat.getColor(
//                            this@MainActivity,
//                            R.color.colorAccent
//                        )
//                    )
//                    .addSwipeLeftActionIcon(R.drawable.ic_delete_black_24dp)
//                    .addSwipeRightBackgroundColor(
//                        ContextCompat.getColor(
//                            this@MainActivity,
//                            R.color.colorPrimaryDark
//                        )
//                    )
//                    .addSwipeRightActionIcon(R.drawable.ic_archive_black_24dp)
//                    .setActionIconTint(ContextCompat.getColor(recyclerView.context, R.color.white))
//                    .create()
//                    .decorate()
//                super.onChildDraw(
//                    c,
//                    recyclerView,
//                    viewHolder,
//                    dX,
//                    dY,
//                    actionState,
//                    isCurrentlyActive
//                )
//            }
        }


        val itemTouchHelper=ItemTouchHelper(simpleCallback)
        itemTouchHelper.attachToRecyclerView(binding.notificationsRecyclerView)



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

    override fun onRecyclerViewGroupsItemClick(offerItem: OfferRoomEntity) {
        TODO("Not yet implemented")
    }

    override fun onPlaceOrderClick(offerItem: OfferRoomEntity, Total: String, Quantity: String) {
        TODO("Not yet implemented")
    }

    override fun onAcceptClick(
        notificationsRoomEntityItem: NotificationsRoomEntity,
        reject: MaterialButton,
        accept: MaterialButton,
        cancel: MaterialButton,
        ready: MaterialButton,
        linearLayout: LinearLayout,
        linearlayout2: LinearLayout,
        linearLayout3: LinearLayout,
        linearlayout4: LinearLayout
    ) {

        linearLayout.visibility=View.GONE
        linearlayout2.visibility=View.VISIBLE
        linearLayout3.visibility=View.GONE
        linearlayout4.visibility=View.GONE

        val notificationRef = notificationsRoomEntityItem.orderTo?.let {
                firebaseFirestore.collection("Users").document(it)
                    .collection("Info_Notifications").document(notificationsRoomEntityItem.idOrder)
            }

        val orderRef = notificationsRoomEntityItem.orderTo?.let {
            firebaseFirestore.collection("Users").document(it)
                .collection("Orders").document(notificationsRoomEntityItem.idOrder)
        }

        val orderRef2 = notificationsRoomEntityItem.orderFrom?.let {
            firebaseFirestore.collection("Users").document(it)
                .collection("Orders").document(notificationsRoomEntityItem.idOrder)
        }

        notificationsRoomEntityItem.orderTo?.let {
            firebaseFirestore.collection("Users").document(it)
                .collection("Info_Notifications").document("recentOrder")
                .get().addOnSuccessListener {
                    val recentOrder=it.toObject(NotificationsRoomEntity::class.java)
                    if (recentOrder?.idOrder==notificationsRoomEntityItem.idOrder){
                        firebaseFirestore.collection("Users").document(notificationsRoomEntityItem.orderTo!!)
                            .collection("Info_Notifications").document("recentOrder")
                            .update("status","Accepted")
                    }
                }
        }?.continueWith {
            notificationsRoomEntityItem.orderFrom?.let {
                firebaseFirestore.collection("Users").document(it)
                    .collection("Orders").document("recentOrder")
                    .get().addOnSuccessListener {
                        val recentOrder=it.toObject(NotificationsRoomEntity::class.java)
                        if (recentOrder?.idOrder==notificationsRoomEntityItem.idOrder){
                            firebaseFirestore.collection("Users").document(notificationsRoomEntityItem.orderTo!!)
                                .collection("Info_Notifications").document("recentOrder")
                                .update("status","Accepted")
                        }
                    }

            }

        }?.continueWith {
            notificationsRoomEntityItem.orderTo?.let {
                firebaseFirestore.collection("Users").document(it)
                    .collection("Orders").document("recentOrder")
                    .get().addOnSuccessListener {
                        val recentOrder=it.toObject(NotificationsRoomEntity::class.java)
                        if (recentOrder?.idOrder==notificationsRoomEntityItem.idOrder){
                            firebaseFirestore.collection("Users").document(notificationsRoomEntityItem.orderTo!!)
                                .collection("Info_Notifications").document("recentOrder")
                                .update("status","Accepted")
                        }
                    }
            }

        }?.continueWith {

            orderRef2?.update("status","Accepted")?.continueWith {
                notificationRef?.update("status","Accepted")?.continueWith {
                    orderRef?.update("status","Accepted") } }?.addOnSuccessListener {
//                Toasty.success(this,"Order Accepted",Toasty.LENGTH_SHORT).show()
            }
        }?.addOnSuccessListener {
            Toasty.success(this,"Order Accepted",Toasty.LENGTH_SHORT).show()
        }


    }

    override fun onOrderReadyClick(
        notificationsRoomEntityItem: NotificationsRoomEntity,
        cancel: MaterialButton,
        ready: MaterialButton,
        waiting: MaterialButton,
        linearLayout: LinearLayout,
        linearLayout2: LinearLayout,
        linearLayout3: LinearLayout,
        linearLayout4: LinearLayout
    ) {

        linearLayout.visibility=View.GONE
        linearLayout2.visibility=View.GONE
        linearLayout3.visibility=View.VISIBLE
        linearLayout4.visibility=View.GONE

        val notificationRef = notificationsRoomEntityItem.orderTo?.let {
            firebaseFirestore.collection("Users").document(it)
                .collection("Info_Notifications").document(notificationsRoomEntityItem.idOrder)
        }

        val orderRef = notificationsRoomEntityItem.orderTo?.let {
            firebaseFirestore.collection("Users").document(it)
                .collection("Orders").document(notificationsRoomEntityItem.idOrder)
        }

        val orderRef2 = notificationsRoomEntityItem.orderFrom?.let {
            firebaseFirestore.collection("Users").document(it)
                .collection("Orders").document(notificationsRoomEntityItem.idOrder)
        }

        notificationsRoomEntityItem.orderTo?.let {
            firebaseFirestore.collection("Users").document(it)
                .collection("Info_Notifications").document("recentOrder")
                .get().addOnSuccessListener {
                    val recentOrder=it.toObject(NotificationsRoomEntity::class.java)
                    if (recentOrder?.idOrder==notificationsRoomEntityItem.idOrder){
                        firebaseFirestore.collection("Users").document(notificationsRoomEntityItem.orderTo!!)
                            .collection("Info_Notifications").document("recentOrder")
                            .update("status","Order Ready")
                    }
                }
        }?.continueWith {
            notificationsRoomEntityItem.orderFrom?.let {
                firebaseFirestore.collection("Users").document(it)
                    .collection("Orders").document("recentOrder")
                    .get().addOnSuccessListener {
                        val recentOrder=it.toObject(NotificationsRoomEntity::class.java)
                        if (recentOrder?.idOrder==notificationsRoomEntityItem.idOrder){
                            firebaseFirestore.collection("Users").document(notificationsRoomEntityItem.orderTo!!)
                                .collection("Info_Notifications").document("recentOrder")
                                .update("status","Order Ready")
                        }
                    }
            }
        }?.continueWith {
            notificationsRoomEntityItem.orderTo?.let {
                firebaseFirestore.collection("Users").document(it)
                    .collection("Orders").document("recentOrder")
                    .get().addOnSuccessListener {
                        val recentOrder=it.toObject(NotificationsRoomEntity::class.java)
                        if (recentOrder?.idOrder==notificationsRoomEntityItem.idOrder){
                            firebaseFirestore.collection("Users").document(notificationsRoomEntityItem.orderTo!!)
                                .collection("Info_Notifications").document("recentOrder")
                                .update("status","Order Ready")
                        }
                    }
            }
        }?.continueWith {
            orderRef2?.update("status", "Order Ready")?.continueWith {
                notificationRef?.update("status", "Order Ready")?.continueWith {
                    orderRef?.update("status", "Order Ready") }
            }?.addOnSuccessListener {
//                Toasty.success(this, "Order Ready Sent", Toasty.LENGTH_SHORT).show()
            }
        }?.addOnSuccessListener {
            Toasty.success(this, "Order Ready Sent", Toasty.LENGTH_SHORT).show()
        }

    }

    override fun onWaitingClicked(
        notificationItem: NotificationsRoomEntity,
        linearLayout: LinearLayout,
        linearLayout2: LinearLayout,
        linearLayout3: LinearLayout,
        linearLayout4: LinearLayout
    ) {
        linearLayout.visibility=View.GONE
        linearLayout2.visibility=View.GONE
        linearLayout3.visibility=View.GONE
        linearLayout4.visibility=View.VISIBLE

        val notificationRef = notificationItem.orderTo?.let {
            firebaseFirestore.collection("Users").document(it)
                .collection("Info_Notifications").document(notificationItem.idOrder)
        }

        val orderRef = notificationItem.orderTo?.let {
            firebaseFirestore.collection("Users").document(it)
                .collection("Orders").document(notificationItem.idOrder)
        }

        val orderRef2 = notificationItem.orderFrom?.let {
            firebaseFirestore.collection("Users").document(it)
                .collection("Orders").document(notificationItem.idOrder)
        }

        notificationItem.orderTo?.let {
            firebaseFirestore.collection("Users").document(it)
                .collection("Info_Notifications").document("recentOrder")
                .get().addOnSuccessListener {
                    val recentOrder=it.toObject(NotificationsRoomEntity::class.java)
                    if (recentOrder?.idOrder==notificationItem.idOrder){
                        firebaseFirestore.collection("Users").document(notificationItem.orderTo!!)
                            .collection("Info_Notifications").document("recentOrder")
                            .update("status","Delivered")
                    }
                }
        }?.continueWith {
            notificationItem.orderFrom?.let {
                firebaseFirestore.collection("Users").document(it)
                    .collection("Orders").document("recentOrder")
                    .get().addOnSuccessListener {
                        val recentOrder=it.toObject(NotificationsRoomEntity::class.java)
                        if (recentOrder?.idOrder==notificationItem.idOrder){
                            firebaseFirestore.collection("Users").document(notificationItem.orderTo!!)
                                .collection("Info_Notifications").document("recentOrder")
                                .update("status","Delivered")
                        }
                    }
            }
        }?.continueWith {
            notificationItem.orderTo?.let {
                firebaseFirestore.collection("Users").document(it)
                    .collection("Orders").document("recentOrder")
                    .get().addOnSuccessListener {
                        val recentOrder = it.toObject(NotificationsRoomEntity::class.java)
                        if (recentOrder?.idOrder == notificationItem.idOrder) {
                            firebaseFirestore.collection("Users")
                                .document(notificationItem.orderTo!!)
                                .collection("Info_Notifications").document("recentOrder")
                                .update("status", "Delivered")
                        }
                    }
            }
        }?.continueWith {
            orderRef2?.update("status","Delivered")?.continueWith {
                notificationRef?.update("status","Delivered")?.continueWith {
                    orderRef?.update("status","Delivered") } }?.addOnSuccessListener {
//                Toasty.success(this,"Order Delivered",Toasty.LENGTH_SHORT).show() }
            }
        }?.addOnSuccessListener {
            Toasty.success(this,"Order Delivered",Toasty.LENGTH_SHORT).show()
        }


    }

    override fun onCurrentUserOfferClick(offerItem: OfferRoomEntity) {
        TODO("Not yet implemented")
    }

    override fun onProfileOpenedDiscover(offerItem: OfferRoomEntity) {
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

    override fun onNotificationItemClick(notificationsRoomEntityItem: NotificationsRoomEntity) {
       val intent =Intent(this,MainActivity2::class.java)
        intent.putExtra("notificationsItem",notificationsRoomEntityItem)
        intent.putExtra("openFriend","openFriend")
        startActivity(intent)
    }

    override fun onSearchPeopleItemClick(userItem: Users) {
        TODO("Not yet implemented")
    }

    override fun onBookMarkItemClick(offerItem: OfferRoomEntity) {
        TODO("Not yet implemented")
    }

    override fun onGroupOpenFromMessages(userItem: UsersChatListEntity) {
        TODO("Not yet implemented")
    }

}