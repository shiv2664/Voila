package com.android.testproject1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.android.testproject1.fragments.ChatFragment
import com.android.testproject1.fragments.ChatGroupFragment
import com.android.testproject1.fragments.GroupFragment
import com.android.testproject1.fragments.ProfileOpened
import com.android.testproject1.interfaces.IMainActivity
import com.android.testproject1.model.Notifications
import com.android.testproject1.model.Offer
import com.android.testproject1.model.Users
import com.android.testproject1.room.enteties.UsersChatListEntity
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.drawer_menu.*

class ChatActivity : AppCompatActivity() ,IMainActivity {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val checkFragment=intent.getStringExtra("openChat")
        val chatsOpened=intent.getStringExtra("chatsOpened")

        if (checkFragment=="openUserChat"){

            if (chatsOpened=="fromGroups"){
                val userItem=intent.getParcelableExtra<UsersChatListEntity>("userItem")
                name_holder.text=userItem?.name

                name_holder.setOnClickListener {

                    val fragment=ProfileOpened()
                    val bundle = Bundle()
//                    bundle.putParcelable("userItem",userItem)
//                    bundle.putString("chatsOpened","fromGroups")
                    if (userItem != null) {
                        bundle.putString("iD",userItem.id)
                    }
                    fragment.arguments=bundle

                    supportFragmentManager
                        .beginTransaction()
                        .add(R.id.container, fragment)
                        .commit()

                }

                val fragment=ChatFragment()
                val bundle = Bundle()
                bundle.putParcelable("userItem",userItem)
                bundle.putString("chatsOpened","fromGroups")
                fragment.arguments=bundle

                supportFragmentManager
                    .beginTransaction()
                    .add(R.id.container, fragment)
                    .commit()
            }else if (chatsOpened=="fromMessages"){

                val userItem=intent.getParcelableExtra<UsersChatListEntity>("userItem")

                name_holder.text=userItem?.name

                name_holder.setOnClickListener {

                    val fragment=ProfileOpened()
                    val bundle = Bundle()
//                    bundle.putParcelable("userItem",userItem)
//                    bundle.putString("chatsOpened","fromGroups")
                    if (userItem != null) {
                        bundle.putString("iD",userItem.id)
                    }
                    fragment.arguments=bundle

                    supportFragmentManager
                        .beginTransaction()
                        .add(R.id.container, fragment)
                        .commit()

                }

                val fragment=ChatFragment()
                val bundle = Bundle()
                bundle.putString("chatsOpened","fromMessages")
                bundle.putParcelable("userItem",userItem)
                fragment.arguments=bundle

                supportFragmentManager
                    .beginTransaction()
                    .add(R.id.container, fragment)
                    .commit()

            }

        }else if (checkFragment=="openGroupChat"){

            val groupItem=intent.getParcelableExtra<UsersChatListEntity>("groupItem")

            name_holder.text=groupItem?.name

            name_holder.setOnClickListener {

                val fragment= GroupFragment()
                val bundle = Bundle()
                bundle.putParcelable("userItem",groupItem)
                fragment.arguments=bundle

                Log.d("MyTag","Group Id is "+groupItem?.id)


                supportFragmentManager
                    .beginTransaction()
                    .add(R.id.container, fragment)
                    .commit()
            }

            val fragment=ChatGroupFragment()
            val bundle = Bundle()
            bundle.putParcelable("userItem",groupItem)
            fragment.arguments=bundle

                Log.d("MyTag","Group Id is "+groupItem?.id)


            supportFragmentManager
                .beginTransaction()
                .add(R.id.container, fragment)
                .commit()
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
        TODO("Not yet implemented")
    }

    override fun onPlaceOrderClick(offerItem: Offer) {
        TODO("Not yet implemented")
    }

    override fun onJoinItemClick(userItem: UsersChatListEntity) {
        TODO("Not yet implemented")
    }

    override fun onGroupItemClick(userItem: UsersChatListEntity) {
        val fragment=ChatFragment()
        val bundle = Bundle()
        bundle.putParcelable("userItem",userItem)
        bundle.putString("chatsOpened","fromGroups")
        fragment.arguments=bundle

        supportFragmentManager
            .beginTransaction()
            .add(R.id.container, fragment)
            .commit()
    }

    override fun onGroupItemClick(userItem: Users) {

        val fragment=ChatFragment()
        val bundle = Bundle()
        bundle.putParcelable("userItem",userItem)
        bundle.putString("chatsOpened","fromGroups")
        fragment.arguments=bundle

        supportFragmentManager
            .beginTransaction()
            .add(R.id.container, fragment)
            .commit()
//        MainActivity2.GROUP_USER_ID =userItem.id
//        val intent = Intent(this@MainActivity2,ChatActivity::class.java)
//        intent.putExtra("openChat","openUserChat")
//        intent.putExtra("chatsOpened","fromMessages")
//        intent.putExtra("userItem",userItem)
//        startActivity(intent)

    }

    override fun onLocationClick() {
        TODO("Not yet implemented")
    }

    override fun onSendFriendReqClick(userItem: Users) {
        TODO("Not yet implemented")
    }

    override fun onNotificationItemClick(notificationsItem: Notifications) {
        TODO("Not yet implemented")
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