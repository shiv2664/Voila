package com.android.testproject1.databindingadapters

import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.android.testproject1.R
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.imageview.ShapeableImageView
import java.text.NumberFormat
import java.util.*

@BindingAdapter("backgroundImage")
fun setImageFromURls(view: ShapeableImageView, fileName: String) {
    val requestOptions: RequestOptions = RequestOptions()
//            .setDefaultRequestOptions(requestOptions)

    Log.d("MyTag",fileName)
    if(fileName.isNotEmpty()){
        Glide.with(view.context)
                .load(fileName)
                .into(view)
    }

}

@BindingAdapter(value = ["imageUrl", "placeholderImage", "errorImage"], requireAll = false)
fun loadImageFromInternet(view: ImageView, imageUrl: String, placeholderImage: Boolean, errorImage: Boolean) {

    var requestOptions: RequestOptions = RequestOptions()

    if (placeholderImage)
        requestOptions.placeholder(R.drawable.person3)

    if (errorImage)
        requestOptions.placeholder(R.drawable.person3)

    Glide.with(view.context)
            .setDefaultRequestOptions(requestOptions)
            .load(imageUrl)
            .into(view)

}

@BindingAdapter("priceText")
fun setPriceText(view: TextView, price: Double) {
    val formatter = NumberFormat.getCurrencyInstance(Locale.US)

    view.text = "Rs. $price / each"
}

@BindingAdapter("ratingText")
fun setTotalRatingText(view: TextView, rating: Int) {
    view.text = "($rating)"
}

@BindingAdapter("android:text")
fun setTextViewText(view: TextView, text: Double) {
    view.text = "$text"
}