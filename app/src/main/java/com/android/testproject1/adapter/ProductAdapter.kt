package com.android.testproject1.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.android.testproject1.BR
import com.android.testproject1.IMainActivity
import com.android.testproject1.databinding.HomeRecyclerviewBinding
import com.android.testproject1.model.Product
import com.google.android.material.imageview.ShapeableImageView

class ProductAdapter(private val context: Context,private var dataList2:MutableList<Product>):RecyclerView
.Adapter<ProductAdapter.BindingViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder {

        val rooView:ViewDataBinding=HomeRecyclerviewBinding.inflate(LayoutInflater.from(context),parent,false)
        return BindingViewHolder(rooView)
    }

    override fun onBindViewHolder(holder: BindingViewHolder, position: Int) {

        val product = dataList2[position]

        holder.itemBinding.setVariable(BR.productitem2,product)
        holder.itemBinding.setVariable(BR.listener, context as IMainActivity)
        holder.itemBinding.executePendingBindings()
    }
    override fun getItemCount(): Int {
       return dataList2.size
    }

    fun UpdateDataList(data: List<Product>){
        dataList2.clear()
        dataList2.addAll(data)
        notifyDataSetChanged()
    }

    class BindingViewHolder(val itemBinding:ViewDataBinding)
        :RecyclerView.ViewHolder(itemBinding.root){}
}