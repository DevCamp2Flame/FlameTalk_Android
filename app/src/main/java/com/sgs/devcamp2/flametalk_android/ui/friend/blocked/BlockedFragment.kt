package com.sgs.devcamp2.flametalk_android.ui.friend.blocked

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.sgs.devcamp2.flametalk_android.databinding.FragmentBlockedFriendBinding
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
 * @desc 차단 친구 리스트. 숨김 여부 변경이 가능하다.
 */

@AndroidEntryPoint
@ExperimentalCoroutinesApi
class BlockedFragment : Fragment() {
    private val binding by lazy { FragmentBlockedFriendBinding.inflate(layoutInflater) }
    private val viewModel: BlockedViewModel by viewModels()

    // 뷰 생성 시점에 adapter 초기화
    private val blockedAdapter: BlockedAdapter by lazy {
        BlockedAdapter(
            requireContext(),
            onClickProfile = {
                val blockedToFriendProfileDirections: NavDirections =
                    FriendFragmentDirections.actionFriendToProfile(
                        BLOCKED_PROFILE,
                        it.preview.profileId
                    )
                findNavController().navigate(blockedToFriendProfileDirections)
            },
            onChangeHidden = {
                viewModel.changeBlockStatue(it.friendId)
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
        binding.abBlockedFriend.tvAppbar.text = "차단 친구 관리"
        binding.abBlockedFriend.imgAppbarBack.toVisible()
        binding.abBlockedFriend.imgAppbarSearch.toVisibleGone()
        binding.abBlockedFriend.imgAppbarAddFriend.toVisibleGone()
        binding.abBlockedFriend.imgAppbarSetting.toVisibleGone()
    }

    // 프로필 초기화
    private fun initUserProfiles() {
        initBirthdayProfile()
    }

    // 차단 친구 리스트 초기화
    private fun initBirthdayProfile() {
        binding.rvBlockedFriend.layoutManager = LinearLayoutManager(requireContext())
        binding.rvBlockedFriend.adapter = blockedAdapter

        lifecycleScope.launch {
            viewModel.blockedFriend.collectLatest {
                if (it.isNullOrEmpty()) { // empty view
                    showEmptyView(GONE)
                } else {
                    blockedAdapter.data = it
                    blockedAdapter.notifyDataSetChanged()
                    showEmptyView(VISIBLE)
                }
            }
        }
    }

    private fun showEmptyView(type: Int) {
        binding.rvBlockedFriend.visibility = type
        binding.tvBlockedFriendEmpty.visibility = 8 - type
    }

    companion object {
        const val BLOCKED_PROFILE = 4

        const val GONE = 8
        const val VISIBLE = 0
    }
}
