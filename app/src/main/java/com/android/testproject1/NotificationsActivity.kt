package com.android.testproject1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.afollestad.materialdialogs.MaterialDialog
import com.android.testproject1.databinding.ActivityNotificationsBinding
import com.android.testproject1.fragments.ProfileOpened
import com.android.testproject1.interfaces.IMainActivity
import com.android.testproject1.room.enteties.NotificationsRoomEntity
import com.android.testproject1.model.Users
import com.android.testproject1.room.enteties.OfferRoomEntity
import com.android.testproject1.room.enteties.OffersSavedRoomEntity
import com.android.testproject1.room.enteties.UsersChatListEntity
import com.android.testproject1.viewmodels.NotificationsActivityViewModel
import com.google.android.material.button.MaterialButton
import com.google.firebase.firestore.FirebaseFirestore
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_notifications.*


class NotificationsActivity : AppCompatActivity(),IMainActivity {

//    private val notificationsAdapter: NotificationsAdapter? = null
    private val mRecyclerView: RecyclerView? = null
    private val refreshLayout: SwipeRefreshLayout? = null
    private lateinit var binding:ActivityNotificationsBinding
    private lateinit var mViewModel:NotificationsActivityViewModel

    private var clear: MenuItem? = null

    private var fragmentmanager: FragmentManager?=null

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
        clear = menu?.findItem(R.id.action_clear)
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

