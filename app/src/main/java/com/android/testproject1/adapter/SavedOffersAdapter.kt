package com.android.testproject1.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.android.testproject1.BR
import com.android.testproject1.databinding.ItemDiscoverBinding
import com.android.testproject1.databinding.ItemSavedOfferBinding
import com.android.testproject1.interfaces.IMainActivity
import com.android.testproject1.room.enteties.OffersSavedRoomEntity

class SavedOffersAdapter(private val context: Context,
                     private var offerList:MutableList<OffersSavedRoomEntity>): RecyclerView.
Adapter<SavedOffersAdapter.BindingViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder {

        val rooView: ViewDataBinding = ItemSavedOfferBinding.inflate(LayoutInflater.from(context),parent,false)
        return BindingViewHolder(rooView)
    }

    override fun onBindViewHolder(holder: BindingViewHolder, position: Int) {

        val savedOffer = offerList[position]

        holder.itemBinding.setVariable(BR.offerItem,savedOffer)
        holder.itemBinding.setVariable(BR.listener, context as IMainActivity)
        holder.itemBinding.executePendingBindings()


    }

    override fun getItemCount(): Int {
        return offerList.size
    }

    fun updateData(data: List<OffersSavedRoomEntity>){
        offerList.clear()
        offerList.addAll(data)
        notifyDataSetChanged()
    }


    class BindingViewHolder(val itemBinding: ViewDataBinding)
        : RecyclerView.ViewHolder(itemBinding.root)
}