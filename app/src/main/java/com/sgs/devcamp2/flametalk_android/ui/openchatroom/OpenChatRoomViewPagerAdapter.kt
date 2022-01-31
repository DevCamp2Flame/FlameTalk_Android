package com.sgs.devcamp2.flametalk_android.ui.openchatroom

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 * @author boris
 * @created 2022/01/26
 */
class OpenChatRoomViewPagerAdapter(fa: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fa, lifecycle) {
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
