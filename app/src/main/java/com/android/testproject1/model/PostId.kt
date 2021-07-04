package com.android.testproject1.model


open class PostId {
    var postId: String? = null
    fun <T : PostId?> withId(id: String): T {
        postId = id
        return this as T
    }
}