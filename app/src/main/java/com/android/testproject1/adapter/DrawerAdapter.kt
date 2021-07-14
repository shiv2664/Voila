package com.android.testproject1.adapter

import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.testproject1.utils.DrawerItem

//
class DrawerAdapter(items: List<DrawerItem<*>>) :
    RecyclerView.Adapter<DrawerAdapter.ViewHolder>() {
    private var items: List<DrawerItem<*>> = items
    private val viewTypes: MutableMap<Class<out DrawerItem<*>?>, Int>
    private val holderFactories: SparseArray<DrawerItem<*>>
    private var listener: OnItemSelectedListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val holder: ViewHolder? = holderFactories[viewType].createViewHolder(parent)
        if (holder != null) {
            holder.adapter = this
        }
        return holder!!
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        items[position].bindViewHolder(holder as Nothing)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun getItemViewType(position: Int): Int {
        return viewTypes[items[position].javaClass]!!
    }

    private fun processViewTypes() {
        var type = 0
        for (item in items) {
            if (!viewTypes.containsKey(item.javaClass)) {
                viewTypes[item.javaClass] = type
                holderFactories.put(type, item)
                type++
            }
        }
    }

    fun setSelected(position: Int) {
        val newChecked: DrawerItem<*> = items[position]
        if (!newChecked.isSelectable) {
            return
        }
        for (i in items.indices) {
            val item: DrawerItem<*> = items[i]
            if (item.isChecked) {
                item.setChecked(false)
                notifyItemChanged(i)
                break
            }
        }
        newChecked.setChecked(true)
        notifyItemChanged(position)
        if (listener != null) {
            listener!!.onItemSelected(position)
        }
    }

    fun setListener(listener: OnItemSelectedListener?) {
        this.listener = listener
    }

    abstract class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        var adapter: DrawerAdapter? = null
        override fun onClick(v: View?) {
            adapter!!.setSelected(adapterPosition)
        }

        init {
            itemView.setOnClickListener(this)
        }
    }

    interface OnItemSelectedListener {
        fun onItemSelected(position: Int)
    }

    init {
        viewTypes = HashMap()
        holderFactories = SparseArray<DrawerItem<*>>()
        processViewTypes()
    }
}