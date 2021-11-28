package com.android.testproject1.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.android.testproject1.BR
import com.android.testproject1.databinding.ProfileOfferCurrentuserRecyclerviewBinding
import com.android.testproject1.databinding.ProfilePostRecyclerviewBinding
import com.android.testproject1.interfaces.IMainActivity
import com.android.testproject1.room.enteties.OfferRoomEntity

class ProfilePostCurrentUserRecyclerviewAdapter(private val context: Context, private var postList: MutableList<OfferRoomEntity>):
    RecyclerView.Adapter<ProfilePostCurrentUserRecyclerviewAdapter.BindingViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder {

        val rooView: ViewDataBinding = ProfileOfferCurrentuserRecyclerviewBinding.inflate(LayoutInflater.from(context),parent,false)
        return BindingViewHolder(rooView)
    }

    override fun onBindViewHolder(holder: BindingViewHolder, position: Int) {

        val offerItem = postList[position]

        holder.itemBinding.setVariable(BR.offerItem,offerItem)
        holder.itemBinding.setVariable(BR.listener, context as IMainActivity)
        holder.itemBinding.executePendingBindings()
    }
    override fun getItemCount(): Int {
        Log.d("MyTag","Size is Current user  : "+postList.size)
        return postList.size
    }

    fun updateData(newDataList: List<OfferRoomEntity>) {
//        postList.clear()
//        postList.addAll(newDataList)
//        this.notifyDataSetChanged()

        val oldList=postList
        val diffUtil:DiffUtil.DiffResult=DiffUtil.calculateDiff(
            PostItemDiffCallback(
                oldList, newDataList
            )
        )
        postList=newDataList.toMutableList()
        diffUtil.dispatchUpdatesTo(this)

    }

    class PostItemDiffCallback(
        var oldNotificationList :List<OfferRoomEntity>,
        private var newNotificationList:List<OfferRoomEntity>
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
        : RecyclerView.ViewHolder(itemBinding.root){}


}