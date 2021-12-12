package com.android.testproject1.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Users(var name:String="",var username:String="",var bio:String="",var userId:String=""
                 ,var email:String="",var location:String="",var profileimage:String=""
                 ,var thumbnailImage:String="",var phoneNumber:String="",var userType:String="") : Parcelable