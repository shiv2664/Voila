package com.android.testproject1.model

import java.util.*
import com.google.firebase.firestore.ServerTimestamp

data class Chat(
        val sender: String="",
        val receiver: String="",
        val message: String="",
        val isseen: Boolean = false,
        var MessageId: String="",
        var name:String="",
        @ServerTimestamp
        val timestamp: Date? = null
)


