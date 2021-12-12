package com.android.testproject1.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.android.testproject1.BR
import com.android.testproject1.interfaces.IMainActivity
import com.android.testproject1.databinding.ItemDiscoverBinding
import com.android.testproject1.model.Offer
import kotlinx.android.synthetic.main.item_discover.view.*
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil

import com.android.testproject1.MainActivity
import com.android.testproject1.room.enteties.AppDatabase
import com.android.testproject1.room.enteties.ChatRoomEntity
import com.android.testproject1.room.enteties.OfferRoomEntity
import com.android.testproject1.room.enteties.UserImagesRoomEntity
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore

import com.shalan.mohamed.itemcounterview.CounterListener
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchAdapter2(private val context: Context,
                     private var offerList:MutableList<OfferRoomEntity>): RecyclerView.
                     Adapter<SearchAdapter2.BindingViewHolder>() {

    private var counterValue: String = "1"
    private var total="0"
    private val localDatabase: AppDatabase = AppDatabase.getInstance(context)!!
    private lateinit var firestore: FirebaseFirestore

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder {

        firestore= FirebaseFirestore.getInstance()

        val rooView: ViewDataBinding =
            ItemDiscoverBinding.inflate(LayoutInflater.from(context), parent, false)
        return BindingViewHolder(rooView)
    }

    override fun onBindViewHolder(holder: BindingViewHolder, position: Int) {

        val exploreOffer = offerList[position]

        holder.itemBinding.setVariable(BR.offerItem, exploreOffer)
        holder.itemBinding.setVariable(BR.listener, context as IMainActivity)
        holder.itemBinding.executePendingBindings()

        counterValue= holder.itemBinding.root.itemCounter.counterValue

        var thumbnailImage:String?=null
        var userName:String?=null
        CoroutineScope(Dispatchers.IO).launch {
            thumbnailImage = offerList[position].userId.let { localDatabase.appDao()?.getUserImage(it) }
            if (thumbnailImage==null){
                offerList[position].userId?.let {
                    firestore.collection("Users").document(it).get().addOnSuccessListener {
                        if (it != null) {
                            if (it.exists()) {
                                // convert document to POJO
                                val userImage: UserImagesRoomEntity? = it.toObject(UserImagesRoomEntity::class.java)
                                if (userImage != null) {
                                    CoroutineScope(Dispatchers.IO).launch {
                                        localDatabase.appDao()?.insertImage(userImage)
                                        thumbnailImage = offerList[position].userId?.let {
                                            localDatabase.appDao()?.getUserImage(it) }

                                        userName = offerList[position].userId?.let {
                                            localDatabase.appDao()?.getUserName(it) }

                                        withContext(Dispatchers.Main){
                                            holder.itemBinding.root.title.text=userName.toString()
                                            if (thumbnailImage!=null) {
                                                Glide.with(context)
                                                    .load(thumbnailImage)
                                                    .into(holder.itemBinding.root.profileImage)
                                            }


//                    Log.d("MyTag"," thumbnail image is "+thumbnailImage)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

//                thumbnailImage = offerList[position].userId?.let {
//                    localDatabase.appDao()?.getUserImage(it) }
//
//                userName = offerList[position].userId?.let {
//                    localDatabase.appDao()?.getUserName(it) }
//
//                withContext(Dispatchers.Main){
//                    if (thumbnailImage!=null) {
//                        Glide.with(context)
//                            .load(thumbnailImage)
//                            .into(holder.itemBinding.root.profileImage)
//                    }
//                    holder.itemBinding.root.title.text=userName.toString()
//
////                    Log.d("MyTag"," thumbnail image is "+thumbnailImage)
//                }

            }else{

                thumbnailImage =localDatabase.appDao()?.getUserImage(offerList[position].userId)
                userName =localDatabase.appDao()?.getUserName(offerList[position].userId)
                holder.itemBinding.root.title.text = userName
                withContext(Dispatchers.Main){
                    holder.itemBinding.root.title.text=userName.toString()
                    if (thumbnailImage!=null) {
                        Glide.with(context)
                            .load(thumbnailImage)
                            .into(holder.itemBinding.root.profileImage)
                    }

//                    Log.d("MyTag"," thumbnail image is "+thumbnailImage)
                }
            }

        }


//        if (thumbnailImage!=null) {
//            Glide.with(context)
//                .load(thumbnailImage)
//                .into(holder.itemBinding.root.profileImage)
//        }
        val discountedPrice=offerList[position].discountPrice

        holder.itemBinding.root.price_total.text = offerList[position].discountPrice
        holder.itemBinding.root.profileImage

//        if (offerList[position].userimage.isNotEmpty()) {
//                Glide.with(context)
//                    .load(offerList[position].userimage)
//                    .into(holder.itemBinding.root.profileImage)
//
//        }


//        holder.itemBinding.root.order_quantity_total.text=(total.toInt()/discountedPrice.toInt()).toString()
        holder.itemBinding.root.order_quantity_total.text="1"

        holder.itemBinding.root.itemCounter.setCounterListener(object :CounterListener{
            override fun onIncClick(value: String?) {
                if (value != null) {
//                    " ₹"+
                       total= ((value.toInt() * discountedPrice.toInt()).toString())
                    holder.itemBinding.root.DiscountedPrice.text=" ₹$total "
                    holder.itemBinding.root.price_total.text = total
                    holder.itemBinding.root.order_quantity_total.text=(total.toInt()/discountedPrice.toInt()).toString()
                }
//                holder.itemBinding.root.DiscountedPrice.text = (counterValue.toInt() * (offerList[position].discountPrice).toInt()).toString()
            }


            override fun onDecClick(value: String?) {
                if (value != null) {
                      total=  ((value.toInt() * discountedPrice.toInt()).toString())
                    holder.itemBinding.root.DiscountedPrice.text=" ₹$total "
                    holder.itemBinding.root.price_total.text = total
                    holder.itemBinding.root.order_quantity_total.text=(total.toInt()/discountedPrice.toInt()).toString()
                }
            }
        })
    }



    override fun getItemCount(): Int {
        return offerList.size
    }

    fun showToast(){
        Toasty.success(context,"New Offers",Toasty.LENGTH_LONG,false).show()
    }

    fun updateData(data: List<OfferRoomEntity>){
//        offerList.clear()
//        offerList.addAll(data)
//        notifyDataSetChanged()

        val oldList=offerList
        val diffUtil:DiffUtil.DiffResult=DiffUtil.calculateDiff(
            OfferItemDiffCallback(
                oldList, data
            )
        )
        offerList=data.toMutableList()
        diffUtil.dispatchUpdatesTo(this)
    }


    class OfferItemDiffCallback(
        var oldOfferList :List<OfferRoomEntity>,
        var newOfferList:List<OfferRoomEntity>
    ): DiffUtil.Callback() {
        override fun getOldListSize(): Int {
            return  oldOfferList.size
        }

        override fun getNewListSize(): Int {
            return newOfferList.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {

            return  (oldOfferList[oldItemPosition].postId== newOfferList[newItemPosition].postId)
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {

            return  (oldOfferList[oldItemPosition] == newOfferList[newItemPosition])
        }
    }


    class BindingViewHolder(val itemBinding: ViewDataBinding)
        : RecyclerView.ViewHolder(itemBinding.root){

        val counterValue=itemBinding.root.itemCounter.counterValue
//        Log.d("MyTag", "counter value is : $counterValue")
//        itemBinding.root.DiscountedPrice.text = (counterValue.toInt() * (offerList[position].discountPrice).toInt()).toString()

    }
}
