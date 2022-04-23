package com.android.testproject1

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.android.testproject1.fragments.*
import com.android.testproject1.interfaces.IMainActivity
import com.android.testproject1.room.enteties.NotificationsRoomEntity
import com.android.testproject1.model.Users
import com.android.testproject1.room.enteties.OfferRoomEntity
import com.android.testproject1.room.enteties.OffersSavedRoomEntity
import com.android.testproject1.room.enteties.UsersChatListEntity
import com.android.testproject1.viewmodels.MainActivity2ViewModel
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_home.*
import java.util.*


class MainActivity : AppCompatActivity() , IMainActivity {

    companion object {
        private const val READ_PERMISSION_CODE = 100
        private const val STORAGE_PERMISSION_CODE = 101
        private var GROUP_USER_ID:String?=null
    }

    private lateinit var mViewModel: MainActivity2ViewModel
    private lateinit var firebaseFirestore: FirebaseFirestore
    private lateinit var firebaseAuth:FirebaseAuth
    var postId:String=""
    var myTag:String="MyTag"
//    private val bottomAppBar:BottomAppBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mViewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory
            .getInstance(this.application))
            .get(MainActivity2ViewModel::class.java)


        val bottomNavigationView = bottomNav
        bottomNavigationView.background = null
        loadFragment(HomeFragment())

        firebaseAuth= FirebaseAuth.getInstance()
        firebaseFirestore= FirebaseFirestore.getInstance()


        checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,STORAGE_PERMISSION_CODE)
        checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE, READ_PERMISSION_CODE)

//        bottomNavigationView.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
//            when (menuItem.itemId) {
////                R.id.home -> return@OnNavigationItemSelectedListener loadFragment(HomeFragment())
//                R.id.search -> return@OnNavigationItemSelectedListener loadFragment(SearchFragment())
//                R.id.notifications -> return@OnNavigationItemSelectedListener loadFragment(MessagesFragment())
//                R.id.profile -> return@OnNavigationItemSelectedListener loadFragment(ProfileFragment())
////                R.id.NewPost -> return@OnNavigationItemSelectedListener loadFragment(CreatePost())
//
//            }
//            false
//        })

    }

//    override fun onBackPressed() {
//        if (bottomNav.selectedItemId==R.id.home){
//            super.onBackPressed()
//        }else{
//            bottomNav.selectedItemId=R.id.home
//        }
//
//    }

    private fun checkPermission(permission: String, requestCode: Int) {
        if (ContextCompat.checkSelfPermission(this@MainActivity, permission) == PackageManager.PERMISSION_DENIED) {
            // Requesting the permission
            ActivityCompat.requestPermissions(this@MainActivity, arrayOf(permission), requestCode)
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
                Toast.makeText(this@MainActivity, "Read Permission Denied", Toast.LENGTH_SHORT).show()
            }
        } else if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                Toast.makeText(this@MainActivity, "Storage Permission Granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this@MainActivity, "Storage Permission Denied", Toast.LENGTH_SHORT).show()
            }
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

    override fun onRecyclerViewGroupsItemClick(offerItem: OfferRoomEntity) {
        TODO("Not yet implemented")
//        val fragment = DetailsFragment()
//
//        postId=postItem.postId
//
//        val bundle = Bundle()
//        bundle.putParcelable("PostItem",postItem)
//        fragment.arguments=bundle
//
//
//        supportFragmentManager
//            .beginTransaction()
//            .addToBackStack("Detail Fragment")
//            .replace(R.id.container, fragment, "Details Fragment")
//            .commit()


    }

    override fun onPlaceOrderClick(offerItem: OfferRoomEntity, Total: String, Quantity: String) {
        TODO("Not yet implemented")
    }

    override fun onUserNamePicClick(userId: String?, orderTo: String?) {
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
        linearlayout4: LinearLayout,
        CancelLayout: LinearLayout,
        RejectedLayout: LinearLayout
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
        linearLayout4: LinearLayout,
        CancelLayout: LinearLayout,
        RejectedLayout: LinearLayout
    ) {
        TODO("Not yet implemented")
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
        TODO("Not yet implemented")
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
        TODO("Not yet implemented")
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
        TODO("Not yet implemented")
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

//        val userId: String = userItem.id
//        val currentUserId: String = firebaseAuth.currentUser.uid
//
//        val postMap: MutableMap<String, Any?> = HashMap()
//        postMap[currentUserId]=true
//
//        val fragment=GroupFragment()
//
//        val bundle = Bundle()
//        bundle.putParcelable("userItem",userItem)
//        fragment.arguments=bundle
//
//
//
//        firebaseFirestore.collection("Posts")
//            .document(postId)
//            .collection("Groups")
//            .document(userId)
//            .update("Members", FieldValue.arrayUnion(currentUserId))
//            .addOnSuccessListener {
//
//                supportFragmentManager
//                    .beginTransaction()
//                    .addToBackStack("Groups Fragment")
//                    .replace(R.id.container, fragment)
//                    .commit()
//            }

    }

    override fun onGroupItemClick(userItem: UsersChatListEntity) {
    }

    override fun onGroupItemClick(userItem: Users) {

//        GROUP_USER_ID=userItem.id
//
//        val fragment=ChatFragment()
//
//        val bundle = Bundle()
//        bundle.putParcelable("userItem",userItem)
//        fragment.arguments=bundle
//
//        supportFragmentManager
//            .beginTransaction()
//            .addToBackStack("Groups Fragment")
//            .replace(R.id.container, fragment)
//            .commit()
    }

    override fun onLocationClick() {
        TODO("Not yet implemented")
    }

    override fun onSendFriendReqClick(userItem: Users) {
        TODO("Not yet implemented")
    }

    override fun onNotificationItemClick(notificationsRoomEntityItem: NotificationsRoomEntity) {
        TODO("Not yet implemented")
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

    fun hideProgressBar(){
        if (progressbar.visibility==View.VISIBLE){
            progressbar.visibility=View.GONE
        }
    }

}


