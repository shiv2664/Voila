package com.android.testproject1

import android.Manifest
import android.content.Intent
import android.content.Intent.*
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.afollestad.materialdialogs.MaterialDialog
import com.android.testproject1.adapter.MenuAdapter
import com.android.testproject1.fragments.*
import com.android.testproject1.interfaces.IMainActivity
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.nguyenhoanglam.imagepicker.model.Config
import com.nguyenhoanglam.imagepicker.model.Image
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_main2.*
import kotlinx.android.synthetic.main.activity_main2.bottomNav
import kotlinx.android.synthetic.main.bottom_sheet_dialog.*
import kotlinx.android.synthetic.main.drawer_menu.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.android.testproject1.model.Users
import com.android.testproject1.room.enteties.*
import com.android.testproject1.viewmodels.MainActivity2ViewModel
import com.google.android.material.button.MaterialButton
import kotlinx.android.synthetic.main.fragment_profile_opened.*
import kotlinx.android.synthetic.main.notifications_item.*

class MainActivity2 : AppCompatActivity(), IMainActivity {

    companion object {
        private const val READ_PERMISSION_CODE = 100
        private const val STORAGE_PERMISSION_CODE = 101
        private var GROUP_USER_ID: String? = null
    }

    private lateinit var firebaseFirestore: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth
    var postId: String = ""
    val myTag = "MyTag"
    private var imagesList1: ArrayList<Image> = java.util.ArrayList()
    private val pickImages = 102
    private val pickImages2 = 103
    private var serviceCount = 0

    private var addPost: MenuItem? = null
    var checkTabDashboard: Boolean? = null

    private lateinit var mViewModel: MainActivity2ViewModel

    private lateinit var mNavDrawer: DrawerLayout
    private var mTitles: ArrayList<String> = ArrayList()
    private lateinit var mMenuAdapter: MenuAdapter

    private lateinit var mBottomSheetDialog: BottomSheetDialog

    var stringCheck: String? = null

    var doubleBackToExitPressedOnce = false
    private lateinit var navController: NavController
    private val fragment1: Fragment = SearchFragment()
    private val fragment2: Fragment = MessagesFragment()
    private val fragment3: Fragment = ProfileFragment()
    private val fm: FragmentManager = supportFragmentManager
    var active = fragment1
    private lateinit var navigation: BottomNavigationView
    var idOrder: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        setSupportActionBar(toolbar)
        toolbar.title = "Voila"

        stringCheck = intent.getStringExtra("openFriend")
        val notificationsItem =
            intent.getParcelableExtra<NotificationsRoomEntity>("notificationsItem")

        if (stringCheck == "openFriend") {

            val fragment = SearchPeopleContainer()
            val fm = supportFragmentManager
            val bundle = Bundle()
            bundle.putString("openFriend", "openFriend")
            bundle.putParcelable("notificationsItem", notificationsItem)
            toolbar.title = "Search"

            fragment.arguments = bundle
            fm.beginTransaction()
                .replace(R.id.container, fragment)
                .commit()

        } else {
//            loadFragment(HomeFragment())
//            loadFragment(SearchFragment())
        }

        mViewModel = ViewModelProvider(
            this, ViewModelProvider.AndroidViewModelFactory
                .getInstance(this.application))
            .get(MainActivity2ViewModel::class.java)

        firebaseAuth = FirebaseAuth.getInstance()
        firebaseFirestore = FirebaseFirestore.getInstance()


//        checkPermission(
//            Manifest.permission.WRITE_EXTERNAL_STORAGE,
//            STORAGE_PERMISSION_CODE
//        )
//        checkPermission(
//            Manifest.permission.READ_EXTERNAL_STORAGE,
//            READ_PERMISSION_CODE
//        )



        mBottomSheetDialog = BottomSheetDialog(this)
        val sheetView = layoutInflater.inflate(R.layout.bottom_sheet_dialog, null)
        mBottomSheetDialog.setContentView(sheetView)

//        val bottomNavigationView = bottomNav
//        bottomNavigationView.background = null
////        loadFragment(HomeFragment())
//        loadFragment(SearchFragment())
//        bottomNavigationView.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
//            when (menuItem.itemId)
//            {
//                R.id.search -> return@OnNavigationItemSelectedListener loadFragment(SearchFragment())
//
////                    return@OnNavigationItemSelectedListener loadFragment(HomeFragment())}
//                R.id.notifications -> return@OnNavigationItemSelectedListener loadFragment(MessagesFragment())
//                R.id.profile -> return@OnNavigationItemSelectedListener loadFragment(ProfileFragment())
////                R.id.NewPost -> return@OnNavigationItemSelectedListener loadFragment(Friends())
//
//            }
//            false
//        })


