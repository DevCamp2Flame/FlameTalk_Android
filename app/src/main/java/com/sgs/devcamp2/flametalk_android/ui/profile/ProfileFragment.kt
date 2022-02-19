package com.sgs.devcamp2.flametalk_android.ui.profile

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.snackbar.Snackbar
import com.sgs.devcamp2.flametalk_android.R
import com.sgs.devcamp2.flametalk_android.databinding.FragmentProfileBinding
import com.sgs.devcamp2.flametalk_android.util.swapViewVisibility
import com.sgs.devcamp2.flametalk_android.util.toVisible
import com.sgs.devcamp2.flametalk_android.util.toVisibleGone
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest

/**
 * @author 박소연
 * @created 2022/01/17
 * @created 2022/01/31
 * @desc 프로필 상세 보기 페이지
 */

@AndroidEntryPoint
@ExperimentalCoroutinesApi
class ProfileFragment : Fragment() {
    private val binding by lazy { FragmentProfileBinding.inflate(layoutInflater) }
    private val viewModel by viewModels<ProfileViewModel>()
    private val args: ProfileFragmentArgs by navArgs()
    val param = ConstraintLayout.LayoutParams(100, 100)
    private var rootLayout: ViewGroup? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        initUI()
        return binding.root
    }

    private fun initUI() {
        initViewType()
        initUserProfile()

        // 프로필 히스토리 피드로 이동
        binding.imgProfile.setOnClickListener {
            findNavController().navigate(
                ProfileFragmentDirections.actionProfileToFeedSingle(
                    args.profileId,
                    false
                )
            )
        }

        // 배경화면 히스토리 피드로 이동
        binding.imgProfileBg.setOnClickListener {
            findNavController().navigate(
                ProfileFragmentDirections.actionProfileToFeedSingle(
                    args.profileId,
                    true
                )
            )
        }

        // 프로필 상세 닫기
        binding.imgProfileClose.setOnClickListener {
            findNavController().navigateUp()
        }

        // 프로필 수정하기로 이동
        binding.cstProfileEdit.setOnClickListener {
            findNavController().navigate(
                ProfileFragmentDirections.actionProfileToEdit(viewModel.userProfile.value)
            )
        }

        /**프로필 메뉴: 프로필 유형에 따라 메뉴가 다름
         * 친구 프로필 -> 친구 숨기기, 친구 차단하기
         * 내 멀티프로필 -> 삭제하기 */
        binding.imgProfileMenu.setOnClickListener {
            when (args.viewType) {
                FRIEND_PROFILE -> { // 친구 프로필
                    friendPopup()
                }
                USER_MULTI_PROFILE -> {
                    multiProfilePopup()
                }
            }
        }
    }

    // 메인 유저, 친구 여부에 따라 UI가 다름
    private fun initViewType() {
        when (args.viewType) {
            USER_DEFAULT_PROFILE -> { // 내 프로필
                binding.imgProfileMenu.toVisibleGone()
                swapViewVisibility(binding.cstProfileChat, binding.cstProfileEdit)
            }
            FRIEND_PROFILE -> { // 친구 프로필
                binding.imgProfileMenu.toVisible()
                swapViewVisibility(binding.cstProfileEdit, binding.cstProfileChat)

                // 클릭한 프로필의 유저와 1:1 채팅방 생성 페이지로 이동
                binding.cstProfileChat.setOnClickListener {
                    val profileToCreateChatRoomDirections: NavDirections =
                        ProfileFragmentDirections.actionProfileToCreateChatRoom(
                            // users의 default value를 null로 설정하여 생략
                            singleFriendId = args.friendUserId,
                        )
                    findNavController().navigate(profileToCreateChatRoomDirections)
                }
            }
            USER_MULTI_PROFILE -> { // 내 멀티 프로필
            }
        }
    }

    // 유저프로필 초기화
    private fun initUserProfile() {
        rootLayout = binding.cstProfile

        viewModel.getProfileData(args.profileId)

        lifecycleScope.launchWhenResumed {
            viewModel.userProfile.collectLatest {
                binding.tvProfileNickname.text = it?.nickname
                binding.tvProfileDesc.text = it?.description
                Glide.with(binding.imgProfile)
                    .load(it?.imageUrl).apply(RequestOptions.circleCropTransform())
                    .into(binding.imgProfile)
                Glide.with(binding.imgProfileBg).load(it?.bgImageUrl)
                    .into(binding.imgProfileBg)
            }
        }
        rootLayout = binding.cstProfile
        lifecycleScope.launchWhenResumed {
            viewModel.stickers.collectLatest { sticker ->
                sticker.forEach {
                    binding.cstProfile.addView(
                        createImageView(
                            it.stickerId,
                            it.positionX,
                            it.positionY
                        )
                    )
                }
            }
        }

        lifecycleScope.launchWhenResumed {
            viewModel.message.collectLatest {
                if (it != "") {
                    Snackbar.make(requireView(), it, Snackbar.LENGTH_SHORT).show()
                }
            }
        }

        // 통신 성공하면 친구 리스트 화면으로 이동
        lifecycleScope.launchWhenResumed {
            viewModel.isSuccess.collectLatest {
                if (it == true) {
                    findNavController().navigate(R.id.navigation_friend)
                }
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun createImageView(emoji: Int, positionX: Double, positionY: Double): View {

        /**프로필 조회하는 디바이스의 사이즈에 따라 scaling 하기 위해
         디바이스의 기기 가로, 세로 사이즈로 나누어 position 저장*/
        val dm: DisplayMetrics = requireContext().resources.displayMetrics
        val width = dm.widthPixels
        val height = dm.heightPixels

        // 스티커를 위한 ImageView 동적 생성
        val img = AppCompatImageView(requireContext())
        // 생성할 스티커의 사이즈
        val param = ConstraintLayout.LayoutParams(100, 100)
        // 생성한 스티커를 저장된 좌표에 배치하기 위한 layout 제약
        param.startToStart = ConstraintLayout.LayoutParams.PARENT_ID
        param.topToTop = ConstraintLayout.LayoutParams.PARENT_ID
        param.marginStart = (positionX * width).toInt()
        param.topMargin = (positionY * height).toInt()

        when (emoji) {
            EMOJI_AWW -> Glide.with(requireContext()).load(R.drawable.emoji_aww).into(img)
            EMOJI_CLAP -> Glide.with(requireContext()).load(R.drawable.emoji_clap).into(img)
            EMOJI_DANCE -> Glide.with(requireContext()).load(R.drawable.emoji_dance).into(img)
            EMOJI_HEART -> Glide.with(requireContext()).load(R.drawable.emoji_hearts).into(img)
            EMOJI_PARTY -> Glide.with(requireContext()).load(R.drawable.emoji_party).into(img)
            EMOJI_SAD -> Glide.with(requireContext()).load(R.drawable.emoji_sad).into(img)
        }
        // 각 스티커 객체 별 아이디 생성
        img.id = ViewCompat.generateViewId()
        img.layoutParams = param

        return img
    }

    private fun friendPopup() {
        val popupMenu = PopupMenu(context, binding.imgProfileMenu)
        popupMenu.inflate(R.menu.profile_menu)
        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menu_hide -> {
                    // 숨김 친구 요청
                    viewModel.changeFriendStatue(TO_HIDE, args.friendId, args.assignedProfileId)
                }
                R.id.menu_block -> {
                    // 차단 친구 요청
                    viewModel.changeFriendStatue(
                        TO_BLOCK,
                        args.friendId,
                        args.assignedProfileId
                    )
                }
                else -> {
                    // 실행되지 않음
                }
            }
            return@setOnMenuItemClickListener false
        }
        popupMenu.show()
    }

    private fun multiProfilePopup() {
        val popupMenu = PopupMenu(context, binding.imgProfileMenu)
        popupMenu.inflate(R.menu.delete_menu)
        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menu_delete -> {
                    // 프로필 삭제 요청
                    viewModel.deleteProfile(args.profileId)
                }
            }
            return@setOnMenuItemClickListener false
        }
        popupMenu.show()
    }

    companion object {
        private const val USER_DEFAULT_PROFILE = 1
        private const val FRIEND_PROFILE = 2
        private const val USER_MULTI_PROFILE = 3

        private const val TO_BLOCK = 400
        private const val TO_HIDE = 500

        private const val EMOJI_AWW = 10
        private const val EMOJI_CLAP = 20
        private const val EMOJI_DANCE = 30
        private const val EMOJI_HEART = 40
        private const val EMOJI_PARTY = 50
        private const val EMOJI_SAD = 60
    }
}
