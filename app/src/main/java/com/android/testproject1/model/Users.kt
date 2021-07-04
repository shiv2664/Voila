package com.android.testproject1.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Users(val name:String="",val username:String="",val bio:String="",val id:String=""
                 ,val email:String="",val location:String="",val profileimage:String=""
                 ,val imgUrl:String="") : Parcelable