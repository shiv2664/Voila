package com.android.testproject1.model

import android.os.Parcelable
import com.google.firebase.firestore.ServerTimestamp
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Notifications(var id: String? = "",
                         var username: String? = "",
                         var image: String? ="",
                         var message: String? = "",
                         var type: String? = "",
                         var orderName:String?="",
                         var status:String?="",
                         var price:String?="",
                         var action_id: String? ="",
                         var Quantity:String?="",
                         var orderFrom:String?="",
                         var orderTo:String?="",
                         var idOrder:String?="",
                         @ServerTimestamp
                         val timestamp: Date? = null):Parcelable