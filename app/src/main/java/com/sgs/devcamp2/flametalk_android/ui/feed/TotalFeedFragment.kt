package com.sgs.devcamp2.flametalk_android.ui.feed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.sgs.devcamp2.flametalk_android.databinding.FragmentTotalFeedBinding
import com.sgs.devcamp2.flametalk_android.util.toVisible
import com.sgs.devcamp2.flametalk_android.util.toVisibleGone
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * @author 박소연
 * @created 2022/02/03
 * @updated 2022/02/04
 * @desc 프로필과 배경화면의 변경 이력을 보여주는 수직 스크롤 형태의 피드
 */

@AndroidEntryPoint
@ExperimentalCoroutinesApi
class TotalFeedFragment : Fragment() {
    private val binding by lazy { FragmentTotalFeedBinding.inflate(layoutInflater) }
    private val viewModel: TotalFeedViewModel by viewModels()

    // 뷰 생성 시점에 adapter 초기화
    private val totalFeedAdapter: TotalFeedAdapter by lazy {
        TotalFeedAdapter(requireContext(), viewModel.profileImage.value)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initUI()
        return binding.root
    }

    private fun initUI() {
        initAppbar()
        initFeed()
    }

    private fun initAppbar() {
        binding.abTotalFeed.tvAppbar.text = "홈"
        binding.abTotalFeed.imgAppbarBack.toVisible()
        binding.abTotalFeed.imgAppbarSetting.toVisibleGone()
        binding.abTotalFeed.imgAppbarAddChat.toVisibleGone()
        binding.abTotalFeed.imgAppbarAddFriend.toVisibleGone()
        binding.abTotalFeed.imgAppbarSearch.toVisibleGone()
    }

    // 멀티프로필 리스트 초기화
    private fun initFeed() {
        binding.rvTotalFeed.layoutManager = LinearLayoutManager(requireContext())
        binding.rvTotalFeed.adapter = totalFeedAdapter

        viewModel.totalFeed.observe(
            viewLifecycleOwner
        ) { it ->
            it?.let {
                if (it.isNotEmpty()) {
                    totalFeedAdapter.data = it
                    totalFeedAdapter.notifyDataSetChanged()
                }
            }
        }
    }
}
