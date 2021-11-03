package com.android.testproject1.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.android.testproject1.BR
import com.android.testproject1.databinding.PaymentRecyclerviewItemBinding
import com.android.testproject1.interfaces.IMainActivity
import com.android.testproject1.room.enteties.UsersChatListEntity

class PaymentRecyclerviewAdapter(private val context: Context, private var userList: MutableList<UsersChatListEntity>): RecyclerView
.Adapter<PaymentRecyclerviewAdapter.BindingViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder {

        val rooView: ViewDataBinding = PaymentRecyclerviewItemBinding.inflate(LayoutInflater.from(context),parent,false)
        return BindingViewHolder(rooView)
    }

    override fun onBindViewHolder(holder: BindingViewHolder, position: Int) {

        val user = userList[position]

        holder.itemBinding.setVariable(BR.userItem,user)
        holder.itemBinding.setVariable(BR.listener, context as IMainActivity)
        holder.itemBinding.executePendingBindings()
    }
    override fun getItemCount(): Int {
        Log.d("MyTag is", userList.size.toString())
        return userList.size

    }

    fun updateData(newUserList: List<UsersChatListEntity>) {
        Log.d("MyTag size before ", userList.size.toString())
        userList.clear()
        Log.d("MyTag ", userList.size.toString())
        userList.addAll(newUserList)
        this.notifyDataSetChanged()
        Log.d("MyTag", "notifyDataSetChanged")
    }

    class BindingViewHolder(val itemBinding: ViewDataBinding)
        : RecyclerView.ViewHolder(itemBinding.root){}
}