package com.android.testproject1.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.android.testproject1.BR
import com.android.testproject1.IMainActivity
import com.android.testproject1.databinding.ChatItemLeftBinding
import com.android.testproject1.databinding.ChatItemRightBinding
import com.android.testproject1.model.Chat
import com.google.firebase.auth.FirebaseAuth
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class ChatGroupAdapter(private val context: Context, private var chats: MutableList<Chat>): RecyclerView
.Adapter<ChatGroupAdapter.BindingViewHolder>() {

    private val MSG_TYPE_LEFT = 0
    private val MSG_TYPE_RIGHT = 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder {

        return if (viewType == MSG_TYPE_RIGHT) {
            val rooView: ViewDataBinding = ChatItemRightBinding.inflate(LayoutInflater.from(context),parent,false)
            BindingViewHolder(rooView)
        } else {
            val rooView: ViewDataBinding = ChatItemLeftBinding.inflate(LayoutInflater.from(context),parent,false)
            BindingViewHolder(rooView)
        }
    }


    override fun onBindViewHolder(holder: BindingViewHolder, position: Int) {

        val chatMessage = chats[position]

        holder.itemBinding.setVariable(BR.chatMessage, chatMessage)
        holder.itemBinding.setVariable(BR.listener, context as IMainActivity)
        holder.itemBinding.executePendingBindings()
    }

    override fun getItemCount(): Int {
        return chats.size

    }

    fun updateData(newChatList: List<Chat>) {
        Log.d("MyTag size before ",chats.size.toString())
        chats.clear()
        Log.d("MyTag ",chats.size.toString())
        chats.addAll(newChatList)
        this.notifyDataSetChanged()
        Log.d("MyTag","notifyDataSetChanged")
    }


    override fun getItemViewType(position: Int): Int {
        val fUser = FirebaseAuth.getInstance().currentUser
        return if (chats[position].sender == fUser?.uid) {
            MSG_TYPE_RIGHT
        } else {
            MSG_TYPE_LEFT
        }
    }

    class BindingViewHolder(val itemBinding: ViewDataBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {}


    companion object {

        fun getTimeDate(timestamp: Long): String? {
            return try {
                var dateFormat = DateFormat.getTimeInstance()
                val netDate = Date(timestamp)
                dateFormat = SimpleDateFormat("HH:mm")
                dateFormat.format(netDate)
            } catch (e: Exception) {
                "date"
            }
        }
    }


}