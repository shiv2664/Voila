package com.android.testproject1.interfaces

import com.android.testproject1.room.enteties.NotificationsRoomEntity
import com.android.testproject1.model.Offer
import com.android.testproject1.model.Users
import com.android.testproject1.room.enteties.UsersChatListEntity
import com.google.android.material.button.MaterialButton

interface IMainActivity {
//    fun onLoginButtonCLick()
    fun onRegisterClick()
    fun onRecyclerViewItemClick(offerItem: Offer)
    fun onPlaceOrderClick(offerItem: Offer, Total: String, Quantity: String="1")
    fun onAcceptClick(
        notificationsRoomEntityItem: NotificationsRoomEntity,
        reject: MaterialButton,
        accept: MaterialButton,
        cancel: MaterialButton,
        ready: MaterialButton
    )
    fun onOrderReadyClick(notificationsRoomEntityItem: NotificationsRoomEntity,
                          cancel: MaterialButton,
                          ready: MaterialButton,
                          waiting:MaterialButton)
    fun onProfileOpenedDiscover(offerItem: Offer)
    fun onJoinItemClick(userItem:UsersChatListEntity)
    fun onGroupItemClick(userItem: UsersChatListEntity)
    fun onGroupItemClick(userItem: Users)
    fun onLocationClick()
    fun onSendFriendReqClick(userItem: Users)
    fun onNotificationItemClick(notificationsRoomEntityItem: NotificationsRoomEntity)
    fun onSearchPeopleItemClick(userItem: Users)
    fun onBookMarkItemClick(offerItem: Offer)
    fun onGroupOpenFromMessages(userItem: UsersChatListEntity)
//    fun onJoinRecyclerViewClick()
//    fun onSignUpButtonClick()

}