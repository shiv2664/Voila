package com.android.testproject1.interfaces

import com.android.testproject1.model.Notifications
import com.android.testproject1.model.Offer
import com.android.testproject1.model.Users
import com.android.testproject1.room.enteties.UsersChatListEntity

interface IMainActivity {
//    fun onLoginButtonCLick()
    fun onRegisterClick()
    fun onRecyclerViewItemClick(offerItem: Offer)
    fun onJoinItemClick(userItem:UsersChatListEntity)
    fun onGroupItemClick(userItem: UsersChatListEntity)
    fun onGroupItemClick(userItem: Users)
    fun onLocationClick()
    fun onSendFriendReqClick(userItem: Users)
    fun onNotificationItemClick(notificationsItem: Notifications)
    fun onSearchPeopleItemClick(userItem: Users)
    fun onBookMarkItemClick(offerItem: Offer)
    fun onGroupOpenFromMessages(userItem: UsersChatListEntity)
//    fun onJoinRecyclerViewClick()
//    fun onSignUpButtonClick()

}