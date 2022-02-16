package com.sgs.devcamp2.flametalk_android.ui.friend.hidden

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
import com.sgs.devcamp2.flametalk_android.databinding.FragmentHiddenFriendBinding
import com.sgs.devcamp2.flametalk_android.ui.friend.FriendFragmentDirections
import com.sgs.devcamp2.flametalk_android.util.toVisible
import com.sgs.devcamp2.flametalk_android.util.toVisibleGone
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * @author 박소연
 * @created 2022/02/09
 * @updated 2022/02/09
 * @desc 숨김 친구 리스트. 숨김 여부 변경이 가능하다.
 */

@AndroidEntryPoint
@ExperimentalCoroutinesApi
class HiddenFragment : Fragment() {
    private val binding by lazy { FragmentHiddenFriendBinding.inflate(layoutInflater) }
    private val viewModel: HiddenViewModel by viewModels()

    // 뷰 생성 시점에 adapter 초기화
    private val hiddenAdapter: HiddenAdapter by lazy {
        HiddenAdapter(
            requireContext(),
            onClickProfile = {
                val blockedToFriendProfileDirections: NavDirections =
                    FriendFragmentDirections.actionFriendToProfile(
                        HIDDEN_PROFILE,
                        it.preview.profileId
                    )
                findNavController().navigate(blockedToFriendProfileDirections)
            },
            onChangeHidden = {
                viewModel.changeHiddenStatue(it.friendId, it.assignedProfileId)
            }
        )
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
        binding.abHiddenFriend.tvAppbar.text = "숨김 친구 관리"
        binding.abHiddenFriend.imgAppbarBack.toVisible()
        binding.abHiddenFriend.imgAppbarSearch.toVisibleGone()
        binding.abHiddenFriend.imgAppbarAddFriend.toVisibleGone()
        binding.abHiddenFriend.imgAppbarSetting.toVisibleGone()

        binding.abHiddenFriend.imgAppbarBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    // 프로필 초기화
    private fun initUserProfiles() {
        // 숨김 친구 리스트
        initBirthdayProfile()
    }

    // 생일인 친구 리스트 초기화
    private fun initBirthdayProfile() {
        binding.rvHiddenFriend.layoutManager = LinearLayoutManager(requireContext())
        binding.rvHiddenFriend.adapter = hiddenAdapter

        lifecycleScope.launch {
            viewModel.hiddenFriend.collectLatest {
                if (it.isNullOrEmpty()) { // empty view
                    showEmptyView(GONE)
                } else {
                    hiddenAdapter.data = it
                    hiddenAdapter.submitList(it)
                    showEmptyView(VISIBLE)
                }
            }
        }
    }

    private fun showEmptyView(type: Int) {
        binding.rvHiddenFriend.visibility = type
        binding.tvBlockedFriendEmpty.visibility = 8 - type
    }

    companion object {
        const val HIDDEN_PROFILE = 5

        const val GONE = 8
        const val VISIBLE = 0
    }
}
