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
import com.sgs.devcamp2.flametalk_android.util.toInvisible
import com.sgs.devcamp2.flametalk_android.util.toVisible
import com.sgs.devcamp2.flametalk_android.util.toVisibleGone
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import timber.log.Timber

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

        // 친구 즐겨찾기
        binding.imgProfileBookmark.setOnClickListener {
            it.isActivated = !it.isActivated
            Snackbar.make(requireContext(), it, it.isActivated.toString(), Snackbar.LENGTH_SHORT)
                .show()
        }
        // 프로필 수정하기로 이동
        binding.cstProfileEdit.setOnClickListener {
            findNavController().navigate(
                ProfileFragmentDirections.actionProfileToEdit(viewModel.userProfile.value)
            )
        }

        // 프로필 메뉴: 숨김친구, 차단친구
        binding.imgProfileMenu.setOnClickListener {
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
    }

    // 메인 유저, 친구 여부에 따라 UI가 다름
    private fun initViewType() {
        when (args.viewType) {
            USER_DEFAULT_PROFILE -> { // 내 프로필
                binding.imgProfileBookmark.toInvisible()
                binding.imgProfileFriend.toVisibleGone()
                binding.tvProfileFriend.toVisibleGone()
                swapViewVisibility(binding.cstProfileChat, binding.cstProfileEdit)
            }
            FRIEND_PROFILE -> { // 친구 프로필
                binding.imgProfileBookmark.toVisible()
                swapViewVisibility(binding.cstProfileEdit, binding.cstProfileChat)

                // 클릭한 프로필의 유저와 1:1 채팅방 생성 페이지로 이동
                binding.cstProfileChat.setOnClickListener {
                    val profileToCreateChatRoomDirections: NavDirections =
                        ProfileFragmentDirections.actionProfileToCreateChatRoom(
                            // users의 default value를 null로 설정하여 생략
                            singleFriendId = args.friendId,
                        )
                    findNavController().navigate(profileToCreateChatRoomDirections)
                }
            }
            USER_MULTI_PROFILE -> { // 내 멀티 프로필
                binding.imgProfileBookmark.toInvisible()
                binding.imgProfileFriend.toVisible()
                binding.tvProfileFriend.toVisible()
            }
            BLOCKED_PROFILE -> { // 차단된 프로필
                // TODO: 차단 해제 여부 버튼 노출
            }
            HIDDEN_PROFILE -> { // 숨긴 프로필
                // TODO: 숨김 해제 여부 버튼 노출
            }
        }
    }

    // 유저프로필 초기화
    private fun initUserProfile() {
        viewModel.getProfileData(profileId = args.profileId)

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
        rootLayout = binding.cstProfile

        /**프로필 조회하는 디바이스의 사이즈에 따라 scaling 하기 위해
         디바이스의 기기 가로, 세로 사이즈로 나누어 position 저장*/
        val dm: DisplayMetrics = requireContext().resources.displayMetrics
        val width = dm.widthPixels
        val height = dm.heightPixels
        Timber.d("width: $width, height: $height")

        // 스티커를 위한 ImageView 동적 생성
        val img = AppCompatImageView(requireContext())
        // 생성할 스티커의 사이즈
        val param = ConstraintLayout.LayoutParams(100, 100)
        // 스티커 생성하고 중앙에 배치하기 위한 layout 제약
        param.startToStart = ConstraintLayout.LayoutParams.PARENT_ID
        param.topToTop = ConstraintLayout.LayoutParams.PARENT_ID

//        val lParams = img.layoutParams as ConstraintLayout.LayoutParams
//        lParams.marginStart = positionX.toInt()
//        lParams.topMargin = positionY.toInt()
//
//        img.layoutParams = lParams

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

    companion object {
        private const val USER_DEFAULT_PROFILE = 1
        private const val FRIEND_PROFILE = 2
        private const val USER_MULTI_PROFILE = 3
        private const val BLOCKED_PROFILE = 4
        private const val HIDDEN_PROFILE = 5

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
