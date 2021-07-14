package com.android.testproject1.utils

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.android.testproject1.R
import com.android.testproject1.adapter.DrawerAdapter

class SimpleItem(private val icon: Drawable?, private val title: String) :
    DrawerItem<SimpleItem.ViewHolder?>() {
    private var selectedItemIconTint = 0
    private var selectedItemTextTint = 0
    private var normalItemIconTint = 0
    private var normalItemTextTint = 0

    override
    fun createViewHolder(parent: ViewGroup?): ViewHolder? {
        val inflater = LayoutInflater.from(parent?.context)
        val v: View = inflater.inflate(R.layout.item_option, parent, false)
        return ViewHolder(v)
    }

    override
    fun bindViewHolder(holder: ViewHolder?) {
        holder?.title?.text = title
        holder?.icon?.setImageDrawable(icon)
        holder?.title?.setTextColor(if (isChecked) selectedItemTextTint else normalItemTextTint)
        holder?.icon?.setColorFilter(if (isChecked) selectedItemIconTint else normalItemIconTint)
    }

    fun withSelectedIconTint(selectedItemIconTint: Int): SimpleItem {
        this.selectedItemIconTint = selectedItemIconTint
        return this
    }

    fun withSelectedTextTint(selectedItemTextTint: Int): SimpleItem {
        this.selectedItemTextTint = selectedItemTextTint
        return this
    }

    fun withIconTint(normalItemIconTint: Int): SimpleItem {
        this.normalItemIconTint = normalItemIconTint
        return this
    }

    fun withTextTint(normalItemTextTint: Int): SimpleItem {
        this.normalItemTextTint = normalItemTextTint
        return this
    }

    class ViewHolder(itemView: View) : DrawerAdapter.ViewHolder(itemView) {
        val icon: ImageView
        val title: TextView

        init {
            icon = itemView.findViewById<View>(R.id.icon) as ImageView
            title = itemView.findViewById<View>(R.id.title) as TextView
        }
    }

//    @JvmName("createViewHolder1")
//    override fun createViewHolder(parent: ViewGroup?): ViewHolder? {
//        TODO("Not yet implemented")
//    }
//
//    @JvmName("bindViewHolder1")
//    override fun bindViewHolder(holder: ViewHolder?) {
//        TODO("Not yet implemented")
//    }
}