        val buttonPostPhoto: LinearLayout = sheetView.findViewById(R.id.postImage)
        val buttonPostOffer: LinearLayout = sheetView.findViewById(R.id.postOffer)

        buttonPostPhoto.setOnClickListener {
            mBottomSheetDialog.dismiss()

            startPickImage()
//            PostText.startActivity(this@MainActivity)
        }

        buttonPostOffer.setOnClickListener {
            mBottomSheetDialog.dismiss()
            checkPermission(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                STORAGE_PERMISSION_CODE
            )
            checkPermission(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                READ_PERMISSION_CODE
            )
            startPickImage2()
//            val intent=Intent(this,CreateOffer::class.java)
//            startActivity(intent)
        }

        val bottomNavigation = bottomNav
        val navHost =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView2) as NavHostFragment
        navController = navHost.navController
        NavigationUI.setupWithNavController(toolbar, navController)
        bottomNavigation.setupWithNavController(navController)


        checkTabDashboard = true

    }

    private fun setFragment(fragment: Fragment, tag: String?, position: Int) {
//        val fm = supportFragmentManager
        if (fragment.isAdded) {
            fm.beginTransaction().hide(active).show(fragment).commit()
        } else {
            fm.beginTransaction().add(R.id.container, fragment, tag).commit()
        }
        navigation.menu.getItem(position)?.isChecked = true
        active = fragment
    }


    private fun startPickImage() {
        imagesList1.clear()
        ImagePicker.with(this)
            .setFolderMode(true)
            .setFolderTitle("Album")
            .setRootDirectoryName(Config.ROOT_DIR_DCIM)
            .setDirectoryName("Image Picker")
            .setMultipleMode(true)
            .setShowNumberIndicator(true)
            .setMaxSize(6)
            .setLimitMessage("You can select up to 6 images")
            .setSelectedImages(imagesList1)
            .setRequestCode(pickImages)
            .start();
    }

    private fun startPickImage2() {
        imagesList1.clear()
        ImagePicker.with(this)
            .setFolderMode(true)
            .setFolderTitle("Album")
            .setRootDirectoryName(Config.ROOT_DIR_DCIM)
            .setDirectoryName("Image Picker")
            .setMultipleMode(true)
            .setShowNumberIndicator(true)
            .setMaxSize(6)
            .setLimitMessage("You can select up to 6 images")
            .setSelectedImages(imagesList1)
            .setRequestCode(pickImages2)
            .start();
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

//        Log.d(myTag,"Req Code "+requestCode+" "+Activity.RESULT_OK)

        if (resultCode == RESULT_OK && data != null) {

            if (ImagePicker.shouldHandleResult(requestCode, resultCode, data, pickImages)) {
                imagesList1 = ImagePicker.getImages(data)

//                val intent = Intent(this, CreatePost::class.java)
//                intent.putParcelableArrayListExtra("imagesList", imagesList1)
//                startActivity(intent)

                MaterialDialog.Builder(this)
                    .title("Confirmation")
                    .content("Are you sure do you want to continue?")
                    .positiveText("Yes")
                    .negativeText("No")
                    .cancelable(false)
                    .canceledOnTouchOutside(false)
                    .neutralText("Cancel")
                    .onPositive { _, _ ->

//                        val intent = Intent(this, CreatePost::class.java)
//                        intent.putParcelableArrayListExtra("imagesList", imagesList1)
//                        startActivity(intent)
                    }
//
//
                    .onNegative { _, _ ->

                        ImagePicker.with(this)
                            .setFolderMode(true)
                            .setFolderTitle("Album")
                            .setRootDirectoryName(Config.ROOT_DIR_DCIM)
                            .setDirectoryName("Image Picker")
                            .setMultipleMode(true)
                            .setShowNumberIndicator(true)
                            .setMaxSize(10)
                            .setLimitMessage("You can select up to 6 images")
//                .setSelectedImages(imagesList1)
                            .setRequestCode(pickImages)
                            .start();

                    }.show()

            } else {
                Log.d(myTag, "Error is : ")
            }

            if (ImagePicker.shouldHandleResult(requestCode, resultCode, data, pickImages2)) {
                imagesList1 = ImagePicker.getImages(data)


                MaterialDialog.Builder(this)
                    .title("Confirmation")
                    .content("Are you sure do you want to continue?")
                    .positiveText("Yes")
                    .negativeText("No")
                    .cancelable(false)
                    .canceledOnTouchOutside(false)
                    .neutralText("Cancel")
                    .onPositive { _, _ ->

                        val intent = Intent(this, CreateOffer::class.java)
                        intent.putParcelableArrayListExtra("imagesList", imagesList1)
                        startActivity(intent)
                    }
//
//
                    .onNegative { _, _ ->

                        ImagePicker.with(this)
                            .setFolderMode(true)
                            .setFolderTitle("Album")
                            .setRootDirectoryName(Config.ROOT_DIR_DCIM)
                            .setDirectoryName("Image Picker")
                            .setMultipleMode(true)
                            .setShowNumberIndicator(true)
                            .setMaxSize(10)
                            .setLimitMessage("You can select up to 6 images")
//                .setSelectedImages(imagesList1)
                            .setRequestCode(pickImages2)
                            .start();

                    }.show()

            } else {
                Log.d(myTag, "Error is : ")
            }

        }

    }


    private fun checkPermission(permission: String, requestCode: Int) {
        if (ContextCompat.checkSelfPermission(this@MainActivity2, permission) == PackageManager.PERMISSION_DENIED) {
            // Requesting the permission
            ActivityCompat.requestPermissions(this@MainActivity2, arrayOf(permission), requestCode)
        } else {
//            Toast.makeText(this@MainActivity, "Permission already granted", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == READ_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                Toast.makeText(this@MainActivity, "Read Permission Granted", Toast.LENGTH_SHORT).show()
                startPickImage2()
            } else {
                Toast.makeText(this@MainActivity2, "Read Permission Denied", Toast.LENGTH_SHORT).show()
            }
        } else if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                Toast.makeText(this@MainActivity, "Storage Permission Granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this@MainActivity2, "Storage Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        addPost = menu?.findItem(R.id.Post)

        return super.onCreateOptionsMenu(menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.Post) {
//            loadFragment(CreatePost())
            mBottomSheetDialog.show()
            return true
        } else if (id == R.id.notification) {
            val intent = Intent(this@MainActivity2, NotificationsActivity::class.java)
            startActivity(intent)
            return true
        } else if (id==R.id.SignOut){
            firebaseAuth.signOut()
            val intent = Intent(this@MainActivity2, RegisterActivity::class.java)
            startActivity(intent)
            finish()
            return true
        }

        return NavigationUI.onNavDestinationSelected(item, navController) || super.onOptionsItemSelected(item)
//        return super.onOptionsItemSelected(item)
    }

    private fun loadFragment(fragment: Fragment?): Boolean {
        if (fragment != null) {
            val fm = supportFragmentManager
            fm.beginTransaction()
                .add(R.id.container, fragment)
                .commit()
            return true
        }
        return false
    }

    override fun onRegisterClick() {
        TODO("Not yet implemented")
    }

    override fun onRecyclerViewGroupsItemClick(offerItem: OfferRoomEntity) {

        val fragment = GroupsOnOfferFragment()
        postId = offerItem.postId
        val bundle = Bundle()
        bundle.putParcelable("OfferItem", offerItem)
        fragment.arguments = bundle
        supportFragmentManager
            .beginTransaction()
            .addToBackStack("Groups On Offer Fragment")
            .add(R.id.container, fragment, "GroupsOnOffer")
            .commit()
    }

    override fun onPlaceOrderClick(offerItem: OfferRoomEntity, Total: String, Quantity: String) {

//        mViewModel.onPlaceOrder(offerItem,Total, Quantity)

        Log.d(myTag, "Total is$Total")
        Log.d(myTag, "Quantity is $Quantity")
        val currentUserId: String = firebaseAuth.currentUser?.uid!!

        MaterialDialog.Builder(this)
            .title("Confirm")
            .content("Confirm order request?")
            .positiveText("Yes")
            .negativeText("No")
            .cancelable(true)
            .canceledOnTouchOutside(false)
            .neutralText("Cancel")
            .onPositive { _, _ ->
                addOrder(offerItem.userId, currentUserId, offerItem.title, Total, Quantity,offerItem.image_url_0,offerItem.name)
//                    binding.refreshLayout.isRefreshing=true
//                mViewModel.deleteAll()
//                    binding.refreshLayout.isRefreshing=false


            }
            .onNegative { dialog, _ ->
                dialog.dismiss()
            }.show()

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
        TODO("Not yet implemented")
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
        TODO("Not yet implemented")
    }

    override fun onWaitingClicked(
        notificationItem: NotificationsRoomEntity,
        linearLayout: LinearLayout,
        linearLayout2: LinearLayout,
        linearLayout3: LinearLayout,
        linearLayout4: LinearLayout
    ) {
        TODO("Not yet implemented")
    }

    override fun onCurrentUserOfferClick(offerItem: OfferRoomEntity) {

        val bundle = Bundle()
        bundle.putParcelable("offerItem", offerItem)
        navController.navigate(R.id.action_viewPagerFragmentCurrentUserPosts_to_singleOfferFragment,bundle)

    }

    override fun onProfileOpenedDiscover(offerItem: OfferRoomEntity) {

        val bundle = Bundle()
        bundle.putParcelable("offerItem", offerItem)

        navController.navigate(R.id.action_searchFragment2_to_profileFragment, bundle)
    }

    override fun onJoinItemClick(userItem: UsersChatListEntity) {
        val userId: String = userItem.createdBy
        val currentUserId: String = firebaseAuth.currentUser?.uid!!

//        val postMap: MutableMap<String, Any?> = HashMap()
//        postMap[currentUserId]=true

        val fragment = GroupFragment()

        val bundle = Bundle()
        bundle.putParcelable("userItem", userItem)
        fragment.arguments = bundle


        firebaseFirestore.collection("Offers")
            .document(postId)
            .collection("Groups")
            .document(userId)
            .update("Members", FieldValue.arrayUnion(currentUserId))
            .addOnSuccessListener {

                val offerMap = HashMap<String, Any>()
                offerMap["postId"] = postId
                offerMap["groupId"] = userId

                firebaseFirestore.collection("Users")
                    .document(currentUserId)
                    .collection("Offers").document(postId).set(offerMap, SetOptions.merge())

                supportFragmentManager
                    .beginTransaction()
                    .addToBackStack("Groups Fragment")
                    .add(R.id.container, fragment)
                    .commit()
            }
    }

    override fun onGroupItemClick(userItem: UsersChatListEntity) {

        GROUP_USER_ID = userItem.id
        val intent = Intent(this@MainActivity2, ChatActivity::class.java)
        intent.putExtra("openChat", "openUserChat")
        intent.putExtra("chatsOpened", "fromMessages")
        intent.putExtra("userItem", userItem)
        startActivity(intent)

    }

    override fun onGroupItemClick(userItem: Users) {
        GROUP_USER_ID = userItem.userId

        val intent = Intent(this@MainActivity2, ChatActivity::class.java)
        intent.putExtra("openChat", "openUserChat")
        intent.putExtra("chatsOpened", "fromGroups")
        intent.putExtra("userItem", userItem)
        startActivity(intent)

    }

    override fun onLocationClick() {

        Log.d(myTag, "Button Clicked")
//        https://maps.app.goo.gl/uRkAJghUgeBNMC6n8

        try {
            val uri: String =
                java.lang.String.format(Locale.ENGLISH, "https://maps.app.goo.gl/uRkAJghUgeBNMC6n8")
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
            startActivity(intent)
        } catch (e: Exception) {
            Log.d(myTag, "" + e.message + " error is " + e.cause)
            Toasty.normal(this, "Can't access the location", Toasty.LENGTH_SHORT).show()
        }

    }

    override fun onSendFriendReqClick(userItem: Users) {
        val currentUserId: String = firebaseAuth.currentUser?.uid!!
        addUser(userItem, currentUserId)
    }

    override fun onNotificationItemClick(notificationsRoomEntityItem: NotificationsRoomEntity) {
        TODO("Not yet implemented")
    }

    override fun onSearchPeopleItemClick(userItem: Users) {

        val fragment = SearchPeopleContainer()
        val fm = supportFragmentManager
        val bundle = Bundle()
        bundle.putString("openFriend", "openFriendSearch")
        bundle.putParcelable("userItem", userItem)
        fragment.arguments = bundle
        loadFragment(fragment)

    }

    override fun onBookMarkItemClick(offerItem: OfferRoomEntity) {

        val localDatabase: AppDatabase = AppDatabase.getInstance(this)!!

//        val mapper = ObjectMapper() // jackson's objectmapper
//        val pojo: MyPojo = mapper.convertValue(map, MyPojo::class.java)

        val offerMap = HashMap<String, Any>()
        offerMap["postId"] = offerItem.postId
        offerMap["username"] = offerItem.username


        firebaseAuth.currentUser?.uid?.let { it1 ->
            firebaseFirestore
                .collection("Users")
                .document(it1)
                .collection("Saved")
                .document(offerItem.postId.trim())
                .get().addOnSuccessListener { it2 ->
                    if (!it2.exists()) {
                        firebaseAuth.currentUser?.uid?.let {
                            firebaseFirestore
                                .collection("Users")
                                .document(it)
                                .collection("Saved")
                                .document(offerItem.postId.trim())
                                .set(offerMap, SetOptions.merge()).addOnSuccessListener {
                                    firebaseFirestore
                                        .collection("Offers")
                                        .document(offerItem.postId)
                                        .get().addOnSuccessListener {
                                            val offerPojo: OffersSavedRoomEntity? =
                                                it.toObject(OffersSavedRoomEntity::class.java)
                                            CoroutineScope(Dispatchers.IO).launch {
                                                if (offerPojo != null) {
                                                    localDatabase.appDao()?.addOffer(offerPojo)
                                                }
                                            }
                                        }


                                    Toasty.success(this, "Saved", Toasty.LENGTH_SHORT, true).show()
                                }

                        }
                    } else {
                        firebaseAuth.currentUser?.uid?.let {
                            firebaseFirestore
                                .collection("Users")
                                .document(it)
                                .collection("Saved")
                                .document(offerItem.postId.trim())
                                .delete().addOnSuccessListener {

                                    CoroutineScope(Dispatchers.IO).launch {
                                        localDatabase.appDao()?.deleteOfferById(offerItem.postId)
                                    }
                                    Toasty.normal(this, "Removed", Toasty.LENGTH_SHORT).show()

                                }
                        }

                    }

                }
        }
    }

    override fun onGroupOpenFromMessages(userItem: UsersChatListEntity) {
        val intent = Intent(this, ChatActivity::class.java)
        intent.putExtra("groupItem", userItem)
        intent.putExtra("openChat", "openGroupChat")
        startActivity(intent)
    }

    private fun addOrder(
        userItem: String,
        currentUserId: String,
        itemName: String,
        price: String,
        Quantity: String,
        imageUrl0: String,
        userNameOffer: String
    ) {

        if (userItem != currentUserId) {
            executeOrder(userItem, currentUserId, itemName, price, Quantity,imageUrl0,userNameOffer)
        } else {
            Toasty.error(this, "Error", Toasty.LENGTH_SHORT, true).show()
        }
    }

    private fun addUser(userItem: Users, currentUserId: String) {
        firebaseFirestore
            .collection("Users")
            .document(userItem.userId)
            .collection("Friends")
            .document(currentUserId)
            .get()
            .addOnSuccessListener { documentSnapshot: DocumentSnapshot ->
                if (!documentSnapshot.exists()) {

                    firebaseFirestore
                        .collection("Users")
                        .document(userItem.userId)
                        .collection("Friend_Requests")
                        .document(currentUserId)
                        .get()
                        .addOnSuccessListener { documentSnapshot1: DocumentSnapshot ->
//                            if (holderr.friend_icon.getVisibility() != View.VISIBLE) {
                            if (!documentSnapshot1.exists()) {
                                executeFriendReq(userItem, currentUserId)
                            } else {
                                Toasty.normal(
                                    this@MainActivity2,
                                    "Friend Request already Sent",
                                    Toasty.LENGTH_SHORT
                                ).show()
                            }
//                            }
//                            else {
//                                Snackbar.make(view, usersList.get(position).getName()
//                                    .toString() + " is already your friend", Snackbar.LENGTH_LONG).show()
//                                notifyDataSetChanged()
                        }
                }
            }
//        else {
//                    usersList.removeAt(position)
//                    notifyDataSetChanged()
//                }
    }
