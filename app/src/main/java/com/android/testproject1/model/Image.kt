package com.android.testproject1.model

data class Image(val url: String, val width: Int, val height: Int) {
    val aspectRatio: Float
        get() = width.toFloat() / height.toFloat()
}
