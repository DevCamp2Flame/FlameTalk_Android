package com.sgs.devcamp2.flametalk_android.ui.friend

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.sgs.devcamp2.flametalk_android.databinding.FragmentFriendBinding
import com.sgs.devcamp2.flametalk_android.ui.friend.birthday.BirthdayAdapter
import com.sgs.devcamp2.flametalk_android.ui.friend.friends.FriendAdapter
import com.sgs.devcamp2.flametalk_android.ui.friend.multi_profile.MultiProfileAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest

/**
 * @author 박소연
 * @created 2022/01/14
 * @desc 앱 실행 시 기본 화면. 1번째 탭의 친구 목록 페이지
 */

@AndroidEntryPoint
@ExperimentalCoroutinesApi
class FriendFragment : Fragment() {
    private val binding by lazy { FragmentFriendBinding.inflate(layoutInflater) }
    private val viewModel: FriendViewModel by viewModels()
    // 뷰 생성 시점에 adapter 초기화
    private val multiProfileAdapter: MultiProfileAdapter by lazy {
        MultiProfileAdapter(requireContext())
    }
    private val birthdayAdapter: BirthdayAdapter by lazy {
        BirthdayAdapter(requireContext())
    }
    private val friendAdapter: FriendAdapter by lazy {
        FriendAdapter(requireContext())
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
        initUserProfile()
        initMultiProfile()
        initBirthdayProfile()
        initFriendProfile()
    }

    // 유저프로필 초기화
    private fun initUserProfile() {
        lifecycleScope.launchWhenStarted {
            viewModel.userProfile.collectLatest {
                Glide.with(binding.lFriendMainUser.imgFriendPreview)
                    .load(it.image).apply(RequestOptions.circleCropTransform())
                    .into(binding.lFriendMainUser.imgFriendPreview)
                binding.lFriendMainUser.tvFriendPreviewNickname.text = it.nickname
                if (it.description.isNullOrBlank()) {
                    binding.lFriendMainUser.tvFriendPreviewDesc.visibility = View.GONE
                } else {
                    binding.lFriendMainUser.tvFriendPreviewDesc.text = it.description
                }
            }
        }
    }

    // 멀티프로필 리스트 초기화
    private fun initMultiProfile() {
        binding.rvFriendMultiProfile.layoutManager = LinearLayoutManager(
            requireContext(), RecyclerView.HORIZONTAL, false
        )
        binding.rvFriendMultiProfile.adapter = multiProfileAdapter

        viewModel.multiProfile.observe(
            viewLifecycleOwner,
            { it ->
                it?.let {
                    if (it.size > 0) {
                        multiProfileAdapter.data = it
                        multiProfileAdapter.notifyDataSetChanged()
                    }
                }
            }
        )
        // 마지막 아이템: 만들기
        binding.itemFriendAddProfile.imgVerticalProfileNone.visibility = View.VISIBLE
        binding.itemFriendAddProfile.tvVerticalProfileNickname.text = "만들기"
    }

    // 생일인 친구 리스트 초기화
    private fun initBirthdayProfile() {
        binding.rvFriendBirthday.layoutManager = LinearLayoutManager(
            requireContext(), RecyclerView.HORIZONTAL, false
        )
        binding.rvFriendBirthday.adapter = birthdayAdapter

        viewModel.birthProfile.observe(
            viewLifecycleOwner,
            { it ->
                it?.let {
                    if (it.size > 0) {
                        birthdayAdapter.data = it
                        birthdayAdapter.notifyDataSetChanged()
                    }
                }
            }
        )
        // 마지막 아이템: 친구의 생일을 확인해보세요
        binding.itemFriendMoreBirthday.imgFriendPreviewNone.visibility = View.VISIBLE
        binding.itemFriendMoreBirthday.tvFriendPreviewCount.visibility = View.VISIBLE
        binding.itemFriendMoreBirthday.tvFriendPreviewCount.text = "n"
    }

    // 주소록 친구 리스트 초기화
    private fun initFriendProfile() {
        binding.rvFriend.layoutManager = LinearLayoutManager(requireContext())
        binding.rvFriend.adapter = friendAdapter

        viewModel.friendProfile.observe(
            viewLifecycleOwner,
            { it ->
                it?.let {
                    if (it.size > 0) {
                        friendAdapter.data = it
                        friendAdapter.notifyDataSetChanged()
                    }
                }
            }
        )
    }
}
