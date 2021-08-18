package com.android.testproject1.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.android.testproject1.BR
import com.android.testproject1.databinding.NotificationsItemBinding
import com.android.testproject1.interfaces.IMainActivity
import com.android.testproject1.model.Notifications
import kotlinx.android.synthetic.main.home_recyclerview3.view.*

class NotificationsAdapter(private val context: Context, private var notificationList: MutableList<Notifications>):
    RecyclerView.Adapter<NotificationsAdapter.BindingViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder {

        val rooView: ViewDataBinding = NotificationsItemBinding.inflate(LayoutInflater.from(context),parent,false)
        return BindingViewHolder(rooView)
    }

    override fun onBindViewHolder(holder: BindingViewHolder, position: Int) {

        val post = notificationList[position]
        holder.itemBinding.setVariable(BR.notificationItem,post)
        holder.itemBinding.setVariable(BR.listener, context as IMainActivity)
        holder.itemBinding.executePendingBindings()

    }
    override fun getItemCount(): Int {
        Log.d("MyTag","notification list size : "+notificationList.size)
        return notificationList.size
    }

    fun updateData(newDataList: List<Notifications>) {
        notificationList.clear()
        notificationList.addAll(newDataList)
        notifyDataSetChanged()
    }

    class BindingViewHolder(val itemBinding: ViewDataBinding)
        : RecyclerView.ViewHolder(itemBinding.root){}

}