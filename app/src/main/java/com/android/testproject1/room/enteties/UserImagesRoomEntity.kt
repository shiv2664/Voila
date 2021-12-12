package com.android.testproject1.room.enteties

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity
@Parcelize
data class UserImagesRoomEntity(
    @PrimaryKey(autoGenerate = false)
    var userId: String="",
    var username:String=""
    ,var thumbnailImage: String? =""):Parcelable