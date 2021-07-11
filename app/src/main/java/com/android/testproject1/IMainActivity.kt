package com.android.testproject1

import com.android.testproject1.model.Post
import com.android.testproject1.model.Users
import com.android.testproject1.room.enteties.PostRoomEntity
import com.android.testproject1.room.enteties.UsersRoomEntity

interface IMainActivity {
//    fun onLoginButtonCLick()
    fun onRegisterClick()
    fun onRecyclerViewItemClick(postItem: Post)
    fun onJoinItemClick(userItem:Users)
    fun onGroupItemClick(userItem: UsersRoomEntity)
    fun onGroupItemClick(userItem: Users)
//    fun onSignUpButtonClick()

}