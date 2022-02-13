package com.sgs.devcamp2.flametalk_android.ui.chattingviewpager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * @author 김현국
 * @created 2022/01/26
 */
class ChattingViewPagerAdapter(fa: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fa, lifecycle) {

    private val fragmentList: MutableList<Fragment> = ArrayList()
    private val titleList: MutableList<String> = ArrayList()

    override fun getItemCount(): Int {
        return fragmentList.size
    }
    override fun createFragment(position: Int): Fragment {
        return fragmentList[position]
    }
    fun getPageTitle(position: Int): CharSequence? {
        return titleList.get(position)
    }
    fun addFragment(fragment: Fragment, title: String) {
        fragmentList.add(fragment)
        titleList.add(title)
    }
}
