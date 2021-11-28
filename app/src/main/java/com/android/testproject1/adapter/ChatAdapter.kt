package com.android.testproject1.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.android.testproject1.BR
import com.android.testproject1.interfaces.IMainActivity
import com.android.testproject1.databinding.ChatItemLeftBinding
import com.android.testproject1.databinding.ChatItemRightBinding
import com.android.testproject1.room.enteties.ChatRoomEntity
import com.google.firebase.auth.FirebaseAuth
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class ChatAdapter(private val context: Context, private var chats: MutableList<ChatRoomEntity>): RecyclerView
    .Adapter<ChatAdapter.BindingViewHolder>() {

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

    fun updateData(newChatList: MutableList<ChatRoomEntity>) {
        val oldList=chats
        val diffUtil:DiffUtil.DiffResult=DiffUtil.calculateDiff(ChatItemDiffCallback(
            oldList,newChatList
        ))
        chats=newChatList
        diffUtil.dispatchUpdatesTo(this)
    }


    class ChatItemDiffCallback(
        var oldChatList :List<ChatRoomEntity>,
        var newChatList:List<ChatRoomEntity>
    ): DiffUtil.Callback() {
        override fun getOldListSize(): Int {
           return  oldChatList.size
        }

        override fun getNewListSize(): Int {
            return newChatList.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {

            return  (oldChatList[oldItemPosition].MessageId== newChatList[newItemPosition].MessageId)
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {

            return  (oldChatList[oldItemPosition] == newChatList[newItemPosition])
        }
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
        const val MSG_TYPE_LEFT = 0
        const val MSG_TYPE_RIGHT = 1
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