//    }


    private fun executeOrder(
        userItem: String,
        currentUserId: String,
        itemName: String,
        price: String,
        Quantity: String,
        imageUrl0: String,
        userNameOffer: String
    ) {

        idOrder = firebaseFirestore.collection("Users")
            .document(userItem)
            .collection("Orders").document().id

        val messageText: String = "Order Request"
        val type: String = "order_req"

        val userMap = HashMap<String, Any?>()
        FirebaseFirestore.getInstance()
            .collection("Users")
            .document(currentUserId)
            .get()
            .addOnSuccessListener { documentSnapshot: DocumentSnapshot ->
                val email = documentSnapshot.getString("email")
                val thumbnailImage=documentSnapshot.getString("thumbnailImage")
                userMap["username"] = documentSnapshot.getString("username")
                userMap["id"] = documentSnapshot.getString("id")
                userMap["email"] = email
                userMap["image"] = documentSnapshot.getString("image")
                userMap["thumbnailImage"] =documentSnapshot.getString("thumbnailImage")
                userMap["userNameOffer"] = userNameOffer
                userMap["notification_id"] = System.currentTimeMillis().toString()
                userMap["orderName"] = itemName
                userMap["status"] = "Pending"
                userMap["message"] = messageText
                userMap["type"] = type
                userMap["price"] = price
                userMap["Quantity"] = Quantity
                userMap["orderFrom"] = currentUserId
                userMap["userId"] = currentUserId
                userMap["orderTo"] = userItem
                userMap["foodImage"] = imageUrl0
                userMap["idOrder"] = idOrder
                userMap["timestamp"] = FieldValue.serverTimestamp()

                firebaseFirestore
                    .collection("Users")
                    .document(userItem)
                    .collection("Orders")
                    .document(idOrder!!)
                    .set(userMap, SetOptions.merge()).continueWith {
                        firebaseFirestore
                            .collection("Users")
                            .document(currentUserId)
                            .collection("Orders")
                            .document(idOrder!!).set(userMap, SetOptions.merge())
                    }
                    .addOnSuccessListener {

                        firebaseFirestore
                            .collection("Users")
                            .document(userItem)
                            .collection("Orders").document(idOrder!!).get().addOnSuccessListener {

                                val ordersRoomEntity: OrdersRoomEntity? = it?.toObject(OrdersRoomEntity::class.java)

                                val recentMap = HashMap<String, Any?>()
                                recentMap["username"] = documentSnapshot.getString("username")
                                recentMap["id"] = documentSnapshot.getString("id")
                                recentMap["email"] = email
                                recentMap["image"] = documentSnapshot.getString("image")
                                recentMap["thumbnailImage"] =documentSnapshot.getString("thumbnailImage")
                                recentMap["foodImage"] = imageUrl0
                                recentMap["userNameOffer"] = userNameOffer
                                recentMap["notification_id"] = System.currentTimeMillis().toString()
                                recentMap["orderName"] = itemName
                                recentMap["status"] = "Pending"
                                recentMap["message"] = messageText
                                recentMap["type"] = type
                                recentMap["price"] = price
                                recentMap["Quantity"] = Quantity
                                recentMap["orderFrom"] = currentUserId
                                recentMap["userId"] = currentUserId
                                recentMap["orderTo"] = userItem
                                recentMap["idOrder"] = idOrder
                                if (ordersRoomEntity != null) {
                                    recentMap["timestamp"] = ordersRoomEntity.timestamp
                                }
                                firebaseFirestore
                                    .collection("Users")
                                    .document(userItem)
                                    .collection("Orders").document("recentOrder").set(recentMap).continueWith {
                                        firebaseFirestore
                                            .collection("Users")
                                            .document(currentUserId)
                                            .collection("Orders").document("recentOrder").set(recentMap)
                                    }

                                documentSnapshot.getString("username")?.let { it1 ->
                                    if (ordersRoomEntity != null) {
                                        ordersRoomEntity.timestamp?.let { it2 ->
                                            addToNotification(
                                                userItem, currentUserId,
                                                it1,
                                                messageText,
                                                type, itemName, price, Quantity, it2,imageUrl0,userNameOffer,thumbnailImage
                                            )
                                        }
                                    }
                            }
                        }

                        Toasty.success(
                            this@MainActivity2,
                            "Order Request Sent",
                            Toasty.LENGTH_SHORT
                        ).show()
                    }.addOnFailureListener { e: java.lang.Exception ->
//                        holder.progressBar.setVisibility(View.GONE)
                        Log.e("Error", e.message!!)
                    }
            }

    }

    private fun executeFriendReq(userItem: Users, currentUserId: String) {

        idOrder = firebaseFirestore.collection("Users")
            .document(userItem.userId)
            .collection("Friend_Requests").document().id

        val userMap = HashMap<String, Any?>()
        firebaseFirestore
            .collection("Users")
            .document(currentUserId)
            .get()
            .addOnSuccessListener { documentSnapshot: DocumentSnapshot ->
                val email = documentSnapshot.getString("email")
                userMap["name"] = documentSnapshot.getString("name")
                userMap["id"] = documentSnapshot.getString("id")
                userMap["email"] = email
                userMap["image"] = documentSnapshot.getString("image")
                userMap["tokens"] = documentSnapshot["token_ids"]
                userMap["notification_id"] = System.currentTimeMillis().toString()
                userMap["timestamp"] = FieldValue.serverTimestamp()

                //Add to user
                FirebaseFirestore.getInstance()
                    .collection("Users")
                    .document(userItem.userId)
                    .collection("Friend_Requests")
                    .document(idOrder!!)
                    .set(userMap)
                    .addOnSuccessListener {

//                        documentSnapshot.getString("name")?.let { it1 ->
//                            addToNotification(
//                                userItem.id, currentUserId,
//                                //                                documentSnapshot.getString("image"),
//                                it1,
//                                "Sent you friend request",
//                                "friend_req"
//                            )
//                        }

                        Toasty.success(
                            this@MainActivity2,
                            "Friend Request Sent",
                            Toasty.LENGTH_SHORT
                        ).show()
                    }.addOnFailureListener { e: java.lang.Exception ->
//                        holder.progressBar.setVisibility(View.GONE)
                        Log.e("Error", e.message!!)
                    }
            }
    }


    private fun addToNotification(
        UserItemId: String,
        currentUserId: String,
//        profile: String,
        username: String,
        message: String,
        type: String,
        itemName: String = "",
        price: String = "",
        Quantity: String = "1",
        timestamp: Date,
        imageUrl0: String,
        userNameOffer: String,
        thumbnailImage: String?
    ) {
        val map: MutableMap<String, Any> = java.util.HashMap()
        map["id"] = currentUserId
        map["username"] = username
//        map["image"] = profile
        map["message"] = message
        map["timestamp"] = timestamp
        map["type"] = type
        map["thumbnailImage"]=thumbnailImage.toString()
        map["action_id"] = currentUserId
        if (type == "order_req") {
            map["orderName"] = itemName
            map["status"] = "Pending"
            map["price"] = price
            map["userNameOffer"] =userNameOffer
            map["foodImage"] = imageUrl0
            map["orderFrom"] = currentUserId
            map["userId"] = currentUserId
            map["orderTo"] = UserItemId
            map["idOrder"] = idOrder.toString()
            map["Quantity"] = Quantity
        }
        if (UserItemId != currentUserId) {

            if (type == "order_req") {

                idOrder?.let {
                    firebaseFirestore.collection("Users")
                        .document(UserItemId)
                        .collection("Info_Notifications")
                        .document(it)
                        .set(map, SetOptions.merge()).addOnSuccessListener {

                            Toasty.success(this, "Order request Sent.", Toasty.LENGTH_SHORT, true)
                                .show()
                        }
                        .addOnFailureListener(OnFailureListener { e: java.lang.Exception ->
                            Log.e("Error",""+e.localizedMessage)
                        })
                }

            } else {

                firebaseFirestore.collection("Users")
                    .document(UserItemId)
                    .collection("Info_Notifications")
                    .document(idOrder!!)
                    .set(map, SetOptions.merge()).addOnSuccessListener {
                        Toasty.success(this, "Friend request Sent.", Toasty.LENGTH_SHORT, true)
                            .show()
                    }.addOnFailureListener {
                        Log.e("Error", "" + it.localizedMessage)
                    }
            }
        }
    }
}


