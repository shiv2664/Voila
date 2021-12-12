package com.android.testproject1.room.enteties

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.firestore.ServerTimestamp
import kotlinx.android.parcel.Parcelize
import java.util.*

@Entity
@Parcelize
data class OfferRoomEntity(
    @PrimaryKey(autoGenerate = false)
    var postId:String=""
    ,var userId: String=""
    ,var name: String=""
    ,var likes: String=""
    ,var favourites: String=""
    ,var description: String=""
    ,var username: String=""
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
    ,var thumbnailImage: String =""
    ,var image_count: Int = 0
    ,var image_url_0: String=""
    ,var image_url_1: String=""
    ,var image_url_2: String=""
    ,var image_url_3: String=""
    ,var image_url_4: String=""
    ,var image_url_5: String=""
    ,var image_url_6: String=""
    ,@ServerTimestamp
    var timestamp: Date? = null) : Parcelable {

    override fun equals(other: Any?): Boolean {

        if (javaClass != other?.javaClass) {
            return false
        }

        other as OfferRoomEntity

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
        if (thumbnailImage != other.thumbnailImage) {
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

    override fun hashCode(): Int {
        var result = postId.hashCode()
        result = 31 * result + userId.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + likes.hashCode()
        result = 31 * result + favourites.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + username.hashCode()
        result = 31 * result + title.hashCode()
        result = 31 * result + saved.hashCode()
        result = 31 * result + originalPrice.hashCode()
        result = 31 * result + discountPrice.hashCode()
        result = 31 * result + city.hashCode()
        result = 31 * result + ratings.hashCode()
        result = 31 * result + usersRated.hashCode()
        result = 31 * result + addressMap.hashCode()
        result = 31 * result + offerCategory.hashCode()
        result = 31 * result + minPeople.hashCode()
        result = 31 * result + maxPeople.hashCode()
        result = 31 * result + thumbnailImage.hashCode()
        result = 31 * result + image_count
        result = 31 * result + image_url_0.hashCode()
        result = 31 * result + image_url_1.hashCode()
        result = 31 * result + image_url_2.hashCode()
        result = 31 * result + image_url_3.hashCode()
        result = 31 * result + image_url_4.hashCode()
        result = 31 * result + image_url_5.hashCode()
        result = 31 * result + image_url_6.hashCode()
        result = 31 * result + (timestamp?.hashCode() ?: 0)
        return result
    }
}