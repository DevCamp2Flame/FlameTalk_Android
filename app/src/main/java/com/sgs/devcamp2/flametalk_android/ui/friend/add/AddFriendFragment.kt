package com.sgs.devcamp2.flametalk_android.ui.friend.add

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
import com.google.android.material.snackbar.Snackbar
import com.sgs.devcamp2.flametalk_android.R
import com.sgs.devcamp2.flametalk_android.databinding.FragmentAddFriendBinding
import com.sgs.devcamp2.flametalk_android.util.toVisible
import com.sgs.devcamp2.flametalk_android.util.toVisibleGone
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * @author 박소연
 * @created 2022/02/09
 * @updated 2022/02/10
 * @desc 친구 추가 화면. 전화번호를 통해 친구를 추가할 수 있다. 이 뷰 이전에 보여줄 프로필을 선택해야 한다.
 */

@AndroidEntryPoint
@ExperimentalCoroutinesApi
class AddFriendFragment : Fragment() {
    private val binding by lazy { FragmentAddFriendBinding.inflate(layoutInflater) }
    private val viewModel by viewModels<AddFriendViewModel>()
    private val profileAdapter: SelectProfileAdapter by lazy {
        SelectProfileAdapter(
            requireContext(),
            viewModel.nickname.value,
            onClicked = {
                viewModel.clickedProfile(it.id)
                Snackbar.make(requireView(), "클릭한 아이템 ${it.id}", Snackbar.LENGTH_SHORT).show()
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
        initAllProfile()

        lifecycleScope.launch {
            viewModel.isSuccess.collectLatest {
                if (it != null) {
                    if (it) {
                        binding.itemAddFriend.root.toVisible()
                    } else {
                        binding.itemAddFriend.root.toVisibleGone()
                    }
                }
            }
        }

        lifecycleScope.launch {
            viewModel.friendData.collectLatest {
                if (it != null) {
                    // 프로필 이미지, 닉네임, 상태메세지
                    Glide.with(binding.itemAddFriend.imgFriendPreview).load(it.imageUrl)
                        .apply(RequestOptions.circleCropTransform())
                        .apply(RequestOptions.placeholderOf(R.drawable.ic_person_white_24))
                        .into(binding.itemAddFriend.imgFriendPreview)
                    binding.itemAddFriend.tvFriendPreviewDesc.text = it.description
                    binding.itemAddFriend.tvFriendPreviewNickname.text = it.nickname
                    binding.itemAddFriend.tvFriendPreviewCount.toVisibleGone()
                } else {
                    binding.itemAddFriend.root.toVisibleGone()
                }
            }
        }

        lifecycleScope.launch {
            viewModel.message.collectLatest {
                if (it != null) {
                    binding.tvAddFriendResult.text = it
                }
            }
        }

        initEventListener()
    }

    private fun initAllProfile() {
        binding.rvAddFriendSelect.layoutManager = LinearLayoutManager(
            requireContext(), RecyclerView.HORIZONTAL, false
        )
        binding.rvAddFriendSelect.adapter = profileAdapter
        lifecycleScope.launch {
            viewModel.profiles.collectLatest {
                if (it.isNotEmpty()) {
                    profileAdapter.data = it
                    profileAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    private fun initEventListener() {
        // 친구 추가
        binding.cktAddFriendConfirm.setOnClickListener {
            // TODO: 1.프로필 recyclerView에서 click된 item의 UI border처리
            // TODO: 2.click된 item의 profileId를 _profileId에 저장

            // 현재의 Id가
            viewModel.addFriend(binding.edtAddFriendTel.text.toString())
        }
    }
}
