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
import com.android.testproject1.model.Notifications
import com.android.testproject1.model.Offer
import com.android.testproject1.model.Users
import com.android.testproject1.room.enteties.AppDatabase
import com.android.testproject1.room.enteties.OffersSavedRoomEntity
import com.android.testproject1.room.enteties.UsersChatListEntity
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
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
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController


//import android.R

//DuoMenuView.OnMenuClickListener
class MainActivity2 : AppCompatActivity(), IMainActivity {

    companion object {
        private const val READ_PERMISSION_CODE = 100
        private const val STORAGE_PERMISSION_CODE = 101
        private var GROUP_USER_ID:String?=null
    }

    private lateinit var firebaseFirestore: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth
    var postId:String=""
    val myTag = "MyTag"
    private var imagesList1: ArrayList<Image> = java.util.ArrayList()
    private val pickImages = 102
    private val pickImages2 = 103
    private var serviceCount = 0

    private var addPost: MenuItem? = null
    var checkTabDashboard:Boolean?=null

    private lateinit var mNavDrawer: DrawerLayout
    private var mTitles: ArrayList<String> = ArrayList()
    private lateinit var mMenuAdapter: MenuAdapter

    private lateinit var mBottomSheetDialog:BottomSheetDialog

    var stringCheck:String?=null

    var doubleBackToExitPressedOnce = false
    private lateinit var navController: NavController
    private val fragment1: Fragment = SearchFragment()
    private val fragment2: Fragment = MessagesFragment()
    private val fragment3: Fragment = ProfileFragment()
    private val fm: FragmentManager = supportFragmentManager
    var active = fragment1
    private lateinit var navigation: BottomNavigationView
    var idOrder:String?=null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        setSupportActionBar(toolbar)
        toolbar.title="Voila"

        stringCheck=intent.getStringExtra("openFriend")
        val notificationsItem=intent.getParcelableExtra<Notifications>("notificationsItem")

        if (stringCheck=="openFriend"){

            val fragment = SearchPeopleContainer()
            val fm = supportFragmentManager
            val bundle = Bundle()
            bundle.putString("openFriend","openFriend")
            bundle.putParcelable("notificationsItem",notificationsItem)
            toolbar.title = "Search"

            fragment.arguments=bundle
            fm.beginTransaction()
                .replace(R.id.container, fragment)
                .commit()

        }else{
//            loadFragment(HomeFragment())
//            loadFragment(SearchFragment())
        }

        firebaseAuth= FirebaseAuth.getInstance()
        firebaseFirestore= FirebaseFirestore.getInstance()


        checkPermission(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            STORAGE_PERMISSION_CODE
        )
        checkPermission(
            Manifest.permission.READ_EXTERNAL_STORAGE,
            READ_PERMISSION_CODE
        )


        
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
            startPickImage2()
//            val intent=Intent(this,CreateOffer::class.java)
//            startActivity(intent)
        }

//        navigation = bottomNav
//        setFragment(fragment1, "1", 0);
//
//        val bottomNavigationView=bottomNav

//        bottomNavigationView.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
//            when (menuItem.itemId)
//            {
//                R.id.search -> {
//                    setFragment(fragment1, "1", 0)
//                    return@OnNavigationItemSelectedListener true
//                }
//                R.id.notifications -> {
//                    setFragment(fragment2, "2", 1)
//                    return@OnNavigationItemSelectedListener true
//                }
//                R.id.profile -> {
//                    setFragment(fragment3, "3", 2)
//                    return@OnNavigationItemSelectedListener true
//                }
//            }
//            false
//        })


//        val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
//                when (item.itemId) {
//                    R.id.search -> {
//                        setFragment(fragment1, "1", 0)
//                        return@OnNavigationItemSelectedListener true
//                    }
//                    R.id.notifications -> {
//                        setFragment(fragment2, "2", 1)
//                        return@OnNavigationItemSelectedListener true
//                    }
//                    R.id.profile -> {
//                        setFragment(fragment3, "3", 2)
//                        return@OnNavigationItemSelectedListener true
//                    }
//                }
//                false
//            }

        val bottomNavigation=bottomNav
        val navHost = supportFragmentManager.findFragmentById(R.id.fragmentContainerView2) as NavHostFragment
        navController = navHost.navController
        NavigationUI.setupWithNavController(toolbar, navController)
        bottomNavigation.setupWithNavController(navController)
