package com.android.testproject1.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.android.testproject1.BR
import com.android.testproject1.databinding.OrderHistoryItemBinding
import com.android.testproject1.interfaces.IMainActivity
import com.android.testproject1.room.enteties.NotificationsRoomEntity
import com.android.testproject1.room.enteties.OrdersRoomEntity
import es.dmoral.toasty.Toasty

class OrderHistoryAdapter(private val context: Context, private var notificationList: MutableList<OrdersRoomEntity>):

    RecyclerView.Adapter<OrderHistoryAdapter.BindingViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder {

        val rooView: ViewDataBinding = OrderHistoryItemBinding.inflate(LayoutInflater.from(context),parent,false)
        return BindingViewHolder(rooView)
    }

    override fun onBindViewHolder(holder: BindingViewHolder, position: Int) {

        val post = notificationList[position]
        holder.itemBinding.setVariable(BR.notificationItem,post)
        holder.itemBinding.setVariable(BR.listener, context as IMainActivity)
        holder.itemBinding.executePendingBindings()

//        holder.itemBinding.root.Accept.setOnClickListener {
//            holder.itemBinding.root.Reject.visibility=View.GONE
//            holder.itemBinding.root.Accept.visibility=View.GONE
//            holder.itemBinding.root.Cancelled.visibility=View.VISIBLE
//            holder.itemBinding.root.Ready.visibility=View.VISIBLE
//        }

    }
    override fun getItemCount(): Int {
        Log.d("MyTag","notification list size : "+notificationList.size)
        return notificationList.size
    }

    fun showToast(){
//        val toast = Toast.makeText(context,"New Notifications", Toast.LENGTH_SHORT)
//        toast.setGravity(Gravity.TOP , 0, 0)
//        toast.show()
        val toasty= Toasty.success(context,"New Notifications", Toasty.LENGTH_LONG,false)
        toasty.show()
    }

    fun updateData(newDataList: List<OrdersRoomEntity>) {
//        notificationList.clear()
//        notificationList.addAll(newDataList)
//        notifyDataSetChanged()

        val oldList=notificationList
        val diffUtil:DiffUtil.DiffResult=DiffUtil.calculateDiff(
            NotificationItemDiffCallback(
                oldList, newDataList
            )
        )
        notificationList=newDataList.toMutableList()
        diffUtil.dispatchUpdatesTo(this)
    }

    class NotificationItemDiffCallback(
        var oldNotificationList :List<OrdersRoomEntity>,
        var newNotificationList:List<OrdersRoomEntity>
    ): DiffUtil.Callback(){
        override fun getOldListSize(): Int {
            return  oldNotificationList.size
        }

        override fun getNewListSize(): Int {
            return  newNotificationList.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return  (oldNotificationList[oldItemPosition].idOrder== newNotificationList[newItemPosition].idOrder)
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return  (oldNotificationList[oldItemPosition] == newNotificationList[newItemPosition])
        }
    }

    class BindingViewHolder(val itemBinding: ViewDataBinding)
        : RecyclerView.ViewHolder(itemBinding.root){}

}