package com.android.testproject1.room.enteties

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.android.testproject1.model.Offer
import com.google.firebase.firestore.ServerTimestamp
import kotlinx.android.parcel.Parcelize
import java.util.*

@Entity
@Parcelize
data class OffersSavedRoomEntity(
    @PrimaryKey(autoGenerate = false)
    var postId:String=""
    ,var userId: String=""
    ,var name: String=""
    ,var  likes: String=""
    ,var favourites: String=""
    ,var description: String=""
    ,var username: String=""
    ,var userimage: String =""
    ,var title:String=""
    ,var saved:String=""
    ,var originalPrice:String=""
    ,var discountPrice:String=""
    ,var city:String=""
    ,var ratings:String=""
    ,var usersRated:String=""
    ,var addressMap:String=""
    ,var offerCategory:String=""
    ,var minPeople:String=""
    ,var maxPeople:String=""
    ,var image_count: Int = 0
    ,var image_url_0: String=""
    ,var image_url_1: String=""
    ,var image_url_2: String=""
    ,var image_url_3: String=""
    ,var image_url_4: String=""
    ,var image_url_5: String=""
    ,var image_url_6: String=""
    ,@ServerTimestamp
    var timestamp: Date? = null) : Parcelable{

    override fun equals(other: Any?): Boolean {

        if (javaClass != other?.javaClass) {
            return false
        }

        other as OffersSavedRoomEntity

        if (userId != other.userId) {
            return false
        }
        if (name != other.name) {
            return false
        }
        if (timestamp != other.timestamp) {
            return false
        }
        if (likes != other.likes) {
            return false
        }
        if (favourites != other.favourites) {
            return false
        }
        if (description != other.description) {
            return false
        }
        if (username != other.username) {
            return false
        }
        if (name != other.name) {
            return false
        }
        if (postId != other.postId) {
            return false
        }
        if (userimage != other.userimage) {
            return false
        }
        if (title != other.title) {
            return false
        }

        if (saved != other.saved) {
            return false
        }

        if (originalPrice != other.originalPrice) {
            return false
        }
        if (discountPrice != other.discountPrice) {
            return false
        }
        if (city != other.city) {
            return false
        }
        if (ratings != other.ratings) {
            return false
        }
        if (usersRated != other.usersRated) {
            return false
        }
        if (addressMap != other.addressMap) {
            return false
        }

        if (offerCategory!= other.offerCategory) {
            return false
        }
        if (minPeople != other.minPeople) {
            return false
        }
        if (maxPeople != other.maxPeople) {
            return false
        }
        if (image_count != other.image_count) {
            return false
        }

        if (image_url_0 != other.image_url_0) {
            return false
        }
        if (image_url_1 != other.image_url_2) {
            return false
        }

        if (image_url_3 != other.image_url_3) {
            return false
        }

        if (image_url_4 != other.image_url_4) {
            return false
        }

        if (image_url_5 != other.image_url_5) {
            return false
        }

        if (image_url_6 != other.image_url_6) {
            return false
        }

        return true

    }
    }