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

        return true
    }

    override fun hashCode(): Int {
        var result = idOrder.hashCode()
        result = 31 * result + (id?.hashCode() ?: 0)
        result = 31 * result + (username?.hashCode() ?: 0)
        result = 31 * result + (image?.hashCode() ?: 0)
        result = 31 * result + (message?.hashCode() ?: 0)
        result = 31 * result + (type?.hashCode() ?: 0)
        result = 31 * result + (orderName?.hashCode() ?: 0)
        result = 31 * result + (status?.hashCode() ?: 0)
        result = 31 * result + (price?.hashCode() ?: 0)
        result = 31 * result + (action_id?.hashCode() ?: 0)
        result = 31 * result + (Quantity?.hashCode() ?: 0)
        result = 31 * result + (orderFrom?.hashCode() ?: 0)
        result = 31 * result + (orderTo?.hashCode() ?: 0)
        result = 31 * result + (timestamp?.hashCode() ?: 0)
        return result
    }
}