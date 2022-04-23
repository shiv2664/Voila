package com.android.testproject1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.android.testproject1.fragments.ChatFragment
import com.android.testproject1.fragments.ChatGroupFragment
import com.android.testproject1.fragments.GroupFragment
import com.android.testproject1.fragments.ProfileOpened
import com.android.testproject1.interfaces.IMainActivity
import com.android.testproject1.room.enteties.NotificationsRoomEntity
import com.android.testproject1.model.Users
import com.android.testproject1.room.enteties.OfferRoomEntity
import com.android.testproject1.room.enteties.OffersSavedRoomEntity
import com.android.testproject1.room.enteties.UsersChatListEntity
import com.google.android.material.button.MaterialButton
import kotlinx.android.synthetic.main.activity_chat.*

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

            } else if (chatsOpened=="fromProfile"){

//                val userItem=intent.getParcelableExtra<UsersChatListEntity>("userItem")
//
                val userId =intent.getStringExtra("userId")
                val name=intent.getStringExtra("name")

//                Log.d("MyTag","user ID is this : "+userId)

                name_holder.text=name

                name_holder.setOnClickListener {
                    val fragment=ProfileOpened()
                    val bundle = Bundle()
//                    bundle.putParcelable("userItem",userItem)
//                    bundle.putString("chatsOpened","fromGroups")
                    if (userId != null) {
                        bundle.putString("iD",userId)
                    }
                    fragment.arguments=bundle

                    supportFragmentManager
                        .beginTransaction()
                        .add(R.id.container, fragment)
                        .commit()

                }

                val fragment=ChatFragment()
                val bundle = Bundle()
                bundle.putString("chatsOpened","fromProfile")
//                bundle.putParcelable("userItem",userItem)
                bundle.putString("userId",userId)
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

    override fun onRecyclerViewGroupsItemClick(offerItem: OfferRoomEntity) {
        TODO("Not yet implemented")
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
}