    override fun onUserNamePicClick(userId: String?, orderTo: String?) {
        binding.recyclerViewContainer.visibility=View.GONE
        val bundle= Bundle()
        bundle.putString("userId",userId)
        clear?.isVisible = false
        toolbar.title = "Profile"

        val fragment: Fragment =ProfileOpened()
        fragment.arguments=bundle

        fragmentmanager= supportFragmentManager
        fragmentmanager!!
            .beginTransaction()
            .setCustomAnimations(R.anim.right_enter,R.anim.left_out)
            .add(R.id.fragment_container2, fragment)
            .commit()

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
        linearlayout4: LinearLayout,
        CancelLayout: LinearLayout,
        RejectedLayout: LinearLayout
    ) {

        linearLayout.visibility=View.GONE
        linearlayout2.visibility=View.VISIBLE
        linearLayout3.visibility=View.GONE
        linearlayout4.visibility=View.GONE

        val notificationRef = notificationsRoomEntityItem.orderTo?.let {
            firebaseFirestore.collection("Users").document(it)
                .collection("Info_Notifications").document(notificationsRoomEntityItem.idOrder)
        }

        //order history of user who received notification
        val orderRef = notificationsRoomEntityItem.orderTo?.let {
            firebaseFirestore.collection("Users").document(it)
                .collection("Orders").document(notificationsRoomEntityItem.idOrder)
        }

        //order history of user who sent order
        val orderRef2 = notificationsRoomEntityItem.orderFrom?.let {
            firebaseFirestore.collection("Users").document(it)
                .collection("Orders").document(notificationsRoomEntityItem.idOrder)
        }

        //update recent notification of user who received notification
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
            //update recent order of user who sent order
            notificationsRoomEntityItem.orderFrom?.let {
                firebaseFirestore.collection("Users").document(it)
                    .collection("Orders").document("recentOrder")
                    .get().addOnSuccessListener {
                        val recentOrder=it.toObject(NotificationsRoomEntity::class.java)
                        if (recentOrder?.idOrder==notificationsRoomEntityItem.idOrder){
                            firebaseFirestore.collection("Users").document(notificationsRoomEntityItem.orderFrom!!)
                                .collection("Orders").document("recentOrder")
                                .update("status","Accepted")
                        }
                    }
            }
        }?.continueWith {
            //update recent order of user who received order
            notificationsRoomEntityItem.orderTo?.let {
                firebaseFirestore.collection("Users").document(it)
                    .collection("Orders").document("recentOrder")
                    .get().addOnSuccessListener {
                        val recentOrder=it.toObject(NotificationsRoomEntity::class.java)
                        if (recentOrder?.idOrder==notificationsRoomEntityItem.idOrder){
                            firebaseFirestore.collection("Users").document(notificationsRoomEntityItem.orderTo!!)
                                .collection("Orders").document("recentOrder")
                                .update("status","Accepted")
                        }
                    }
            }
        }?.continueWith {
            orderRef2?.update("status", "Accepted")?.continueWith {
                notificationRef?.update("status", "Accepted")?.continueWith {
                    orderRef?.update("status", "Accepted") }
            }?.addOnSuccessListener {
//                Toasty.success(this, "Order Ready Sent", Toasty.LENGTH_SHORT).show()
            }
        }?.addOnSuccessListener {
            Toasty.normal(this, "Accepted", Toasty.LENGTH_SHORT).show()
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
        linearLayout4: LinearLayout,
        CancelLayout: LinearLayout,
        RejectedLayout: LinearLayout
    ) {

        linearLayout.visibility=View.GONE
        linearLayout2.visibility=View.GONE
        linearLayout3.visibility=View.VISIBLE
        linearLayout4.visibility=View.GONE

        //notifications of user who received order
        val notificationRef = notificationsRoomEntityItem.orderTo?.let {
            firebaseFirestore.collection("Users").document(it)
                .collection("Info_Notifications").document(notificationsRoomEntityItem.idOrder)
        }

        //order history of user who received notification
        val orderRef = notificationsRoomEntityItem.orderTo?.let {
            firebaseFirestore.collection("Users").document(it)
                .collection("Orders").document(notificationsRoomEntityItem.idOrder)
        }

        //order history of user who sent order
        val orderRef2 = notificationsRoomEntityItem.orderFrom?.let {
            firebaseFirestore.collection("Users").document(it)
                .collection("Orders").document(notificationsRoomEntityItem.idOrder)
        }

        //update recent notification of user who received notification
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
            //update recent order of user who sent order
            notificationsRoomEntityItem.orderFrom?.let {
                firebaseFirestore.collection("Users").document(it)
                    .collection("Orders").document("recentOrder")
                    .get().addOnSuccessListener {
                        val recentOrder=it.toObject(NotificationsRoomEntity::class.java)
                        if (recentOrder?.idOrder==notificationsRoomEntityItem.idOrder){
                            firebaseFirestore.collection("Users").document(notificationsRoomEntityItem.orderFrom!!)
                                .collection("Orders").document("recentOrder")
                                .update("status","Order Ready")
                        }
                    }
            }
        }?.continueWith {
            //update recent order of user who received order
            notificationsRoomEntityItem.orderTo?.let {
                firebaseFirestore.collection("Users").document(it)
                    .collection("Orders").document("recentOrder")
                    .get().addOnSuccessListener {
                        val recentOrder=it.toObject(NotificationsRoomEntity::class.java)
                        if (recentOrder?.idOrder==notificationsRoomEntityItem.idOrder){
                            firebaseFirestore.collection("Users").document(notificationsRoomEntityItem.orderTo!!)
                                .collection("Orders").document("recentOrder")
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
            Toasty.normal(this, "Order Ready", Toasty.LENGTH_SHORT).show()
        }

    }

    override fun onWaitingClicked(
        notificationsRoomEntityItem: NotificationsRoomEntity,
        linearLayout: LinearLayout,
        linearLayout2: LinearLayout,
        linearLayout3: LinearLayout,
        linearLayout4: LinearLayout,
        CancelLayout: LinearLayout,
        RejectedLayout: LinearLayout
    ) {
        linearLayout.visibility=View.GONE
        linearLayout2.visibility=View.GONE
        linearLayout3.visibility=View.GONE
        linearLayout4.visibility=View.VISIBLE

        //notifications of user who received order
        val notificationRef = notificationsRoomEntityItem.orderTo?.let {
            firebaseFirestore.collection("Users").document(it)
                .collection("Info_Notifications").document(notificationsRoomEntityItem.idOrder)
        }

        //order history of user who received notification
        val orderRef = notificationsRoomEntityItem.orderTo?.let {
            firebaseFirestore.collection("Users").document(it)
                .collection("Orders").document(notificationsRoomEntityItem.idOrder)
        }

        //order history of user who sent order
        val orderRef2 = notificationsRoomEntityItem.orderFrom?.let {
            firebaseFirestore.collection("Users").document(it)
                .collection("Orders").document(notificationsRoomEntityItem.idOrder)
        }

        //update recent notification of user who received notification
        notificationsRoomEntityItem.orderTo?.let {
            firebaseFirestore.collection("Users").document(it)
                .collection("Info_Notifications").document("recentOrder")
                .get().addOnSuccessListener {
                    val recentOrder=it.toObject(NotificationsRoomEntity::class.java)
                    if (recentOrder?.idOrder==notificationsRoomEntityItem.idOrder){
                        firebaseFirestore.collection("Users").document(notificationsRoomEntityItem.orderTo!!)
                            .collection("Info_Notifications").document("recentOrder")
                            .update("status","Delivered")
                    }
                }
        }?.continueWith {
            //update recent order of user who sent order
            notificationsRoomEntityItem.orderFrom?.let {
                firebaseFirestore.collection("Users").document(it)
                    .collection("Orders").document("recentOrder")
                    .get().addOnSuccessListener {
                        val recentOrder=it.toObject(NotificationsRoomEntity::class.java)
                        if (recentOrder?.idOrder==notificationsRoomEntityItem.idOrder){
                            firebaseFirestore.collection("Users").document(notificationsRoomEntityItem.orderFrom!!)
                                .collection("Orders").document("recentOrder")
                                .update("status","Delivered")
                        }
                    }
            }
        }?.continueWith {
            //update recent order of user who received order
            notificationsRoomEntityItem.orderTo?.let {
                firebaseFirestore.collection("Users").document(it)
                    .collection("Orders").document("recentOrder")
                    .get().addOnSuccessListener {
                        val recentOrder=it.toObject(NotificationsRoomEntity::class.java)
                        if (recentOrder?.idOrder==notificationsRoomEntityItem.idOrder){
                            firebaseFirestore.collection("Users").document(notificationsRoomEntityItem.orderTo!!)
                                .collection("Orders").document("recentOrder")
                                .update("status","Delivered")
                        }
                    }
            }
        }?.continueWith {
            orderRef2?.update("status", "Delivered")?.continueWith {
                notificationRef?.update("status", "Delivered")?.continueWith {
                    orderRef?.update("status", "Delivered") }
            }?.addOnSuccessListener {
//                Toasty.success(this, "Order Ready Sent", Toasty.LENGTH_SHORT).show()
            }
        }?.addOnSuccessListener {
            Toasty.normal(this, "Delivered", Toasty.LENGTH_SHORT).show()
        }


    }

    override fun onRejectClick(
        notificationsRoomEntityItem: NotificationsRoomEntity,
        linearLayout: LinearLayout,
        linearlayout2: LinearLayout,
        linearLayout3: LinearLayout,
        linearlayout4: LinearLayout,
        CancelLayout: LinearLayout,
        RejectedLayout: LinearLayout
    ) {

        linearLayout.visibility=View.GONE
        linearlayout2.visibility=View.GONE
        linearLayout3.visibility=View.GONE
        linearlayout4.visibility=View.GONE
        CancelLayout.visibility=View.GONE
        RejectedLayout.visibility=View.VISIBLE

        //notifications of user who received order
        val notificationRef = notificationsRoomEntityItem.orderTo?.let {
            firebaseFirestore.collection("Users").document(it)
                .collection("Info_Notifications").document(notificationsRoomEntityItem.idOrder)
        }

        //order history of user who received notification
        val orderRef = notificationsRoomEntityItem.orderTo?.let {
            firebaseFirestore.collection("Users").document(it)
                .collection("Orders").document(notificationsRoomEntityItem.idOrder)
        }

        //order history of user who sent order
        val orderRef2 = notificationsRoomEntityItem.orderFrom?.let {
            firebaseFirestore.collection("Users").document(it)
                .collection("Orders").document(notificationsRoomEntityItem.idOrder)
        }

        //update recent notification of user who received notification
        notificationsRoomEntityItem.orderTo?.let {
            firebaseFirestore.collection("Users").document(it)
                .collection("Info_Notifications").document("recentOrder")
                .get().addOnSuccessListener {
                    val recentOrder=it.toObject(NotificationsRoomEntity::class.java)
                    if (recentOrder?.idOrder==notificationsRoomEntityItem.idOrder){
                        firebaseFirestore.collection("Users").document(notificationsRoomEntityItem.orderTo!!)
                            .collection("Info_Notifications").document("recentOrder")
                            .update("status","Order Rejected")
                    }
                }
        }?.continueWith {
            //update recent order of user who sent order
            notificationsRoomEntityItem.orderFrom?.let {
                firebaseFirestore.collection("Users").document(it)
                    .collection("Orders").document("recentOrder")
                    .get().addOnSuccessListener {
                        val recentOrder=it.toObject(NotificationsRoomEntity::class.java)
                        if (recentOrder?.idOrder==notificationsRoomEntityItem.idOrder){
                            firebaseFirestore.collection("Users").document(notificationsRoomEntityItem.orderFrom!!)
                                .collection("Orders").document("recentOrder")
                                .update("status","Order Rejected")
                        }
                    }
            }
        }?.continueWith {
            //update recent order of user who received order
            notificationsRoomEntityItem.orderTo?.let {
                firebaseFirestore.collection("Users").document(it)
                    .collection("Orders").document("recentOrder")
                    .get().addOnSuccessListener {
                        val recentOrder=it.toObject(NotificationsRoomEntity::class.java)
                        if (recentOrder?.idOrder==notificationsRoomEntityItem.idOrder){
                            firebaseFirestore.collection("Users").document(notificationsRoomEntityItem.orderTo!!)
                                .collection("Orders").document("recentOrder")
                                .update("status","Order Rejected")
                        }
                    }
            }
        }?.continueWith {
            orderRef2?.update("status", "Order Rejected")?.continueWith {
                notificationRef?.update("status", "Order Rejected")?.continueWith {
                    orderRef?.update("status", "Order Rejected") }
            }?.addOnSuccessListener {
//                Toasty.success(this, "Order Ready Sent", Toasty.LENGTH_SHORT).show()
            }
        }?.addOnSuccessListener {
            Toasty.normal(this, "Order Rejected", Toasty.LENGTH_SHORT).show()
        }

    }

    override fun onCancelClick(
        notificationsRoomEntityItem: NotificationsRoomEntity,
        linearLayout: LinearLayout,
        linearlayout2: LinearLayout,
        linearLayout3: LinearLayout,
        linearlayout4: LinearLayout,
        CancelLayout: LinearLayout,
        RejectedLayout: LinearLayout
    ) {
        linearLayout.visibility=View.GONE
        linearlayout2.visibility=View.GONE
        linearLayout3.visibility=View.GONE
        linearlayout4.visibility=View.GONE
        CancelLayout.visibility=View.GONE
        RejectedLayout.visibility=View.GONE
        CancelLayout.visibility=View.VISIBLE

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
                            .update("status","Order Cancelled")
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
                                .update("status","Order Cancelled")
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
                                .update("status","Order Cancelled")
                        }
                    }
            }
        }?.continueWith {
            orderRef2?.update("status", "Order Ready")?.continueWith {
                notificationRef?.update("status", "Order Cancelled")?.continueWith {
                    orderRef?.update("status", "Order Cancelled") }
            }?.addOnSuccessListener {
//                Toasty.success(this, "Order Ready Sent", Toasty.LENGTH_SHORT).show()
            }
        }?.addOnSuccessListener {
            Toasty.normal(this, "Order Cancelled", Toasty.LENGTH_SHORT).show()
        }
    }

    override fun onProfilePostItemCLick(offerItem: OfferRoomEntity) {
        TODO("Not yet implemented")
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

    override fun onBookMarkItemClick(offerItem: OfferRoomEntity, bookmarkImageView: ImageView) {
        TODO("Not yet implemented")
    }

    override fun onBookMarkClickedSavedOffers(
        offerItem: OffersSavedRoomEntity,
        bookmarkImageView: ImageView
    ) {
        TODO("Not yet implemented")
    }

    override fun onGroupOpenFromMessages(userItem: UsersChatListEntity) {
        TODO("Not yet implemented")
    }

}