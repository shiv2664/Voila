package com.android.testproject1.utils

import android.view.ViewGroup

import com.android.testproject1.adapter.DrawerAdapter


abstract class DrawerItem <T : DrawerAdapter.ViewHolder?> {
    var isChecked = false
        protected set

   public abstract fun createViewHolder(parent: ViewGroup?): T
   public abstract fun bindViewHolder(holder: T)

   public fun setChecked(isChecked: Boolean): DrawerItem<T> {
        this.isChecked = isChecked
        return this
    }

   public val isSelectable: Boolean
        get() = true
}