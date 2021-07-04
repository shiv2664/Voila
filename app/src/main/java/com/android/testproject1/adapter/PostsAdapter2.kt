package com.android.testproject1.adapter

import android.content.Context
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.android.testproject1.BR
import com.android.testproject1.IMainActivity
import com.android.testproject1.databinding.HomeRecyclerview2Binding
import com.android.testproject1.databinding.HomeRecyclerviewBinding
import com.android.testproject1.model.MultipleImage
import com.android.testproject1.model.Post
import java.util.*

class PostsAdapter2(private val context: Context, private var postList: MutableList<Post>):
    RecyclerView.Adapter<PostsAdapter2.BindingViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder {

        val rooView: ViewDataBinding = HomeRecyclerview2Binding.inflate(LayoutInflater.from(context),parent,false)
        return BindingViewHolder(rooView)
    }

    override fun onBindViewHolder(holder: BindingViewHolder, position: Int) {

        val post = postList[position]

        holder.itemBinding.setVariable(BR.postItem,post)
        holder.itemBinding.setVariable(BR.listener, context as IMainActivity)
        holder.itemBinding.executePendingBindings()
    }
    override fun getItemCount(): Int {
        return postList.size
    }

    fun updateData(newDataList: List<Post>) {
        postList.clear()
        postList.addAll(newDataList)
        this.notifyDataSetChanged()
    }

    class BindingViewHolder(val itemBinding: ViewDataBinding)
        : RecyclerView.ViewHolder(itemBinding.root){}


}