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
import com.android.testproject1.model.Offer
import com.android.testproject1.model.Post
import com.android.testproject1.model.Users
import com.android.testproject1.room.enteties.UsersRoomEntity
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.nguyenhoanglam.imagepicker.model.Config
import com.nguyenhoanglam.imagepicker.model.Image
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_main2.*
import kotlinx.android.synthetic.main.activity_main2.toolbar
import kotlinx.android.synthetic.main.activity_main2_content.*
import kotlinx.android.synthetic.main.bottom_sheet_dialog.*
import kotlinx.android.synthetic.main.drawer_menu.*
import nl.psdcompany.duonavigationdrawer.views.DuoMenuView
import nl.psdcompany.duonavigationdrawer.widgets.DuoDrawerToggle
import java.util.*
import kotlin.collections.ArrayList


class MainActivity2 : AppCompatActivity(),IMainActivity,DuoMenuView.OnMenuClickListener {

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        setSupportActionBar(toolbar)
        toolbar.title = "Home"

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

        loadFragment(Dashboard())
        checkTabDashboard=true

        Home.setOnClickListener {
            loadFragment(Dashboard())
            toolbar.title = "Home"
            addPost?.isVisible = true
            drawer.closeDrawer()
        }
        Discover.setOnClickListener {

            val fragment = Dashboard()
            val fm = supportFragmentManager
            val bundle = Bundle()
            bundle.putString("bottom_nav","1")
            toolbar.title = "Discover"
            addPost?.isVisible = true

            fragment.arguments=bundle
                fm.beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit()
            drawer.closeDrawer()
        }
        Search.setOnClickListener {
            val fragment = SearchPeople()
            val fm = supportFragmentManager
//            val bundle = Bundle()
//            bundle.putString("bottom_nav","1")
//            toolbar.title = "Discover"
//            addPost?.isVisible = true

//            fragment.arguments=bundle
            fm.beginTransaction()
                .replace(R.id.container, fragment)
                .commit()
            drawer.closeDrawer()
        }
        Messages.setOnClickListener {
            loadFragment(MessagesFragment())
            toolbar.title = "Chats"
            addPost?.isVisible = false
            drawer.closeDrawer()
        }
        Profile.setOnClickListener {
            loadFragment(ProfileFragment())
            toolbar.title = "Profile"
            addPost?.isVisible = false
            drawer.closeDrawer()
        }


        val duoDrawerToggle = DuoDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer.setDrawerListener(duoDrawerToggle)
        duoDrawerToggle.syncState()
        mMenuAdapter = MenuAdapter(mTitles);
        duoMenuView.setOnMenuClickListener(this);
        duoMenuView.adapter = mMenuAdapter;

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
        }
        return super.onOptionsItemSelected(item)
    }



    override fun onBackPressed() {
        if (drawer.isDrawerOpen) {
            drawer.closeDrawer()
        } else {
            super.onBackPressed()
        }

    }

    private fun loadFragment(fragment: Fragment?): Boolean {
        if (fragment != null) {
            val fm = supportFragmentManager
            fm.beginTransaction()
                .replace(R.id.container, fragment)
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

    override fun onJoinItemClick(userItem: Users) {
        val userId: String = userItem.id
        val currentUserId: String = firebaseAuth.currentUser?.uid!!

        val postMap: MutableMap<String, Any?> = HashMap()
        postMap[currentUserId]=true

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

                supportFragmentManager
                    .beginTransaction()
                    .addToBackStack("Groups Fragment")
                    .add(R.id.container, fragment)
                    .commit()
            }
    }

    override fun onGroupItemClick(userItem: UsersRoomEntity) {
        GROUP_USER_ID =userItem.id

        val fragment=ChatFragment()

        val bundle = Bundle()
        bundle.putParcelable("userItem",userItem)
        fragment.arguments=bundle

        supportFragmentManager
            .beginTransaction()
            .addToBackStack("Groups Fragment")
            .add(R.id.container, fragment)
            .commit()
    }

    override fun onGroupItemClick(userItem: Users) {
        GROUP_USER_ID =userItem.id

        val fragment=ChatFragment()

        val bundle = Bundle()
        bundle.putParcelable("userItem",userItem)
        fragment.arguments=bundle

        supportFragmentManager
            .beginTransaction()
            .addToBackStack("Groups Fragment")
            .add(R.id.container, fragment)
            .commit()
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

    override fun onFooterClicked() {}

    override fun onHeaderClicked() {}

    override fun onOptionClicked(position: Int, objectClicked: Any?) {
        title = mTitles[position]
        Log.d("MyTag", "Positin Clicked $position")

        // Set the right options selected

        // Set the right options selected
        mMenuAdapter.setViewSelected(position, true)

        // Navigate to the right fragment
        when (position) {
            0 -> loadFragment(HomeFragment())
            1 -> loadFragment(SearchFragment())
        }
        drawer.closeDrawer()
    }

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