//        bottomNavigation.setOnNavigationItemReselectedListener {
//            "Reselect blocked."
//        }

//        toolbar.setNavigationOnClickListener {
//            when (navController.currentDestination?.id) {
//                R.id.search, R.id.notifications, R.id.profile -> {
//                    if (onBackPressedDispatcher.hasEnabledCallbacks())
//                        onBackPressedDispatcher.onBackPressed()
//                    else
//                        navController.navigateUp()
//                }
//                else -> navController.navigateUp()
//            }
//        }









        checkTabDashboard=true

//        Home.setOnClickListener {
//            loadFragment(Dashboard())
//            toolbar.title = "Home"
//            addPost?.isVisible = true
//            drawer.closeDrawer()
//        }
//        Discover.setOnClickListener {
//
//            val fragment = Dashboard()
//            val fm = supportFragmentManager
//            val bundle = Bundle()
//            bundle.putString("bottom_nav","1")
//            toolbar.title = "Discover"
//            addPost?.isVisible = true
//
//            fragment.arguments=bundle
//                fm.beginTransaction()
//                    .replace(R.id.container, fragment)
//                    .commit()
//            drawer.closeDrawer()
//        }
//        Search.setOnClickListener {
//            val fragment = SearchPeopleContainer()
//            val fm = supportFragmentManager
////            val bundle = Bundle()
////            bundle.putString("bottom_nav","1")
////            toolbar.title = "Discover"
////            addPost?.isVisible = true
//            toolbar.title = "Search"
////            fragment.arguments=bundle
//            fm.beginTransaction()
//                .replace(R.id.container, fragment)
//                .commit()
//            drawer.closeDrawer()
//        }
//        Friends.setOnClickListener {
//            val fragment = Friends()
//            val fm = supportFragmentManager
////            val bundle = Bundle()
////            bundle.putString("bottom_nav","1")
////            toolbar.title = "Discover"
////            addPost?.isVisible = true
//
////            fragment.arguments=bundle
//            fm.beginTransaction()
//                .replace(R.id.container, fragment)
//                .commit()
//            drawer.closeDrawer()
//        }
//        Messages.setOnClickListener {
//            loadFragment(MessagesFragment())
//            toolbar.title = "Chats"
//            addPost?.isVisible = false
//            drawer.closeDrawer()
//        }
//        Profile.setOnClickListener {
//            loadFragment(ProfileFragment())
//            toolbar.title = "Profile"
//            addPost?.isVisible = false
//            drawer.closeDrawer()
//        }


//        val duoDrawerToggle = DuoDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
//        drawer.setDrawerListener(duoDrawerToggle)
//        duoDrawerToggle.syncState()
//        mMenuAdapter = MenuAdapter(mTitles);
//        duoMenuView.setOnMenuClickListener(this);
//        duoMenuView.adapter = mMenuAdapter

    }

