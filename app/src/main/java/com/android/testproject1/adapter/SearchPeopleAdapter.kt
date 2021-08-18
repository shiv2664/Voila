package com.android.testproject1.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.android.testproject1.BR
import com.android.testproject1.interfaces.IMainActivity
import com.android.testproject1.databinding.SearchPeopleItemBinding
import com.android.testproject1.model.Users

class SearchPeopleAdapter(private val context: Context, private var userList: MutableList<Users>): RecyclerView
.Adapter<SearchPeopleAdapter.BindingViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder {

        val rooView: ViewDataBinding = SearchPeopleItemBinding.inflate(LayoutInflater.from(context),parent,false)
        return BindingViewHolder(rooView)
    }

    override fun onBindViewHolder(holder: BindingViewHolder, position: Int) {

        val user = userList[position]

        holder.itemBinding.setVariable(BR.userItem,user)
        holder.itemBinding.setVariable(BR.listener, context as IMainActivity)
        holder.itemBinding.executePendingBindings()
    }
    override fun getItemCount(): Int {
        Log.d("MyTag"," size userList in getItemCount "+ userList.size.toString())
        return userList.size

    }

    fun updateData(newUserList: List<Users>) {
        if (userList!=newUserList) {
            Log.d("MyTag", " size before userList in Details " + userList.size.toString())
            Log.d("MyTag", " size before newUserList in Details " + newUserList.size.toString())
            userList.clear()
            Log.d("MyTag ", "size after userList in Details " + userList.size.toString())
            userList.addAll(newUserList)
            notifyDataSetChanged()
            Log.d("MyTag", "notifyDataSetChanged")
        }
    }

    class BindingViewHolder(val itemBinding: ViewDataBinding)
        : RecyclerView.ViewHolder(itemBinding.root){}
}