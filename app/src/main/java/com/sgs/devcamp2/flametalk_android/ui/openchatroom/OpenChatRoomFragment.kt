package com.sgs.devcamp2.flametalk_android.ui.openchatroom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.sgs.devcamp2.flametalk_android.databinding.FragmentOpenChatRoomBinding
import com.sgs.devcamp2.flametalk_android.ui.openchatroom.myopenchatroom.MyOpenChatRoomFragment
import com.sgs.devcamp2.flametalk_android.ui.openchatroom.myopenprofile.MyOpenChatProfileFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OpenChatRoomFragment : Fragment() {
    lateinit var binding: FragmentOpenChatRoomBinding
    lateinit var viewPager: ViewPager2
    lateinit var tabLayout: TabLayout
    lateinit var viewpagerAdapter: FragmentStateAdapter
    lateinit var openChatroomAdapter: OpenChatRoomAdapter

    private val viewModel: OpenChatRoomViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOpenChatRoomBinding.inflate(inflater, container, false)

        initUI()

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.openChatRoom.observe(
                viewLifecycleOwner
            ) {
                openChatroomAdapter.data = it
            }
        }
        return binding.root
    }

    fun initUI() {
        viewPager = binding.vpOpenChatRoomInner
        tabLayout = binding.tblOpenChatRoom
        binding.rvOpenChatRoom.layoutManager = LinearLayoutManager(context)

        openChatroomAdapter = OpenChatRoomAdapter(this.requireContext())

        viewpagerAdapter = OpenChatRoomViewPagerAdapter(childFragmentManager, lifecycle)
        (viewpagerAdapter as OpenChatRoomViewPagerAdapter).addFragment(MyOpenChatRoomFragment(), "오픈채팅")
        (viewpagerAdapter as OpenChatRoomViewPagerAdapter).addFragment(MyOpenChatProfileFragment(), "오픈프로필")
        viewPager.adapter = viewpagerAdapter
        TabLayoutMediator(tabLayout, viewPager) {
            tab, position ->
            when (position) {
                0 ->
                    {
                        tab.text = (viewpagerAdapter as OpenChatRoomViewPagerAdapter).getPageTitle(0)
                    }
                1 ->
                    {
                        tab.text = (viewpagerAdapter as OpenChatRoomViewPagerAdapter).getPageTitle(1)
                    }
            }
        }.attach()

        binding.rvOpenChatRoom.adapter = openChatroomAdapter
    }
}
