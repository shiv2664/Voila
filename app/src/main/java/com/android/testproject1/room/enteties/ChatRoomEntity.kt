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
        @ServerTimestamp
        val timestamp: Date? = null
)


