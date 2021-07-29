package com.android.testproject1.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.android.testproject1.BR
import com.android.testproject1.IMainActivity
import com.android.testproject1.databinding.ItemDiscoverBinding
import com.android.testproject1.databinding.SearchRecyclerviewGridBinding
import com.android.testproject1.model.Offer
import com.android.testproject1.model.ProductExplore

class SearchAdapter2(private val context: Context,
                     private var offerList:MutableList<Offer>): RecyclerView.
                     Adapter<SearchAdapter2.BindingViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder {

        val rooView: ViewDataBinding = ItemDiscoverBinding.inflate(LayoutInflater.from(context),parent,false)
        return BindingViewHolder(rooView)
    }

    override fun onBindViewHolder(holder: BindingViewHolder, position: Int) {

        val exploreOffer = offerList[position]

        holder.itemBinding.setVariable(BR.offerItem,exploreOffer)
        holder.itemBinding.setVariable(BR.listener, context as IMainActivity)
        holder.itemBinding.executePendingBindings()


    }

    override fun getItemCount(): Int {
        return offerList.size
    }

    fun updateData(data: List<Offer>){
        offerList.clear()
       offerList.addAll(data)
        notifyDataSetChanged()
    }


    class BindingViewHolder(val itemBinding: ViewDataBinding)
        : RecyclerView.ViewHolder(itemBinding.root)
}