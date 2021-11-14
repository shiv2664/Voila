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

import com.android.testproject1.MainActivity

import com.shalan.mohamed.itemcounterview.CounterListener

class SearchAdapter2(private val context: Context,
                     private var offerList:MutableList<Offer>): RecyclerView.
                     Adapter<SearchAdapter2.BindingViewHolder>() {

    private var counterValue: String = "1"
    private var total="0"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindingViewHolder {

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

        val discountedPrice=offerList[position].discountPrice

        holder.itemBinding.root.price_total.text = offerList[position].discountPrice

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

    fun updateData(data: List<Offer>){
        offerList.clear()
        offerList.addAll(data)
        notifyDataSetChanged()
    }


    class BindingViewHolder(val itemBinding: ViewDataBinding)
        : RecyclerView.ViewHolder(itemBinding.root){

        val counterValue=itemBinding.root.itemCounter.counterValue
//        Log.d("MyTag", "counter value is : $counterValue")
//        itemBinding.root.DiscountedPrice.text = (counterValue.toInt() * (offerList[position].discountPrice).toInt()).toString()

    }


}
