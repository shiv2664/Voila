package com.android.testproject1

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.Intent.*
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.android.testproject1.adapter.MenuAdapter
import com.android.testproject1.fragments.HomeFragment
import com.android.testproject1.fragments.SearchFragment
import com.android.testproject1.model.Post
import com.android.testproject1.model.Users
import com.android.testproject1.room.enteties.UsersRoomEntity
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.activity_main2.*
import kotlinx.android.synthetic.main.drawer_menu.*
import nl.psdcompany.duonavigationdrawer.views.DuoMenuView
import nl.psdcompany.duonavigationdrawer.widgets.DuoDrawerToggle
import java.util.*
import kotlin.collections.ArrayList


class MainActivity2 : AppCompatActivity(),IMainActivity,DuoMenuView.OnMenuClickListener {

    private lateinit var mNavDrawer: DrawerLayout
    private var mTitles: ArrayList<String> = ArrayList()
    private lateinit var mMenuAdapter: MenuAdapter

    val myTag = "MyTag"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
//        mTitles.addAll(listOf(listOf(resources.getStringArray(R.array.titles_menu)).toString()))
//        mTitles.add("Dashboard")
        Log.d(myTag, "" + mTitles)
//        mTitles.add("DASHBOARD")

        loadFragment(Dashboard())

        homeItem.setOnClickListener {
            loadFragment(Dashboard())
            drawer.closeDrawer()
        }
        forumItem.setOnClickListener {

            val fragment =Dashboard()
            val fm = supportFragmentManager
            val bundle = Bundle()
            bundle.putString("bottom_nav","1")

            fragment.arguments=bundle
                fm.beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit()



//            loadFragment(Dashboard())
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

    override fun onResume() {
        super.onResume()
    }

    override fun onBackPressed() {
//        if (mNavDrawer.isDrawerOpen(GravityCompat.START)) {
//            mNavDrawer.closeDrawer(GravityCompat.START)
//        } else {
//            super.onBackPressed()
//        }

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

    override fun onRecyclerViewItemClick(postItem: Post) {
        TODO("Not yet implemented")
    }

    override fun onJoinItemClick(userItem: Users) {
        TODO("Not yet implemented")
    }

    override fun onGroupItemClick(userItem: UsersRoomEntity) {
        TODO("Not yet implemented")
    }

    override fun onGroupItemClick(userItem: Users) {
        TODO("Not yet implemented")
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
        Log.d("MyTag", "Positin Clicked " + position)

        // Set the right options selected

        // Set the right options selected
        mMenuAdapter.setViewSelected(position, true)

        // Navigate to the right fragment
        when (position) {
            0 -> loadFragment(HomeFragment())
            1 -> loadFragment(SearchFragment())
        }

        // Close the drawer

        // Close the drawer
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


