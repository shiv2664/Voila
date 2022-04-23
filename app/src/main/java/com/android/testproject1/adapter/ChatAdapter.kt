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
import com.android.testproject1.room.enteties.AppDatabase
import com.android.testproject1.room.enteties.ChatRoomEntity
import com.android.testproject1.room.enteties.UserImagesRoomEntity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.chat_item_left.view.*
import kotlinx.android.synthetic.main.chat_item_right.view.*
import kotlinx.android.synthetic.main.chat_item_right.view.profile_image
import kotlinx.android.synthetic.main.order_history_item.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class ChatAdapter(private val context: Context, private var chats: MutableList<ChatRoomEntity>): RecyclerView
    .Adapter<ChatAdapter.BindingViewHolder>() {

    private val localDatabase: AppDatabase = AppDatabase.getInstance(context)!!
    private lateinit var firestore: FirebaseFirestore


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder {

        firestore= FirebaseFirestore.getInstance()

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

        var thumbnailImage:String?=null
        var userName:String?=null
        CoroutineScope(Dispatchers.IO).launch {

            thumbnailImage = chats[position].sender?.let {
                localDatabase.appDao()?.getUserImage(it) }

            if (thumbnailImage==null){
                chats[position].sender?.let {
                    firestore.collection("Users").document(it).get().addOnSuccessListener {
                        if (it != null) {
                            if (it.exists()) {
                                // convert document to POJO
                                val userImage: UserImagesRoomEntity? = it.toObject(
                                    UserImagesRoomEntity::class.java)
                                if (userImage != null) {
                                    CoroutineScope(Dispatchers.IO).launch {
                                        localDatabase.appDao()?.insertImage(userImage)

                                        thumbnailImage = chats[position].sender?.let {
                                            localDatabase.appDao()?.getUserImage(it) }

                                        userName = chats[position].id?.let {
                                            localDatabase.appDao()?.getUserName(it) }

                                        withContext(Dispatchers.Main){
//                                            holder.itemBinding.root.name.text=userName
                                            if (thumbnailImage!=null) {
                                                Glide.with(context)
                                                    .load(thumbnailImage)
                                                    .into(holder.itemBinding.root.profile_image)
                                            }

                                        }
                                    }

                                }
                            }
                        }
                    }
                }

            }else{
                userName = chats[position].sender?.let {
                    localDatabase.appDao()?.getUserName(it) }

                withContext(Dispatchers.Main){
//                    holder.itemBinding.root.name.text=userName

                    if (thumbnailImage!=null) {
                        Glide.with(context)
                            .load(thumbnailImage)
                            .into(holder.itemBinding.root.profile_image)
                    }


//                    Log.d("MyTag"," thumbnail image is "+thumbnailImage)
                }


            }

        }



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









