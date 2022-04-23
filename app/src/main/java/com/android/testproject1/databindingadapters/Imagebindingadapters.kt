package com.android.testproject1.databindingadapters

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.airbnb.lottie.LottieAnimationView
import com.android.testproject1.R
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.imageview.ShapeableImageView
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*


@BindingAdapter("backgroundImage")
fun setImageFromURls(view: ShapeableImageView, fileName: String?) {

        Log.d("MyTag","file name is"+fileName)

    if (fileName != null) {
        if(fileName.isNotEmpty()){
            Glide.with(view.context)
                .load(fileName)
                .placeholder(R.drawable.loading_png)
                .thumbnail(0.05f)
                .into(view)
        }
    }

}

@BindingAdapter(value = ["lottieView","imageUrl"], requireAll = true)
fun setImageResdkmoveLottie(view: ShapeableImageView,lottiAnimView:LottieAnimationView,imageUrl: String){

    if(imageUrl.isNotEmpty()){
        Glide.with(view.context)
            .load(imageUrl)
            .addListener(imageLoadingListener(lottiAnimView))
            .thumbnail(0.05f)
            .into(view)
    }
}

fun imageLoadingListener(pendingImage: LottieAnimationView): RequestListener<Drawable?>? {
    return object : RequestListener<Drawable?> {
        override fun onLoadFailed(e: GlideException?, model: Any?, target: com.bumptech.glide.request.target.Target<Drawable?>?, isFirstResource: Boolean): Boolean {
            return false
        }

        override fun onResourceReady(
            resource: Drawable?,
            model: Any?,
            target: com.bumptech.glide.request.target.Target<Drawable?>?,
            dataSource: DataSource?,
            isFirstResource: Boolean
        ): Boolean {
            pendingImage.pauseAnimation()
            pendingImage.visibility = View.GONE
            return false
        }
    }
}

@BindingAdapter("backgroundImageCircle")
fun setCircularImageFromURls(view: de.hdodenhof.circleimageview.CircleImageView, fileName: String?) {
    val requestOptions = RequestOptions()
    requestOptions.placeholder(R.drawable.ic_person_black_24dp)
    requestOptions.error(R.drawable.ic_baseline_people_outline_24)
//        Log.d("MyTag","file name is"+fileName)
    if (fileName != null) {
        if(fileName.isNotEmpty()){
            Glide.with(view.context)
                .applyDefaultRequestOptions(requestOptions)
                .load(fileName)
                .into(view)
        }
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

@BindingAdapter(value = ["discountedPrice","originalPrice"], requireAll = true)
fun setPriceText(view: TextView,discountedPrice:String,originalPrice:String) {

    if (discountedPrice.isBlank()){
        view.text=" ₹$originalPrice "
    }else{
        view.text=" ₹$discountedPrice "
    }
//    val formatter = NumberFormat.getCurrencyInstance(Locale.US)
//    view.text = "Rs. $price / each"
}

@BindingAdapter("ratingText")
fun setTotalRatingText(view: TextView, rating: Int) {
    view.text = "($rating)"
}

@BindingAdapter("android:text")
fun setTextViewText(view: TextView, text: Double) {
    view.text = "$text"
}

@BindingAdapter("setDate")
fun setDate(view: TextView,date: Date){
    val dateFormat = SimpleDateFormat("hh:mm a")

    view.text="Delivery Req : "+dateFormat.format(date)
}

//@BindingAdapter(value = ["discountedPrice","OriginalPrice"], requireAll = true)
//fun setOfferPrice(view: TextView,discountedPrice:String,originalPrice:String){
//    if (discountedPrice.isBlank()){
//        view.text=" ₹$originalPrice "
//    }else{
//        view.text=" ₹$discountedPrice "
//    }
//}