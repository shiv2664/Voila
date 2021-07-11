package com.android.testproject1.room.enteties

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity
@Parcelize
data class UsersRoomEntity(
    @PrimaryKey(autoGenerate = false)
    val id:String=""
    ,val name:String="",val username:String="",val bio:String="",
    val email:String="",val location:String="",val profileimage:String=""
    ,val imgUrl:String="") : Parcelable