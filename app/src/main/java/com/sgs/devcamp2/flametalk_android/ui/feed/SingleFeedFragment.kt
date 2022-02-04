package com.sgs.devcamp2.flametalk_android.ui.feed

import android.os.Bundle
import android.view.*
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.sgs.devcamp2.flametalk_android.R
import com.sgs.devcamp2.flametalk_android.data.dummy.getDummyHorizentalFeed
import com.sgs.devcamp2.flametalk_android.databinding.FragmentSingleFeedBinding
import com.sgs.devcamp2.flametalk_android.util.toInvisible
import com.sgs.devcamp2.flametalk_android.util.toVisible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

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

        // ViewPager 초기화
        binding.vpSingleFeed.offscreenPageLimit = 1
        binding.vpSingleFeed.adapter =
            SingleFeedAdapter(requireContext(), getDummyHorizentalFeed().data.feeds)

        // 이미지를 슬라이드하며 index, 공개 여부를 변경
        binding.vpSingleFeed.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.tvSingleFeedIndex.text =
                    "${position + 1}/${getDummyHorizentalFeed().data.feeds.size}"
                if (getDummyHorizentalFeed().data.feeds[position].isLock) {
                    binding.tvSingleFeedPrivate.toVisible()
                } else {
                    binding.tvSingleFeedPrivate.toInvisible()
                }
            }
        })

        binding.imgSingleFeedDownload.setOnClickListener {
            var popupMenu = PopupMenu(requireContext(), requireView())
            popupMenu.inflate(R.menu.feed_menu)
            popupMenu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.menu_download -> {
                        // TODO: 이미지 다운로드 통신 진행
                        Snackbar.make(requireView(), "다운로드를 시작합니다...", Snackbar.LENGTH_SHORT).show()
                    }
                    R.id.menu_delete -> {
                        Snackbar.make(requireView(), "피드를 삭제했습니다", Snackbar.LENGTH_SHORT).show()
                    }
                    else -> {
                        Snackbar.make(requireView(), "else...", Snackbar.LENGTH_SHORT).show()
                    }
                }
                return@setOnMenuItemClickListener false
            }
            popupMenu.show()
        }

        Glide.with(binding.imgSingleFeedToTotal)
            .load(getDummyHorizentalFeed().data.profileImage).into(binding.imgSingleFeedToTotal)
    }

    private fun initEventListener() {
    }
}
