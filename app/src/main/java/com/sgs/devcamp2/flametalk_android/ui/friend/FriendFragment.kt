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
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * @author 박소연
 * @created 2022/01/17
 * @updated 2022/01/31
 * @desc 프로필 상세 보기 (유저, 친구)
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
    ): View {
        initUI()
        return binding.root
    }

    private fun initUI() {
        initAppbar()
        initUserProfiles()
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
        binding.tvFriendLoadContact.setOnClickListener {
            // TODO: 연락처 동기화 -> 서버로 요청 -> Friend 페이지로 다시 랜딩
        }
    }

    // 프로필 초기화
    private fun initUserProfiles() {
        initMainProfile()
        initMultiProfile()

        // 생일, 친구 프로필
        initBirthdayProfile()
        initFriendProfile()
    }

    // 유저프로필 초기화
    // 유저 닉네임은 list 데이터로 넘어오지 않기 때문에 따로 observe
    private fun initMainProfile() {
        // preference에서 가져온 사용자 닉네임
        lifecycleScope.launch {
            viewModel.nickname.collectLatest {
                if (it.isNotEmpty()) {
                    multiProfileAdapter.nickname = it
                    binding.lFriendMainUser.tvFriendPreviewNickname.text = it
                }
            }
        }

        // 사용자 프로필
        lifecycleScope.launchWhenResumed {
            viewModel.userProfile.collectLatest {
                Glide.with(binding.lFriendMainUser.imgFriendPreview)
                    .load(it?.imageUrl).apply(RequestOptions.circleCropTransform())
                    .apply(RequestOptions.placeholderOf(R.drawable.ic_person_white_24))
                    .into(binding.lFriendMainUser.imgFriendPreview)

                if (it?.description.isNullOrEmpty()) {
                    binding.lFriendMainUser.tvFriendPreviewDesc.toVisibleGone()
                } else {
                    binding.lFriendMainUser.tvFriendPreviewDesc.toVisible()
                    binding.lFriendMainUser.tvFriendPreviewDesc.text = it?.description
                    Timber.d("description ${it?.description}")
                }
            }
        }

        // 내 프로필 미리보기 > 프로필 상세 보기 이동
        binding.lFriendMainUser.root.setOnClickListener {
            val friendToProfileDirections: NavDirections =
                FriendFragmentDirections.actionFriendToProfile(
                    viewType = USER_DEFAULT_PROFILE, profileId = viewModel.userProfile.value!!.id
                )
            findNavController().navigate(friendToProfileDirections)
        }
    }

    // 멀티프로필 리스트 초기화
    private fun initMultiProfile() {
        binding.rvFriendMultiProfile.layoutManager = LinearLayoutManager(
            requireContext(), RecyclerView.HORIZONTAL, false
        )
        binding.rvFriendMultiProfile.adapter = multiProfileAdapter

        lifecycleScope.launchWhenResumed {
            viewModel.multiProfile.collectLatest {
                if (it.isNotEmpty()) {
                    multiProfileAdapter.data = it
                    multiProfileAdapter.notifyDataSetChanged()
                }
            }
        }
        // 마지막 아이템: 만들기
        binding.itemFriendAddProfile.imgVerticalProfileNone.toVisible()
        binding.itemFriendAddProfile.tvVerticalProfileNickname.text = "만들기"

        // 친구리스트 > 멀티프로필 생성: 멀티 프로필 만들기
        binding.itemFriendAddProfile.root.setOnClickListener {
            findNavController().navigate(R.id.navigation_add_profile)
        }
    }

    // 생일인 친구 리스트 초기화
    private fun initBirthdayProfile() {
        binding.rvFriendBirthday.layoutManager = LinearLayoutManager(requireContext())
        binding.rvFriendBirthday.adapter = birthdayAdapter

        lifecycleScope.launch {
            viewModel.birthProfile.collectLatest {
                if (it.isNullOrEmpty()) {
                    birthdayVisibility(GONE)
                } else {
                    birthdayAdapter.data = it
                    birthdayAdapter.notifyDataSetChanged()
                    birthdayVisibility(VISIBLE)
                }
            }
        }
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
        lifecycleScope.launch {
            viewModel.friendProfile.collectLatest {
                if (it.isNullOrEmpty()) {
                    friendVisibility(GONE)
                } else {
                    friendAdapter.data = it
                    friendAdapter.notifyDataSetChanged()
                    friendVisibility(VISIBLE)
                }
            }
        }
    }

    // 생일 친구 리스트 노출 여부
    private fun birthdayVisibility(visibleType: Int) {
        binding.rvFriendBirthday.visibility = visibleType
        binding.itemFriendMoreBirthday.root.visibility = visibleType
        binding.tvFriendBirthday.visibility = visibleType
    }

    // 친구 리스트 노출 여부
    private fun friendVisibility(visibleType: Int) {
        binding.rvFriend.visibility = visibleType
        binding.tvFriendCount.visibility = visibleType

        when (visibleType) {
            GONE -> binding.tvFriendLoadContact.toVisible()
            VISIBLE -> binding.tvFriendLoadContact.toVisibleGone()
        }
    }

    companion object {
        const val USER_DEFAULT_PROFILE = 1
        const val BIRTHDAY_FRIEND = 100
        const val FRIEND = 200

        const val GONE = 8
        const val VISIBLE = 0
    }
}
