package com.android.testproject1.adapter

import android.content.Context
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.android.testproject1.BR
import com.android.testproject1.interfaces.IMainActivity
import com.android.testproject1.databinding.HomeRecyclerview3Binding
import com.android.testproject1.model.MultipleImage
import com.android.testproject1.model.Post
import kotlinx.android.synthetic.main.home_recyclerview2.view.*
import kotlinx.android.synthetic.main.home_recyclerview3.view.*
import java.util.*

class PostsAdapter2(private val context: Context, private var postList: MutableList<Post>):
    RecyclerView.Adapter<PostsAdapter2.BindingViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder {

        val rooView: ViewDataBinding = HomeRecyclerview3Binding.inflate(LayoutInflater.from(context),parent,false)
        return BindingViewHolder(rooView)
    }

    override fun onBindViewHolder(holder: BindingViewHolder, position: Int) {

        val post = postList[position]
        holder.itemBinding.setVariable(BR.postItem,post)
        holder.itemBinding.setVariable(BR.listener, context as IMainActivity)
        holder.itemBinding.executePendingBindings()
        setupViews(holder,post)

    }
    override fun getItemCount(): Int {
        return postList.size
    }

    fun updateData(newDataList: List<Post>) {
        Log.d("MyTag","newdatalistt is : "+newDataList)
        postList.clear()
        postList.addAll(newDataList)
        notifyDataSetChanged()
    }

    class BindingViewHolder(val itemBinding: ViewDataBinding)
        : RecyclerView.ViewHolder(itemBinding.root){}


    private fun setupViews(holder: BindingViewHolder,post:Post) {

        if (post.image_count == 0) {
            holder.itemBinding.root.pager_layout.visibility = View.GONE
        }
        else if (post.image_count== 1) {
            val multipleImages: ArrayList<MultipleImage> = ArrayList<MultipleImage>()
            val photosAdapter = PostPhotosAdapter(context,multipleImages)

            setUrls(holder, multipleImages, photosAdapter,post)
            holder.itemBinding.root.pager.adapter = photosAdapter
            holder.itemBinding.root.indicator_holder.visibility = View.GONE
            photosAdapter.notifyDataSetChanged()
            holder.itemBinding.root.visibility = View.VISIBLE

        } else if (post.image_count > 0) {
            val multipleImages: ArrayList<MultipleImage> = ArrayList<MultipleImage>()
            val photosAdapter = PostPhotosAdapter(
                context,multipleImages)
            setUrls(holder, multipleImages, photosAdapter,post)

            holder.itemBinding.root.pager.adapter = photosAdapter
            photosAdapter.notifyDataSetChanged()
            holder.itemBinding.root.indicator.dotsClickable = true
            holder.itemBinding.root.indicator.setViewPager(holder.itemBinding.root.pager)

            //autoStartSlide(holder,multipleImages.size());
            holder.itemBinding.root.pager_layout.visibility = View.VISIBLE
            holder.itemBinding.root.visibility = View.VISIBLE
            holder.itemBinding.root.visibility = View.GONE
            holder.itemBinding.root.visibility = View.VISIBLE
        }
    }

    private fun setUrls(holder: BindingViewHolder, multipleImages: ArrayList<MultipleImage>,
                        photosAdapter: PostPhotosAdapter,post: Post) {
        val pos: Int = holder.adapterPosition
        val url0: String = post.image_url_0
        val url1: String = post.image_url_1
        val url2: String = post.image_url_2
        val url3: String = post.image_url_3
        val url4: String = post.image_url_4
        val url5: String = post.image_url_5
        val url6: String = post.image_url_6

        if (!TextUtils.isEmpty(url0)) {
            val image = MultipleImage(url0)
            multipleImages.add(image)
            photosAdapter.notifyDataSetChanged()
            Log.i("url0", url0)
        }
        if (!TextUtils.isEmpty(url1)) {
            val image = MultipleImage(url1)
            multipleImages.add(image)
            photosAdapter.notifyDataSetChanged()
            Log.i("url1", url1)
        }
        if (!TextUtils.isEmpty(url2)) {
            val image = MultipleImage(url2)
            multipleImages.add(image)
            photosAdapter.notifyDataSetChanged()
            Log.i("url2", url2)
        }
        if (!TextUtils.isEmpty(url3)) {
            val image = MultipleImage(url3)
            multipleImages.add(image)
            photosAdapter.notifyDataSetChanged()
            Log.i("url3", url3)
        }
        if (!TextUtils.isEmpty(url4)) {
            val image = MultipleImage(url4)
            multipleImages.add(image)
            photosAdapter.notifyDataSetChanged()
            Log.i("url4", url4)
        }
        if (!TextUtils.isEmpty(url5)) {
            val image = MultipleImage(url5)
            multipleImages.add(image)
            photosAdapter.notifyDataSetChanged()
            Log.i("url5", url5)
        }
        if (!TextUtils.isEmpty(url6)) {
            val image = MultipleImage(url6)
            multipleImages.add(image)
            photosAdapter.notifyDataSetChanged()
            Log.i("ur6", url6)
        }
    }

}