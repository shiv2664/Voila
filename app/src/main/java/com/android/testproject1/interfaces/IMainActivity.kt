package com.android.testproject1.interfaces

import android.widget.ImageView
import android.widget.LinearLayout
import com.android.testproject1.room.enteties.NotificationsRoomEntity
import com.android.testproject1.model.Users
import com.android.testproject1.room.enteties.OfferRoomEntity
import com.android.testproject1.room.enteties.OffersSavedRoomEntity
import com.android.testproject1.room.enteties.UsersChatListEntity
import com.google.android.material.button.MaterialButton

interface IMainActivity {
//    fun onLoginButtonCLick()
    fun onRegisterClick()
    fun onRecyclerViewGroupsItemClick(offerItem: OfferRoomEntity)
    fun onPlaceOrderClick(offerItem: OfferRoomEntity, Total: String, Quantity: String="1")
    fun onUserNamePicClick(userId: String?, orderTo: String?)
    fun onAcceptClick(
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
    )

    fun onOrderReadyClick(
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
    )
    fun onWaitingClicked(
        notificationsRoomEntityItem: NotificationsRoomEntity,
        linearLayout: LinearLayout,
        linearLayout2: LinearLayout,
        linearLayout3: LinearLayout,
        linearLayout4: LinearLayout,
        CancelLayout: LinearLayout,
        RejectedLayout: LinearLayout
    )

    fun onRejectClick(
        notificationsRoomEntityItem: NotificationsRoomEntity,
        linearLayout: LinearLayout,
        linearlayout2: LinearLayout,
        linearLayout3: LinearLayout,
        linearlayout4: LinearLayout,
        CancelLayout: LinearLayout,
        RejectedLayout: LinearLayout
    )

    fun onCancelClick(
        notificationsRoomEntityItem: NotificationsRoomEntity,
        linearLayout: LinearLayout,
        linearlayout2: LinearLayout,
        linearLayout3: LinearLayout,
        linearlayout4: LinearLayout,
        CancelLayout: LinearLayout,
        RejectedLayout: LinearLayout
    )

    fun onProfilePostItemCLick(offerItem: OfferRoomEntity)

    fun onCurrentUserOfferClick(offerItem: OfferRoomEntity)
    fun onProfileOpenedDiscover(offerItem: OfferRoomEntity)
    fun onJoinItemClick(userItem:UsersChatListEntity)
    fun onGroupItemClick(userItem: UsersChatListEntity)
    fun onGroupItemClick(userItem: Users)
    fun onLocationClick()
    fun onSendFriendReqClick(userItem: Users)
    fun onNotificationItemClick(notificationsRoomEntityItem: NotificationsRoomEntity)
    fun onSearchPeopleItemClick(userItem: Users)
    fun onBookMarkItemClick(offerItem: OfferRoomEntity, bookmarkImageView: ImageView)
    fun onBookMarkClickedSavedOffers(offerItem: OffersSavedRoomEntity, bookmarkImageView: ImageView)
    fun onGroupOpenFromMessages(userItem: UsersChatListEntity)
//    fun onJoinRecyclerViewClick()
//    fun onSignUpButtonClick()

}