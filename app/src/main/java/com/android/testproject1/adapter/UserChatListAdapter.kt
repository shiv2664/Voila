package com.android.testproject1.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.android.testproject1.BR
import com.android.testproject1.databinding.UserChatlistGroupBinding
import com.android.testproject1.interfaces.IMainActivity
import com.android.testproject1.databinding.UserChatlistRecyclerviewLayoutBinding
import com.android.testproject1.room.enteties.UsersChatListEntity

class UserChatListAdapter(private val context: Context, private var userList: MutableList<UsersChatListEntity>): RecyclerView
.Adapter<UserChatListAdapter.BindingViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder {

        if (viewType==0){
            val rooView: ViewDataBinding = UserChatlistRecyclerviewLayoutBinding.inflate(LayoutInflater
                .from(context),parent,false)
            return BindingViewHolder(rooView)

        } else{
            val rooView2: ViewDataBinding = UserChatlistGroupBinding.inflate(LayoutInflater
                .from(context),parent,false)
            return BindingViewHolder(rooView2)

        }


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

    override fun getItemViewType(position: Int): Int {

       if ( userList[position].viewType=="group"){
           return 1
       }else if (userList[position].viewType=="user"){
           return 0
       }

        return super.getItemViewType(position)
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