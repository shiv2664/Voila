package com.android.testproject1.room.enteties

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity
@Parcelize
data class PostRoomEntity(
    @PrimaryKey(autoGenerate = false)
    var postId:String=""
    ,var userId: String=""
    ,var name: String=""
    ,var timestamp: String=""
    ,var likes: String=""
    ,var favourites: String=""
    ,var description: String=""
    ,var color: String=""
    ,var username: String=""
    ,var userimage: String =""
    ,var image_count: Int = 0
    ,var image_url_0: String=""
    ,var image_url_1: String=""
    ,var image_url_2: String=""
    ,var image_url_3: String=""
    ,var image_url_4: String=""
    ,var image_url_5: String=""
    ,var image_url_6: String=""
):Parcelable


