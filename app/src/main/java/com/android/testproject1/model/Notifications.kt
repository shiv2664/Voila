package com.android.testproject1.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Notifications(var id: String? = "",
                         var username: String? = "",
                         var image: String? ="",
                         var message: String? = "",
                         var timestamp: String? = "",
                         var type: String? = "",
                         var action_id: String? =""):Parcelable