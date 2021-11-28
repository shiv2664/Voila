package com.android.testproject1.room.enteties

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.util.*

@Entity
@Parcelize
data class UsersChatListEntity(
     @PrimaryKey(autoGenerate = false)
     var id:String="",var name:String="",var username:String="",var bio:String=""
    ,var email:String="",var location:String="",var profileimage:String=""
    ,var imgUrl:String="",var postId:String="",var groupId:String=""
    ,var createdBy:String="",var timeStamp: Date?=null,var viewType:String=""
) : Parcelable