package com.android.testproject1.room.enteties

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity
@Parcelize
data class PostRoomEntity(
    @PrimaryKey(autoGenerate = false)
    val postId:String=""
    ,val userId: String=""
    ,val name: String=""
    ,val timestamp: String=""
    ,val likes: String=""
    ,var favourites: String=""
    ,val description: String=""
    ,val color: String=""
    ,val username: String=""
    ,val userimage: String =""
    ,val image_count: Int = 0
    ,val image_url_0: String=""
    ,val image_url_1: String=""
    ,val image_url_2: String=""
    ,val image_url_3: String=""
    ,val image_url_4: String=""
    ,val image_url_5: String=""
    ,val image_url_6: String=""
):Parcelable


