package com.sgs.devcamp2.flametalk_android.ui.friend.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
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
        initProfileList()

        binding.imgSignupBack.setOnClickListener {
            findNavController().navigate(R.id.navigation_friend)
        }

        lifecycleScope.launch {
            viewModel.friendData.collectLatest {
                if (it != null) {
                    // 결과 body가 날아오면 뷰를 보여줌
                    binding.itemAddFriend.root.toVisible()
                    binding.tvAddFriendResult.toVisible()

                    // 프로필 이미지, 닉네임, 상태메세지
                    Glide.with(binding.itemAddFriend.imgFriendPreview).load(it.imageUrl)
                        .transform(CenterCrop(), RoundedCorners(35))
                        .into(binding.itemAddFriend.imgFriendPreview)
                    binding.itemAddFriend.tvFriendPreviewNickname.text = it.friendNickname
                    if (it.description == null) {
                        binding.itemAddFriend.tvFriendPreviewDesc.toVisibleGone()
                    } else {
                        binding.itemAddFriend.tvFriendPreviewDesc.toVisible()
                        binding.itemAddFriend.tvFriendPreviewDesc.text = it.description
                    }
                    binding.itemAddFriend.tvFriendPreviewNickname.text = it.friendNickname
                    binding.itemAddFriend.tvFriendPreviewCount.toVisibleGone()
                } else {
                    binding.itemAddFriend.root.toVisibleGone()
                    binding.tvAddFriendResult.toVisibleGone()
                }
            }
        }

        lifecycleScope.launch {
            viewModel.message.collectLatest {
                if (it != null) {
                    Snackbar.make(requireView(), it, Snackbar.LENGTH_SHORT).show()
                }
            }
        }

        initEventListener()
    }

    private fun initProfileList() {
        binding.rvAddFriendSelect.layoutManager = LinearLayoutManager(
            requireContext(), RecyclerView.HORIZONTAL, false
        )
        binding.rvAddFriendSelect.adapter = profileAdapter
        lifecycleScope.launch {
            viewModel.profiles.collectLatest {
                if (it.isNotEmpty()) {
                    profileAdapter.data = it
                    profileAdapter.submitList(it)
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

        // 입력창 clear
        binding.imgAddFriendClear.setOnClickListener {
            binding.edtAddFriendTel.setText("")
        }
    }
}
