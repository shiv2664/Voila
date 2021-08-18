package com.android.testproject1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.android.testproject1.fragments.ChatFragment
import com.android.testproject1.fragments.ChatGroupFragment
import com.android.testproject1.interfaces.IMainActivity
import com.android.testproject1.model.Notifications
import com.android.testproject1.model.Offer
import com.android.testproject1.model.Users
import com.android.testproject1.room.enteties.UsersRoomEntity

class ChatActivity : AppCompatActivity() ,IMainActivity {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val checkFragment=intent.getStringExtra("openChat")
        val chatsOpened=intent.getStringExtra("chatsOpened")

        if (checkFragment=="openUserChat"){

            if (chatsOpened=="fromGroups"){
                val userItem=intent.getParcelableExtra<Users>("userItem")

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

                val userItem=intent.getParcelableExtra<UsersRoomEntity>("userItem")

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

            val groupItem=intent.getParcelableExtra<Users>("groupItem")

            val fragment=ChatGroupFragment()
            val bundle = Bundle()
            bundle.putParcelable("userItem",groupItem)
            fragment.arguments=bundle

            supportFragmentManager
                .beginTransaction()
                .addToBackStack("Groups Fragment")
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
}