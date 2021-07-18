package com.android.testproject1.adapter

import android.content.Context
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.android.testproject1.R
import com.android.testproject1.model.MultipleImage
import com.bumptech.glide.Glide
import java.util.ArrayList

class PostPhotosAdapter(private val context: Context?, private var IMAGES: ArrayList<MultipleImage>) : PagerAdapter() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun getCount(): Int {
        return IMAGES.size

    }

    override fun instantiateItem(view: ViewGroup, position: Int): Any {
        val imageLayout: View = inflater.inflate(R.layout.item_viewpager_image, view, false)!!
        val imageView: ImageView = imageLayout.findViewById(R.id.image)

        if (context != null) {
            Glide.with(context)
                .load(IMAGES[position].local_path)
                .into(imageView)
        }
        view.addView(imageLayout, 0)
        return imageLayout
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun restoreState(state: Parcelable?, loader: ClassLoader?) {}
    override fun saveState(): Parcelable? {
        return null
    }

}
