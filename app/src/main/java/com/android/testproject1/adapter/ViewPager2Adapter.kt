package com.android.testproject1.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.android.testproject1.fragments.ViewPagerFragmentSaved
import com.android.testproject1.fragments.ViewpagerFragmentPosts


class ViewPager2Adapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun createFragment(position: Int): Fragment {

        when (position) {
            0 -> { return ViewpagerFragmentPosts() }
//            1 -> {return ViewPagerFragmentSaved() }
        }

        return ViewpagerFragmentPosts()

    }

    override fun getItemCount(): Int {
        return 1
    }
}