//    private fun initNavigation() {
//        val navHost = supportFragmentManager.findFragmentById(R.id.container) as NavHostFragment
//        navController = navHost.navController
//        NavigationUI.setupWithNavController(binding.toolbar, navController)
//        binding.bottomNavigation.setupWithNavController(navController)
//        binding.bottomNavigation.setOnNavigationItemReselectedListener {
//            "Reselect blocked."
//        }
//
//        binding.toolbar.setNavigationOnClickListener {
//            when (navController.currentDestination?.id) {
//                R.id.searchFragment, R.id.gamesFragment, R.id.notificationsFragment -> {
//                    if (onBackPressedDispatcher.hasEnabledCallbacks())
//                        onBackPressedDispatcher.onBackPressed()
//                    else
//                        navController.navigateUp()
//                }
//                else -> navController.navigateUp()
//            }
//        }
//    }

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

                    val intent=Intent(this,CreatePost::class.java)
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

                        val intent=Intent(this,CreateOffer::class.java)
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

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == READ_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                Toast.makeText(this@MainActivity, "Read Permission Granted", Toast.LENGTH_SHORT).show()
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
        menuInflater.inflate(R.menu.menu_main,menu)
        addPost=menu?.findItem(R.id.Post)

        return super.onCreateOptionsMenu(menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.Post) {
//            loadFragment(CreatePost())
            mBottomSheetDialog.show()
        }else if (id==R.id.notification){
            val intent=Intent(this@MainActivity2,NotificationsActivity::class.java)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }



    override fun onBackPressed() {
        super.onBackPressed()
//        if (drawer.isDrawerOpen) {
//            drawer.closeDrawer()
//        } else {
//            super.onBackPressed()
//        }

//        if (active == fragment1) {
//            if (doubleBackToExitPressedOnce) {
//                super.onBackPressed();
//                return;
//            }
//            this.doubleBackToExitPressedOnce = true;
//            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
//        } else {
//            setFragment(fragment1, "1", 0);
//        }

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

    override fun onRecyclerViewItemClick(offerItem: Offer) {
//        val fragment = DetailsFragment()
        val fragment = GroupsOnOfferFragment()

        postId=offerItem.postId

        val bundle = Bundle()
        bundle.putParcelable("OfferItem",offerItem)
        fragment.arguments=bundle


        supportFragmentManager
            .beginTransaction()
            .addToBackStack("Groups On Offer Fragment")
            .add(R.id.container, fragment, "GroupsOnOffer")
            .commit()
    }

    override fun onPlaceOrderClick(offerItem: Offer) {

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
                addOrder(offerItem.userId,currentUserId,offerItem.title)
//                    binding.refreshLayout.isRefreshing=true
//                mViewModel.deleteAll()
//                    binding.refreshLayout.isRefreshing=false


            }
            .onNegative { dialog, _ ->
                dialog.dismiss()
            }.show()




    }

    override fun onJoinItemClick(userItem: UsersChatListEntity) {
        val userId: String = userItem.createdBy
        val currentUserId: String = firebaseAuth.currentUser?.uid!!

//        val postMap: MutableMap<String, Any?> = HashMap()
//        postMap[currentUserId]=true

        val fragment=GroupFragment()

        val bundle = Bundle()
        bundle.putParcelable("userItem",userItem)
        fragment.arguments=bundle


        firebaseFirestore.collection("Offers")
            .document(postId)
            .collection("Groups")
            .document(userId)
            .update("Members", FieldValue.arrayUnion(currentUserId))
            .addOnSuccessListener {

                val offerMap=HashMap<String,Any>()
                offerMap["postId"]=postId
                offerMap["groupId"]=userId

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

        GROUP_USER_ID =userItem.id
        val intent =Intent(this@MainActivity2,ChatActivity::class.java)
        intent.putExtra("openChat","openUserChat")
        intent.putExtra("chatsOpened","fromMessages")
        intent.putExtra("userItem",userItem)
        startActivity(intent)

    }

    override fun onGroupItemClick(userItem: Users) {
        GROUP_USER_ID =userItem.id

        val intent =Intent(this@MainActivity2,ChatActivity::class.java)
        intent.putExtra("openChat","openUserChat")
        intent.putExtra("chatsOpened","fromGroups")
        intent.putExtra("userItem",userItem)
        startActivity(intent)

//        val fragment=ChatFragment()
//        val bundle = Bundle()
//        bundle.putParcelable("userItem",userItem)
//        fragment.arguments=bundle
//
//        supportFragmentManager
//            .beginTransaction()
//            .add(R.id.container, fragment)
//            .commit()
    }

    override fun onLocationClick() {

        Log.d(myTag,"Button Clicked")
//        https://maps.app.goo.gl/uRkAJghUgeBNMC6n8

        try{
        val uri: String = java.lang.String.format(Locale.ENGLISH, "https://maps.app.goo.gl/uRkAJghUgeBNMC6n8")
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
        startActivity(intent)
        }catch (e :Exception){
            Log.d(myTag,""+e.message+" error is "+e.cause)
            Toasty.normal(this,"Can't access the location",Toasty.LENGTH_SHORT).show()
        }

    }

    override fun onSendFriendReqClick(userItem: Users) {
        val currentUserId: String = firebaseAuth.currentUser?.uid!!
        addUser(userItem,currentUserId)
    }

    override fun onNotificationItemClick(notificationsItem: Notifications) {
        TODO("Not yet implemented")
    }

    override fun onSearchPeopleItemClick(userItem: Users) {

            val fragment = SearchPeopleContainer()
            val fm = supportFragmentManager
        val bundle = Bundle()
        bundle.putString("openFriend", "openFriendSearch")
        bundle.putParcelable("userItem",userItem)
        fragment.arguments=bundle
        loadFragment(fragment)

    }

    override fun onBookMarkItemClick(offerItem: Offer) {

        val localDatabase: AppDatabase = AppDatabase.getInstance(this)!!

//        val mapper = ObjectMapper() // jackson's objectmapper
//        val pojo: MyPojo = mapper.convertValue(map, MyPojo::class.java)

        val offerMap=HashMap<String,Any>()
        offerMap["postId"]=offerItem.postId
        offerMap["username"]=offerItem.username


        firebaseAuth.currentUser?.uid?.let { it1 ->
            firebaseFirestore
                .collection("Users")
                .document(it1)
                .collection("Saved")
                .document(offerItem.postId.trim())
                .get().addOnSuccessListener { it2 ->
                    if (!it2.exists()){
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
                                            val offerPojo: OffersSavedRoomEntity? = it.toObject(OffersSavedRoomEntity::class.java)
                                            CoroutineScope(Dispatchers.IO).launch {
                                                if (offerPojo != null) {
                                                    localDatabase.appDao()?.addOffer(offerPojo)
                                                }
                                            }
                                        }


                                    Toasty.success(this,"Saved",Toasty.LENGTH_SHORT,true).show()
                                }

                        }
                    }else{
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
                                    Toasty.normal(this,"Removed",Toasty.LENGTH_SHORT).show()

                                }
                        }

                    }

                }
        }
    }

    override fun onGroupOpenFromMessages(userItem: UsersChatListEntity) {
        val intent=Intent(this,ChatActivity::class.java)
        intent.putExtra("groupItem",userItem)
        intent.putExtra("openChat","openGroupChat")
        startActivity(intent)
    }

    private fun addOrder(userItem: String, currentUserId: String,itemName:String){

//        firebaseFirestore
//            .collection("Users")
//            .document(userItem.id)
//            .collection("Friends")
//            .document(currentUserId)
//            .get()
//            .addOnSuccessListener { documentSnapshot: DocumentSnapshot ->
//                if (!documentSnapshot.exists()) {


        executeOrder(userItem,currentUserId,itemName)

//                    firebaseFirestore
//                        .collection("Users")
//                        .document(userItem)
//                        .collection("Orders")
//                        .document(currentUserId)
//                        .get()
//                        .addOnSuccessListener { documentSnapshot1: DocumentSnapshot ->
////                            if (holderr.friend_icon.getVisibility() != View.VISIBLE) {
//                            if (!documentSnapshot1.exists()) {
//                                executeOrder(userItem,currentUserId)
//                            } else {
//                                Toasty.normal(this@MainActivity2,"Order already Sent",Toasty.LENGTH_SHORT).show()
//                            }
////                            }
////                            else {
////                                Snackbar.make(view, usersList.get(position).getName()
////                                    .toString() + " is already your friend", Snackbar.LENGTH_LONG).show()
////                                notifyDataSetChanged()
//                        }

    //                }
//            }

    //        else {
//                    usersList.removeAt(position)
//                    notifyDataSetChanged()
//                }

    }


    private fun addUser(userItem: Users,currentUserId: String) {
        firebaseFirestore
            .collection("Users")
            .document(userItem.id)
            .collection("Friends")
            .document(currentUserId)
            .get()
            .addOnSuccessListener { documentSnapshot: DocumentSnapshot ->
                if (!documentSnapshot.exists()) {

                    firebaseFirestore
                        .collection("Users")
                        .document(userItem.id)
                        .collection("Friend_Requests")
                        .document(currentUserId)
                        .get()
                        .addOnSuccessListener { documentSnapshot1: DocumentSnapshot ->
//                            if (holderr.friend_icon.getVisibility() != View.VISIBLE) {
                                if (!documentSnapshot1.exists()) {
                                    executeFriendReq(userItem,currentUserId)
                                } else {
                                     Toasty.normal(this@MainActivity2,"Friend Request already Sent",Toasty.LENGTH_SHORT).show()
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


    private fun executeOrder(userItem: String, currentUserId: String,itemName:String){

        val userMap = HashMap<String, Any?>()
        FirebaseFirestore.getInstance()
            .collection("Users")
            .document(currentUserId)
            .get()
            .addOnSuccessListener { documentSnapshot: DocumentSnapshot ->
                val email = documentSnapshot.getString("email")
                userMap["name"] = documentSnapshot.getString("name")
                userMap["id"] = documentSnapshot.getString("id")
                userMap["email"] = email
                userMap["image"] = documentSnapshot.getString("image")
//                userMap["tokens"] = documentSnapshot["token_ids"]
                userMap["notification_id"] = System.currentTimeMillis().toString()
                userMap["orderName"] =itemName
                userMap["timestamp"] = System.currentTimeMillis().toString()

               idOrder= firebaseFirestore.collection("Users")
                    .document(userItem)
                    .collection("Orders").document().id
                //Add to user
                FirebaseFirestore.getInstance()
                    .collection("Users")
                    .document(userItem)
                    .collection("Orders")
//                    .document(currentUserId)
                    .document(idOrder!!)
                    .set(userMap, SetOptions.merge())
                    .addOnSuccessListener {

                        documentSnapshot.getString("name")?.let { it1 ->
                            addToNotification(userItem, currentUserId,
                                //                                documentSnapshot.getString("image"),
                                it1,
                                "Sent you order request for",
                                "order_req",itemName
                            )
                        }

                        Toasty.success(this@MainActivity2,"Order Request Sent",Toasty.LENGTH_SHORT).show()
                    }.addOnFailureListener { e: java.lang.Exception ->
//                        holder.progressBar.setVisibility(View.GONE)
                        Log.e("Error", e.message!!)
                    }
            }

    }

    private fun executeFriendReq(userItem: Users,currentUserId: String) {
        val userMap = HashMap<String, Any?>()
        FirebaseFirestore.getInstance()
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
                userMap["timestamp"] = System.currentTimeMillis().toString()

                //Add to user
                FirebaseFirestore.getInstance()
                    .collection("Users")
                    .document(userItem.id)
                    .collection("Friend_Requests")
                    .document(currentUserId)
                    .set(userMap)
                    .addOnSuccessListener {

                        documentSnapshot.getString("name")?.let { it1 ->
                            addToNotification(userItem.id, currentUserId,
                    //                                documentSnapshot.getString("image"),
                                it1,
                                "Sent you friend request",
                                "friend_req"
                            )
                        }

                        Toasty.success(this@MainActivity2,"Friend Request Sent",Toasty.LENGTH_SHORT).show()
                    }.addOnFailureListener { e: java.lang.Exception ->
//                        holder.progressBar.setVisibility(View.GONE)
                        Log.e("Error", e.message!!)
                    }
            }
    }


    private fun addToNotification(
        UserItemId: String,
        user_id: String,
//        profile: String,
        username: String,
        message: String,
        type: String,
        itemName: String=""
    ) {
        val map: MutableMap<String, Any> = java.util.HashMap()
        map["id"] = user_id
        map["username"] = username
//        map["image"] = profile
        map["message"] = message
        map["timestamp"] = System.currentTimeMillis().toString()
        map["type"] = type
        map["action_id"] = user_id
        if(type=="order_req"){
            map["orderName"] =itemName
        }
        if (UserItemId != user_id) {

            if (type=="order_req"){

                idOrder?.let {
                    firebaseFirestore.collection("Users")
                        .document(UserItemId)
                        .collection("Info_Notifications")
                        .document(it)
                        .set(map, SetOptions.merge()).addOnSuccessListener {

                            Toasty.success(this, "Order request sent.", Toasty.LENGTH_SHORT,true).show()
                        }
                        .addOnFailureListener(OnFailureListener { e: java.lang.Exception ->
                            Log.e("Error", e.localizedMessage)
                        })
                }

            }else{

                firebaseFirestore.collection("Users")
                    .document(UserItemId)
                    .collection("Info_Notifications")
                    .add(map)
                    .addOnSuccessListener(OnSuccessListener { documentReference: DocumentReference? ->
                        if (type == "friend_req") {
//                        req_sent.setText("Friend request sent")
                            Toasty.success(this, "Friend request sent.", Toasty.LENGTH_SHORT,true).show()
                        }
//                    else if (type == "order_req"){
//                        Toasty.success(this, "Order request sent.", Toasty.LENGTH_SHORT,true).show()
//                    }
//                    else { Toasty.success(this, "Friend request accepted", Toasty.LENGTH_SHORT, true).show()
//                    }
//                    if (type == "order_req") {
////                        req_sent.setText("Friend request sent")
//                        Toasty.success(this, "Order request sent.", Toasty.LENGTH_SHORT,true).show()
//                    }
//                    else { Toasty.success(this, "Order request accepted", Toasty.LENGTH_SHORT, true).show()
//                    }
                    })
                    .addOnFailureListener(OnFailureListener { e: java.lang.Exception ->
                        Log.e("Error", e.localizedMessage)
                    })

            }
//            firebaseFirestore.collection("Users")
//                .document(UserItemId)
//                .collection("Info_Notifications")
//                .add(map)
//                .addOnSuccessListener(OnSuccessListener { documentReference: DocumentReference? ->
//                    if (type == "friend_req") {
////                        req_sent.setText("Friend request sent")
//                        Toasty.success(this, "Friend request sent.", Toasty.LENGTH_SHORT,true).show()
//                    }
////                    else if (type == "order_req"){
////                        Toasty.success(this, "Order request sent.", Toasty.LENGTH_SHORT,true).show()
////                    }
////                    else { Toasty.success(this, "Friend request accepted", Toasty.LENGTH_SHORT, true).show()
////                    }
////                    if (type == "order_req") {
//////                        req_sent.setText("Friend request sent")
////                        Toasty.success(this, "Order request sent.", Toasty.LENGTH_SHORT,true).show()
////                    }
////                    else { Toasty.success(this, "Order request accepted", Toasty.LENGTH_SHORT, true).show()
////                    }
//                })
//                .addOnFailureListener(OnFailureListener { e: java.lang.Exception ->
//                    Log.e("Error", e.localizedMessage)
//                })
        }
    }



//    override fun onFooterClicked() {}
//
//    override fun onHeaderClicked() {}
//
//    override fun onOptionClicked(position: Int, objectClicked: Any?) {
////        title = mTitles[position]
////        Log.d("MyTag", "Positin Clicked $position")
////
////        // Set the right options selected
////
////        // Set the right options selected
////        mMenuAdapter.setViewSelected(position, true)
////
////        // Navigate to the right fragment
////        when (position) {
////            0 -> loadFragment(HomeFragment())
////            1 -> loadFragment(SearchFragment())
////        }
////        drawer.closeDrawer()
//    }
}



    //        mNavDrawer = drawer_layout
//        val navigationView = findViewById<NavigationView>(R.id.navigation_view)
//
//        val toggle = ActionBarDrawerToggle(
//            this, mNavDrawer, toolbar,
//            R.string.navigation_drawer_open,
//            R.string.navigation_drawer_close
//        )
//
//        mNavDrawer.addDrawerListener(toggle)
//
//        toggle.syncState()
//        navigationView.setNavigationItemSelectedListener(this)
//
//        if (savedInstanceState == null) {
//            supportFragmentManager.beginTransaction()
//                .replace(R.id.container, Dashboard())
//                .commit()
//            navigationView.setCheckedItem(R.id.nav_dashboard)
//        }


//    override fun onNavigationItemSelected(item: MenuItem): Boolean {
//
//        when (item.itemId) {
//            R.id.nav_dashboard ->                 //add the inbox fragment
//                supportFragmentManager.beginTransaction()
//                    .replace(R.id.container, Dashboard())
//                    .commit()
//            R.id.nav_forum ->                 //add the inbox fragment
//                supportFragmentManager.beginTransaction()
//                    .replace(R.id.container,SearchFragment())
//                    .commit()
//            R.id.nav_messages ->                 //add the inbox fragment
//                supportFragmentManager.beginTransaction()
//                    .replace(R.id.container, MessagesFragment())
//                    .commit()
//            R.id.nav_about -> Toast.makeText(this, "This is about Item", Toast.LENGTH_SHORT).show()
//            R.id.nav_signOut -> Toast.makeText(this, "This is sign out Item", Toast.LENGTH_SHORT).show()
//        }
//
//        mNavDrawer.closeDrawer(GravityCompat.START)
//
//        return true
//
//    }


