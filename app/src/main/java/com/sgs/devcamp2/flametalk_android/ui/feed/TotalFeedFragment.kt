package com.sgs.devcamp2.flametalk_android.ui.feed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.sgs.devcamp2.flametalk_android.databinding.FragmentTotalFeedBinding
import com.sgs.devcamp2.flametalk_android.util.toVisible
import com.sgs.devcamp2.flametalk_android.util.toVisibleGone
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest

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
    private val args: TotalFeedFragmentArgs by navArgs()

    // TODO: 뷰 연결 후 삭제할 변수
    val profileId = 9L

    // 뷰 생성 시점에 adapter 초기화
    private val totalFeedAdapter: TotalFeedAdapter by lazy {
        TotalFeedAdapter(
            requireContext(),
            args.profileImage,
            onClickDownloadItem = {
                // viewModel.downloadItem(it.feedId)
            },
            onClickDeleteItem = {
                viewModel.deleteFeed(it.feedId)
            },
            onClickChangeLockItem = {
                viewModel.updateFeedImageLock(it.feedId)
            }
        )
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

        // 뒤로가기 버튼을 누르면 이전의 single Feed의 UI stack을 pop하고 프로필 상세보기로 랜딩한다.
        binding.abTotalFeed.imgAppbarBack.setOnClickListener {
            // findNavController().navigateUp()
            // TODO: Feed Total -> Profile
        }
    }

    // 프로필+배경 피드 데이터 초기화
    private fun initFeed() {
        // TODO change: viewModel.getTotalFeedList(args.args.profileId)
        viewModel.getTotalFeedList(profileId)
        binding.rvTotalFeed.layoutManager = LinearLayoutManager(requireContext())
        binding.rvTotalFeed.adapter = totalFeedAdapter

        viewModel.totalFeed.observe(
            viewLifecycleOwner
        ) { it ->
            it?.let {
                totalFeedAdapter.data = it
                totalFeedAdapter.submitList(it)

                if (it.isEmpty()) {
                    binding.tvSingleFeedEmpty.toVisible()
                }
            }
        }

        // 에러메세지
        lifecycleScope.launchWhenStarted {
            viewModel.error?.collectLatest {
                if (it != null)
                    Snackbar.make(requireView(), "알 수 없는 에러 발생", Snackbar.LENGTH_SHORT).show()
            }
        }
        // 성공메세지
        lifecycleScope.launchWhenResumed {
            viewModel.message?.collectLatest {
                if (it != null)
                    Snackbar.make(requireView(), it, Snackbar.LENGTH_SHORT).show()
            }
        }

        // 리스트 재요청
        lifecycleScope.launchWhenResumed {
            viewModel.reload.collectLatest {
                if (it) { // TODO change: viewModel.getTotalFeedList(args.args.profileId)
                    viewModel.getTotalFeedList(profileId)
                }
            }
        }
    }
}
