package com.sgs.devcamp2.flametalk_android.ui.feed

import android.os.Bundle
import android.view.*
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.sgs.devcamp2.flametalk_android.R
import com.sgs.devcamp2.flametalk_android.databinding.FragmentSingleFeedBinding
import com.sgs.devcamp2.flametalk_android.util.toInvisible
import com.sgs.devcamp2.flametalk_android.util.toVisible
import com.sgs.devcamp2.flametalk_android.util.toVisibleGone
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest

/**
 * @author 박소연
 * @created 2022/02/03
 * @desc 프로필 or 배경사진의 변경 이력을 수평 스와이프하여 볼 수 있다.
 */

@AndroidEntryPoint
@ExperimentalCoroutinesApi
class SingleFeedFragment : Fragment() {
    private val binding by lazy { FragmentSingleFeedBinding.inflate(layoutInflater) }
    private val viewModel by viewModels<SingleFeedViewModel>()
    private val args: SingleFeedFragmentArgs by navArgs()

    // TODO: 뷰 연결 후 삭제할 변수
    val profileId = 4L
    val isBackground = false // args.isBackground

    private val singleFeedAdapter: SingleFeedAdapter by lazy {
        SingleFeedAdapter(requireContext())
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
        initEventListener()

        // 서버로부터 피드 리스트 데이터 요청
        viewModel.getSingleFeedList(
            profileId,
            isBackground
        ) // TODO change: viewModel.getTotalFeedList(args.args.profileId)

        viewModel.feeds.observe(
            viewLifecycleOwner
        ) { it ->
            it?.let {
                singleFeedAdapter.data = it
                singleFeedAdapter.notifyDataSetChanged()

                if (it.isEmpty()) {
                    binding.tvSingleFeedEmpty.toVisible()
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.profileImage?.collectLatest {
                if (it != "")
                    Glide.with(binding.imgSingleFeedToTotal)
                        .load(it).into(binding.imgSingleFeedToTotal)
            }
        }

        // TODO: response로 Feed Data가 오면 확인
        lifecycleScope.launchWhenStarted {
            viewModel.lockChanged?.collectLatest {
                when (it) {
                    true -> binding.tvSingleFeedPrivate.toVisibleGone()
                    false -> binding.tvSingleFeedPrivate.toVisible()
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

        // 리스트 재요청
        lifecycleScope.launchWhenResumed {
            viewModel.reload.collectLatest {
                if (it) { // TODO change: viewModel.getTotalFeedList(args.args.profileId)
                    viewModel.getSingleFeedList(profileId, isBackground)
                }
            }
        }

        // ViewPager 초기화
        binding.vpSingleFeed.offscreenPageLimit = 1
        binding.vpSingleFeed.adapter = singleFeedAdapter
    }

    private fun initEventListener() {
        // 이미지를 슬라이드하며 index, 공개 여부를 변경
        binding.vpSingleFeed.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                // 현재 보고있는 피드 position을 ViewModel에 알려준다
                viewModel.currentImagePosition(position)

                binding.tvSingleFeedIndex.text =
                    "${position + 1}/${viewModel.feeds.value!!.size}"

                var feeds = viewModel.feeds.value
                if (feeds?.get(position)!!.isLock) {
                    binding.tvSingleFeedPrivate.toVisible()
                } else {
                    binding.tvSingleFeedPrivate.toInvisible()
                }
            }
        })

        // 옵션 메뉴: 다운로드, 피드 삭제
        binding.imgSingleFeedMenu.setOnClickListener {
            var popupMenu = PopupMenu(this.activity, binding.vpSingleFeed)
            popupMenu.inflate(R.menu.feed_menu)
            popupMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.menu_download -> {
                        viewModel.downloadItem()
                    }
                    R.id.menu_delete -> {
                        viewModel.deleteFeed()
                        // Snackbar.make(requireView(), "피드를 삭제했습니다", Snackbar.LENGTH_SHORT).show()
                    }
                    else -> {
                        viewModel.updateFeedImageLock()
                        // Snackbar.make(requireView(), "else...", Snackbar.LENGTH_SHORT).show()
                    }
                }
                return@setOnMenuItemClickListener false
            }
            popupMenu.show()
        }

        // 프로필+배경 피드로 이동
        binding.imgSingleFeedToTotal.setOnClickListener {
            findNavController().navigate(
                SingleFeedFragmentDirections.actionFeedSingleToTotal(
                    profileId,
                    viewModel.profileImage.value
                )
            )
        }
    }
}
