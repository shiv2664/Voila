package com.android.testproject1.room.enteties

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.firestore.ServerTimestamp
import kotlinx.android.parcel.Parcelize
import java.util.*


@Entity
@Parcelize
data class OrdersRoomEntity(
    @PrimaryKey(autoGenerate = false)
    var idOrder:String="",
    var id: String? = "",
    var username: String? = "",
    var thumbnailImage:String?="",
    var image: String? ="",
    var foodImage: String? ="",
    var userNameOffer:String="",
    var message: String? = "",
    var type: String? = "",
    var orderName:String?="",
    var status:String?="",
    var price:String?="",
    var action_id: String? ="",
    var Quantity:String?="",
    var orderFrom:String?="",
    var userId:String?="",
    var orderTo:String?="",
    @ServerTimestamp
    var timestamp: Date? = null
):Parcelable{

    override fun equals(other: Any?): Boolean {

        if (javaClass != other?.javaClass) {
            return false
        }

        other as OrdersRoomEntity

        if (idOrder != other.idOrder) {
            return false
        }
        if (username != other.username) {
            return false
        }
        if (image != other.image) {
            return false
        }
        if (message != other.message) {
            return false
        }
        if (type != other.type) {
            return false
        }
        if (orderName != other.orderName) {
            return false
        }
        if (username != other.username) {
            return false
        }
        if (status != other.status) {
            return false
        }
        if (price != other.price) {
            return false
        }
        if (action_id != other.action_id) {
            return false
        }

        if (Quantity != other.Quantity) {
            return false
        }

        if (orderFrom != other.orderFrom) {
            return false
        }

        if (orderTo != other.orderTo) {
            return false
        }

        if (timestamp != other.timestamp) {
            return false
        }

        if (foodImage != other.foodImage) {
            return false
        }

        if (userNameOffer!= other.userNameOffer){
            return false
        }

        return true
    }


}