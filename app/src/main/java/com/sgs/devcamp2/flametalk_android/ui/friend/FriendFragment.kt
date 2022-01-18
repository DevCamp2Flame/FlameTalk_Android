package com.sgs.devcamp2.flametalk_android.ui.friend

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.sgs.devcamp2.flametalk_android.R
import com.sgs.devcamp2.flametalk_android.databinding.FragmentFriendBinding
import com.sgs.devcamp2.flametalk_android.ui.friend.birthday.BirthdayAdapter
import com.sgs.devcamp2.flametalk_android.ui.friend.friends.FriendAdapter
import com.sgs.devcamp2.flametalk_android.ui.friend.multi_profile.MultiProfileAdapter
import com.sgs.devcamp2.flametalk_android.util.toVisible
import com.sgs.devcamp2.flametalk_android.util.toVisibleGone
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest

/**
 * @author 박소연
 * @created 2022/01/17
 * @desc 프로필 상세 보기 (유저, 친)
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
        initAppbar()
        initUserProfile()
        initMultiProfile()
        initBirthdayProfile()
        initFriendProfile()
    }

    private fun initAppbar() {
        binding.abFriend.tvAppbar.text = "친구"
        binding.abFriend.imgAppbarSearch.setOnClickListener {
            // TODO: Friend > Search
        }
        binding.abFriend.imgAppbarAddFriend.setOnClickListener {
            // TODO: 친구 추가 top sheet
        }
        binding.abFriend.imgAppbarSetting.setOnClickListener {
            // TODO: Friend > Setting
        }
    }

    // 유저프로필 초기화
    private fun initUserProfile() {
        lifecycleScope.launchWhenStarted {
            viewModel.userProfile.collectLatest {
                Glide.with(binding.lFriendMainUser.imgFriendPreview)
                    .load(it.image).apply(RequestOptions.circleCropTransform())
                    .apply(RequestOptions.placeholderOf(R.drawable.ic_person_white_24))
                    .into(binding.lFriendMainUser.imgFriendPreview)
                binding.lFriendMainUser.tvFriendPreviewNickname.text = it.nickname
                if (it.description.isNullOrBlank()) {
                    binding.lFriendMainUser.tvFriendPreviewDesc.toVisibleGone()
                } else {
                    binding.lFriendMainUser.tvFriendPreviewDesc.text = it.description
                }
            }
        }

        // 친구 목록 > 프로필 상세 보기 이동
        binding.lFriendMainUser.root.setOnClickListener {
            val friendToProfileDirections: NavDirections =
                FriendFragmentDirections.actionFriendToProfile(1, viewModel.userProfile.value)
            findNavController().navigate(friendToProfileDirections)
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
        binding.itemFriendAddProfile.imgVerticalProfileNone.toVisible()
        binding.itemFriendAddProfile.tvVerticalProfileNickname.text = "만들기"
    }

    // 생일인 친구 리스트 초기화
    private fun initBirthdayProfile() {
        binding.rvFriendBirthday.layoutManager = LinearLayoutManager(requireContext())
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
        binding.itemFriendMoreBirthday.imgFriendPreviewNone.toVisible()
        binding.itemFriendMoreBirthday.tvFriendPreviewCount.toVisible()
        binding.itemFriendMoreBirthday.tvFriendPreviewDesc.toVisibleGone()
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
