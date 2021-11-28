package com.android.testproject1.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.android.testproject1.BR
import com.android.testproject1.databinding.ItemDiscoverBinding
import com.android.testproject1.databinding.ItemSavedOfferBinding
import com.android.testproject1.interfaces.IMainActivity
import com.android.testproject1.room.enteties.OfferRoomEntity
import com.android.testproject1.room.enteties.OffersSavedRoomEntity

class SavedOffersAdapter(private val context: Context, private var offerList:MutableList<OffersSavedRoomEntity>): RecyclerView.
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
//        offerList.clear()
//        offerList.addAll(data)
//        notifyDataSetChanged()

        val oldList=offerList
        val diffUtil:DiffUtil.DiffResult=DiffUtil.calculateDiff(
            PostItemDiffCallback(
                oldList, data
            )
        )
        offerList=data.toMutableList()
        diffUtil.dispatchUpdatesTo(this)

    }

    class PostItemDiffCallback(
        var oldNotificationList :List<OffersSavedRoomEntity>,
        private var newNotificationList:List<OffersSavedRoomEntity>
    ): DiffUtil.Callback(){
        override fun getOldListSize(): Int {
            return  oldNotificationList.size
        }

        override fun getNewListSize(): Int {
            return  newNotificationList.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return  (oldNotificationList[oldItemPosition].postId== newNotificationList[newItemPosition].postId)
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return  (oldNotificationList[oldItemPosition] == newNotificationList[newItemPosition])
        }
    }


    class BindingViewHolder(val itemBinding: ViewDataBinding)
        : RecyclerView.ViewHolder(itemBinding.root)
}