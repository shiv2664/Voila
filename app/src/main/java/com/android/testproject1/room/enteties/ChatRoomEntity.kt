package com.android.testproject1.room.enteties

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*
import com.google.firebase.firestore.ServerTimestamp

@Entity
data class ChatRoomEntity(
        @PrimaryKey(autoGenerate = false)
        val id :String = "",
        val sender: String="",
        val receiver: String="",
        val message: String="",
        val isseen: Boolean = false,
        var MessageId: String="",
        val chatKey: String="",
        var name:String="",
        val postId:String="",
        val groupId:String="",
        @ServerTimestamp
        val timestamp: Date? = null
){
        override fun equals(other: Any?): Boolean {

                if (javaClass !=other?.javaClass){
                        return false
                }

                other as ChatRoomEntity

                if (id !=other.id){
                        return false
                }
                if (sender !=other.sender){
                        return false
                }
                if (receiver !=other.receiver){
                        return false
                }
                if (message !=other.message){
                        return false
                }
                if (isseen !=other.isseen){
                        return false
                }
                if (MessageId !=other.MessageId){
                        return false
                }
                if (chatKey !=other.chatKey){
                        return false
                }
                if (name !=other.name){
                        return false
                }
                if (postId !=other.postId){
                        return false
                }
                if (groupId !=other.groupId){
                        return false
                }
                if (timestamp !=other.timestamp){
                        return false
                }

                return true
        }
}




