package com.sgs.devcamp2.flametalk_android.ui.chattingviewpager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.sgs.devcamp2.flametalk_android.R
import com.sgs.devcamp2.flametalk_android.databinding.FragmentChattingViewPagerBinding
import com.sgs.devcamp2.flametalk_android.ui.chatroomlist.ChatRoomListFragment
import com.sgs.devcamp2.flametalk_android.ui.openchatroom.OpenChatRoomFragment
import dagger.hilt.android.AndroidEntryPoint
/**
 * @author 김현국
 * @created 2022/02/05
 */
@AndroidEntryPoint
class ChattingViewPagerFragment : Fragment(), View.OnClickListener {
    lateinit var binding: FragmentChattingViewPagerBinding
    lateinit var viewPager: ViewPager2
    lateinit var tabLayout: TabLayout
    lateinit var viewpagerAdapter: FragmentStateAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChattingViewPagerBinding.inflate(inflater, container, false)
        initUI()

        return binding.root
    }

    fun initUI() {
        viewPager = binding.vpChatRoom
        viewPager.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        tabLayout = binding.layoutChatViewPagerTab
        viewpagerAdapter = ChattingViewPagerAdapter(childFragmentManager, lifecycle)
        (viewpagerAdapter as ChattingViewPagerAdapter).addFragment(ChatRoomListFragment(), "채팅")
        (viewpagerAdapter as ChattingViewPagerAdapter).addFragment(OpenChatRoomFragment(), "오픈 채팅")
        viewPager.adapter = viewpagerAdapter
        binding.ivChatViewPagerChat.setOnClickListener(this)

        TabLayoutMediator(tabLayout, viewPager) {
            tab, position ->
            when (position) {
                0 ->
                    {
                        tab.text = (viewpagerAdapter as ChattingViewPagerAdapter).getPageTitle(0)
                    }
                1 ->
                    {
                        tab.text = (viewpagerAdapter as ChattingViewPagerAdapter).getPageTitle(1)
                    }
            }
        }.attach()
    }
    override fun onClick(view: View?) {
        when (view) {
            binding.ivChatViewPagerChat ->
                {
                    // ChatRoomTopSheetFragment().show(childFragmentManager, "topsheet")
                    findNavController().navigate(R.id.action_navigation_chatting_ViewPager_Fragment_to_navigation_chat_Room_Top_Sheet)
                }
        }
    }
}
