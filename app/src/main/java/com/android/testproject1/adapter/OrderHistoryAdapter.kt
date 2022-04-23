package com.android.testproject1.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.android.testproject1.BR
import com.android.testproject1.databinding.OrderHistoryItemBinding
import com.android.testproject1.interfaces.IMainActivity
import com.android.testproject1.room.enteties.AppDatabase
import com.android.testproject1.room.enteties.OrdersRoomEntity
import com.android.testproject1.room.enteties.UserImagesRoomEntity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.notifications_item.view.*
import kotlinx.android.synthetic.main.order_history_item.view.*
import kotlinx.android.synthetic.main.order_history_item.view.Delivered
import kotlinx.android.synthetic.main.order_history_item.view.Reject
import kotlinx.android.synthetic.main.order_history_item.view.image
import kotlinx.android.synthetic.main.order_history_item.view.lottiViewProfile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OrderHistoryAdapter(private val context: Context, private var notificationList: MutableList<OrdersRoomEntity>):

    RecyclerView.Adapter<OrderHistoryAdapter.BindingViewHolder>() {

    private val localDatabase: AppDatabase = AppDatabase.getInstance(context)!!
    private lateinit var firestore: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth
    lateinit var currentUserId:String

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder {
        firestore= FirebaseFirestore.getInstance()
        firebaseAuth= FirebaseAuth.getInstance()
        currentUserId= firebaseAuth.currentUser?.uid.toString()

        val rooView: ViewDataBinding = OrderHistoryItemBinding.inflate(LayoutInflater.from(context),parent,false)
        return BindingViewHolder(rooView)
    }

    override fun onBindViewHolder(holder: BindingViewHolder, position: Int) {

        val post = notificationList[position]
        holder.itemBinding.setVariable(BR.notificationItem,post)
        holder.itemBinding.setVariable(BR.listener, context as IMainActivity)
        holder.itemBinding.executePendingBindings()


        if (notificationList[position].status=="Pending"){
            holder.itemBinding.root.Pending.visibility=View.VISIBLE
            holder.itemBinding.root.Accepted.visibility=View.GONE
            holder.itemBinding.root.Waiting.visibility=View.GONE
            holder.itemBinding.root.Delivered.visibility=View.GONE
            holder.itemBinding.root.Cancel.visibility=View.GONE
            holder.itemBinding.root.Reject.visibility=View.GONE
        } else if (notificationList[position].status=="Accepted"){
            holder.itemBinding.root.Pending.visibility=View.GONE
            holder.itemBinding.root.Accepted.visibility=View.VISIBLE
            holder.itemBinding.root.Waiting.visibility=View.GONE
            holder.itemBinding.root.Delivered.visibility=View.GONE
            holder.itemBinding.root.Cancel.visibility=View.GONE
            holder.itemBinding.root.Reject.visibility=View.GONE
        }else if(notificationList[position].status=="Order Ready"){
            holder.itemBinding.root.Pending.visibility=View.GONE
            holder.itemBinding.root.Accepted.visibility=View.GONE
            holder.itemBinding.root.Waiting.visibility=View.VISIBLE
            holder.itemBinding.root.Delivered.visibility=View.GONE
            holder.itemBinding.root.Cancel.visibility=View.GONE
            holder.itemBinding.root.Reject.visibility=View.GONE
        }else if (notificationList[position].status=="Delivered"){
            holder.itemBinding.root.Pending.visibility=View.GONE
            holder.itemBinding.root.Accepted.visibility=View.GONE
            holder.itemBinding.root.Waiting.visibility=View.GONE
            holder.itemBinding.root.Delivered.visibility=View.VISIBLE
            holder.itemBinding.root.Cancel.visibility=View.GONE
            holder.itemBinding.root.Reject.visibility=View.GONE
        }else if (notificationList[position].status=="Order Cancelled"){
            holder.itemBinding.root.Pending.visibility=View.GONE
            holder.itemBinding.root.Accepted.visibility=View.GONE
            holder.itemBinding.root.Waiting.visibility=View.GONE
            holder.itemBinding.root.Delivered.visibility=View.GONE
            holder.itemBinding.root.Cancel.visibility=View.VISIBLE
            holder.itemBinding.root.Reject.visibility=View.GONE
        }else if (notificationList[position].status=="Order Rejected"){
            holder.itemBinding.root.Pending.visibility=View.GONE
            holder.itemBinding.root.Accepted.visibility=View.GONE
            holder.itemBinding.root.Waiting.visibility=View.GONE
            holder.itemBinding.root.Delivered.visibility=View.GONE
            holder.itemBinding.root.Cancel.visibility=View.GONE
            holder.itemBinding.root.Reject.visibility=View.VISIBLE
        }


        var thumbnailImage:String?=null
        var userName:String?=null
        CoroutineScope(Dispatchers.IO).launch {

            if (notificationList[position].orderFrom==currentUserId){

                thumbnailImage = notificationList[position].orderTo?.let {
                    localDatabase.appDao()?.getUserImage(it) }

                if (thumbnailImage==null){
                    notificationList[position].orderTo?.let {
                        firestore.collection("Users").document(it).get().addOnSuccessListener {
                            if (it != null) {
                                if (it.exists()) {
                                    // convert document to POJO
                                    val userImage: UserImagesRoomEntity? = it.toObject(UserImagesRoomEntity::class.java)
                                    if (userImage != null) {
                                        CoroutineScope(Dispatchers.IO).launch {
                                            localDatabase.appDao()?.insertImage(userImage)

                                            thumbnailImage = notificationList[position].orderTo?.let {
                                                localDatabase.appDao()?.getUserImage(it) }

                                            userName = notificationList[position].orderTo?.let {
                                                localDatabase.appDao()?.getUserName(it) }

                                            withContext(Dispatchers.Main){
                                                holder.itemBinding.root.title.text = userName
                                                if (thumbnailImage!=null) {
                                                    Glide.with(context)
                                                        .load(thumbnailImage)
                                                        .addListener(imageLoadingListener(holder.itemBinding.root.lottiViewProfile))
                                                        .into(holder.itemBinding.root.image)
                                                }

                                            }
                                        }

                                    }
                                }
                            }
                        }
                    }

                }else{
                    userName = notificationList[position].orderTo?.let {
                        localDatabase.appDao()?.getUserName(it) }

                    withContext(Dispatchers.Main){
                        holder.itemBinding.root.title.text=userName

                        if (thumbnailImage!=null) {
                            Glide.with(context)
                                .load(thumbnailImage)
                                .addListener(imageLoadingListener(holder.itemBinding.root.lottiViewProfile))
                                .into(holder.itemBinding.root.image)
                        }

                    }

                }


            }else{
                thumbnailImage = notificationList[position].userId?.let {
                    localDatabase.appDao()?.getUserImage(it) }

                if (thumbnailImage==null){
                    notificationList[position].userId?.let {
                        firestore.collection("Users").document(it).get().addOnSuccessListener {
                            if (it != null) {
                                if (it.exists()) {
                                    // convert document to POJO
                                    val userImage: UserImagesRoomEntity? = it.toObject(UserImagesRoomEntity::class.java)
                                    if (userImage != null) {
                                        CoroutineScope(Dispatchers.IO).launch {
                                            localDatabase.appDao()?.insertImage(userImage)

                                            thumbnailImage = notificationList[position].userId?.let {
                                                localDatabase.appDao()?.getUserImage(it) }

                                            userName = notificationList[position].userId?.let {
                                                localDatabase.appDao()?.getUserName(it) }

                                            withContext(Dispatchers.Main){
                                                holder.itemBinding.root.title.text = userName
                                                if (thumbnailImage!=null) {
                                                    Glide.with(context)
                                                        .load(thumbnailImage)
                                                        .addListener(imageLoadingListener(holder.itemBinding.root.lottiViewProfile))
                                                        .into(holder.itemBinding.root.image)
                                                }

                                            }
                                        }

                                    }
                                }
                            }
                        }
                    }

                }else{
                    userName = notificationList[position].userId?.let {
                        localDatabase.appDao()?.getUserName(it) }

                    withContext(Dispatchers.Main){
                        holder.itemBinding.root.title.text=userName

                        if (thumbnailImage!=null) {
                            Glide.with(context)
                                .load(thumbnailImage)
                                .addListener(imageLoadingListener(holder.itemBinding.root.lottiViewProfile))
                                .into(holder.itemBinding.root.image)
                        }

                    }

                }

            }

//            if (thumbnailImage==null){
//                notificationList[position].userId?.let {
//                    firestore.collection("Users").document(it).get().addOnSuccessListener {
//                        if (it != null) {
//                            if (it.exists()) {
//                                // convert document to POJO
//                                val userImage: UserImagesRoomEntity? = it.toObject(UserImagesRoomEntity::class.java)
//                                if (userImage != null) {
//                                    CoroutineScope(Dispatchers.IO).launch {
//                                        localDatabase.appDao()?.insertImage(userImage)
//
//                                        thumbnailImage = notificationList[position].userId?.let {
//                                            localDatabase.appDao()?.getUserImage(it) }
//
//                                        userName = notificationList[position].userId?.let {
//                                            localDatabase.appDao()?.getUserName(it) }
//
//                                        withContext(Dispatchers.Main){
//                                            holder.itemBinding.root.title.text = userName
//                                            if (thumbnailImage!=null) {
//                                                Glide.with(context)
//                                                    .load(thumbnailImage)
//                                                    .addListener(imageLoadingListener(holder.itemBinding.root.lottiViewProfile))
//                                                    .into(holder.itemBinding.root.image)
//                                            }
//
//                                        }
//                                    }
//
//                                }
//                            }
//                        }
//                    }
//                }
//
//            }else{
//                userName = notificationList[position].userId?.let {
//                    localDatabase.appDao()?.getUserName(it) }
//
//                withContext(Dispatchers.Main){
//                    holder.itemBinding.root.title.text=userName
//
//                    if (thumbnailImage!=null) {
//                        Glide.with(context)
//                            .load(thumbnailImage)
//                            .addListener(imageLoadingListener(holder.itemBinding.root.lottiViewProfile))
//                            .into(holder.itemBinding.root.image)
//                    }
//
//                }
//
//            }

        }

    }

    fun imageLoadingListener(pendingImage: LottieAnimationView): RequestListener<Drawable?>? {
        return object : RequestListener<Drawable?> {
            override fun onLoadFailed(e: GlideException?, model: Any?, target: com.bumptech.glide.request.target.Target<Drawable?>?, isFirstResource: Boolean): Boolean {
                return false
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: com.bumptech.glide.request.target.Target<Drawable?>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                pendingImage.pauseAnimation()
                pendingImage.visibility = View.GONE
                return false
            }
        }
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