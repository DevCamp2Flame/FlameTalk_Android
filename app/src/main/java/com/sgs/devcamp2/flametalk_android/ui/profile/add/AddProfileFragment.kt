package com.sgs.devcamp2.flametalk_android.ui.profile.add

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.snackbar.Snackbar
import com.sgs.devcamp2.flametalk_android.R
import com.sgs.devcamp2.flametalk_android.databinding.FragmentAddProfileBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest

/**
 * @author 박소연
 * @created 2022/01/25
 * @updated 2022/02/17
 * @desc 프로필 생성 페이지 (배경 이미지, 프로필 이미지, 상태메세지)
 *       스티커 붙이기
 */

@AndroidEntryPoint
@ExperimentalCoroutinesApi
class AddProfileFragment : Fragment() {
    private val binding by lazy { FragmentAddProfileBinding.inflate(layoutInflater) }
    private val viewModel by activityViewModels<AddProfileViewModel>()
    private var rootLayout: ViewGroup? = null
    private var xPoint = 0
    private var yPoint = 0

    private val getProfileImageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data?.data != null) {
                viewModel.setProfileImage(
                    viewModel.uriToPath(
                        requireActivity(),
                        result.data!!.data!!
                    )
                )
            }
        }
    private val getBackgroundImageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data?.data != null) {
                viewModel.setBackgroundImage(
                    viewModel.uriToPath(
                        requireActivity(),
                        result.data!!.data!!
                    )
                )
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        updateUI()
        initUI()
        initClickEvent()
        return binding.root
    }

    private fun initUI() {
        initSticker()
    }

    // ViewModel StateFlow 변수가 갱신되면 UI에 자동 바인딩한다
    private fun updateUI() {
        // 닉네임
        lifecycleScope.launchWhenResumed {
            viewModel.nickname.collectLatest {
                binding.tvAddProfileNickname.text = it
            }
        }

        // 상태메세지
        lifecycleScope.launchWhenResumed {
            viewModel.description.collectLatest {
                binding.tvAddProfileDesc.text = it
            }
        }

        // 프로필 이미지
        lifecycleScope.launchWhenResumed {
            viewModel.profileImage.collectLatest {
                Glide.with(binding.imgAddProfile)
                    .load(it)
                    .apply(RequestOptions.circleCropTransform())
                    .into(binding.imgAddProfile)
            }
        }

        // 배경 이미지
        lifecycleScope.launchWhenResumed {
            viewModel.backgroundImage.collectLatest {
                Glide.with(binding.imgAddProfileBg)
                    .load(it)
                    .into(binding.imgAddProfileBg)
            }
        }

        // 프로필 생성 성공 여부
        lifecycleScope.launchWhenResumed {
            viewModel.isSuccess.collectLatest {
                if (it != null) {
                    findNavController().navigate(R.id.navigation_friend)
                }
            }
        }

        // 프로필 생성 성공 여부
        lifecycleScope.launchWhenResumed {
            viewModel.message.collectLatest {
                if (it != "") {
                    Snackbar.make(requireView(), it, Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun initClickEvent() {
        binding.tvAddProfileClose.setOnClickListener {
            findNavController().popBackStack()
        }

        // 프로필 이미지 변경
        binding.imgAddProfileGallery.setOnClickListener {
            getProfileImage(PROFILE_IMAGE)
        }

        // 배경 이미지 변경
        binding.imgAddProfileGalleryBg.setOnClickListener {
            getProfileImage(BACKGROUND_IMAGE)
        }

        // 상태메세지 생성
        binding.tvAddProfileDesc.setOnClickListener {
            val addProfileToDescDirections: NavDirections =
                AddProfileFragmentDirections.actionAddToEditDesc(startView = "Add")
            findNavController().navigate(addProfileToDescDirections)
        }

        // 프로필 생성 완료
        binding.tvAddProfileConfirm.setOnClickListener {
            // 파일 생성 통신 요청
            viewModel.addProfile()
        }
    }

    // 이미지 변경
    private fun getProfileImage(type: Int) {
        if (checkPermission()) {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = MediaStore.Images.Media.CONTENT_TYPE
            intent.type = "image/*"
            intent.putExtra("crop", true)
            intent.putExtra("aspectX", 1)
            intent.putExtra("aspectY", 1)
            intent.putExtra("scale", true)
            when (type) {
                PROFILE_IMAGE -> {
                    getProfileImageLauncher.launch(intent)
                }
                BACKGROUND_IMAGE -> {
                    getBackgroundImageLauncher.launch(intent)
                }
            }
        } else {
            requestPermission()
        }
    }

    private fun checkPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireActivity(),
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    // 저장소 접근 권한
    private fun requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        ) {
            Toast.makeText(requireActivity(), "권한이 거부되었습니다. 직접 권한을 허용해야 합니다.", Toast.LENGTH_SHORT)
                .show()
        } else {
            requestPermissions(
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                PERMISSION_CALLBACK_CONSTANT
            )
        }
    }

    private fun initSticker() {
        rootLayout = binding.cstAddProfile

        Glide.with(requireContext()).load(R.drawable.emoji_aww).into(binding.imgAddProfileEmoji1)
        Glide.with(requireContext()).load(R.drawable.emoji_clap).into(binding.imgAddProfileEmoji2)
        Glide.with(requireContext()).load(R.drawable.emoji_dance).into(binding.imgAddProfileEmoji3)
        Glide.with(requireContext()).load(R.drawable.emoji_hearts).into(binding.imgAddProfileEmoji4)
        Glide.with(requireContext()).load(R.drawable.emoji_party).into(binding.imgAddProfileEmoji5)
        Glide.with(requireContext()).load(R.drawable.emoji_sad).into(binding.imgAddProfileEmoji6)

        binding.imgAddProfileEmoji1.setOnClickListener {
            binding.cstAddProfile.addView(createImageView(EMOJI_AWW))
        }
        binding.imgAddProfileEmoji2.setOnClickListener {
            binding.cstAddProfile.addView(createImageView(EMOJI_CLAP))
        }
        binding.imgAddProfileEmoji3.setOnClickListener {
            binding.cstAddProfile.addView(createImageView(EMOJI_DANCE))
        }
        binding.imgAddProfileEmoji4.setOnClickListener {
            binding.cstAddProfile.addView(createImageView(EMOJI_HEART))
        }
        binding.imgAddProfileEmoji5.setOnClickListener {
            binding.cstAddProfile.addView(createImageView(EMOJI_PARTY))
        }
        binding.imgAddProfileEmoji6.setOnClickListener {
            binding.cstAddProfile.addView(createImageView(EMOJI_SAD))
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun createImageView(emoji: Int): View {
        /**프로필 조회하는 디바이스의 사이즈에 따라 scaling 하기 위해
         디바이스의 기기 가로, 세로 사이즈로 나누어 position 저장*/
        val dm: DisplayMetrics = requireContext().resources.displayMetrics
        val width = dm.widthPixels
        val height = dm.heightPixels

        // 스티커를 위한 ImageView 동적 생성
        val img = AppCompatImageView(requireContext())
        // 생성할 스티커의 사이즈
        val param = ConstraintLayout.LayoutParams(100, 100)
        // 스티커 생성하고 중앙에 배치하기 위한 layout 제약
        param.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID
        // param.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID
        param.topToTop = ConstraintLayout.LayoutParams.PARENT_ID
        // param.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID
        // param.marginStart = 300
        param.leftMargin = width / 2
        param.topMargin = height / 2
        img.layoutParams = param

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

        // 스티커 삭제
        img.setOnLongClickListener {
            popupDeleteMenu(it)
        }
        // 스티커 위치 드래그
        img.setOnTouchListener(StickerListener(img.id, emoji))

        return img
    }

    private inner class StickerListener(id: Int, emoji: Int) : View.OnTouchListener {
        val stickerId = id
        val emojiType = emoji

        @SuppressLint("ClickableViewAccessibility")
        override fun onTouch(view: View, event: MotionEvent): Boolean {
            // 화면의 원시 좌표
            val x = event.rawX.toInt()
            val y = event.rawY.toInt()

            when (event.action and MotionEvent.ACTION_MASK) {
                MotionEvent.ACTION_DOWN -> {
                    val lParams = view.layoutParams as ConstraintLayout.LayoutParams
                    /** 손가락으로 터치 한 좌표에 스티커를 위치시킨다 */
                    xPoint = x - lParams.leftMargin
                    yPoint = y - lParams.topMargin
                }
                MotionEvent.ACTION_UP -> {
                    val itemParams = view.layoutParams as ConstraintLayout.LayoutParams

                    /** 손가락 뗄 때 해당 스티커의 정보를 ViewModel의 arrayList에 추가한다 */
                    viewModel.createSticker(
                        stickerId,
                        emojiType,
                        itemParams.leftMargin.toDouble(),
                        itemParams.topMargin.toDouble()
                    )
                }
                MotionEvent.ACTION_POINTER_DOWN -> {}
                MotionEvent.ACTION_POINTER_UP -> {}
                MotionEvent.ACTION_MOVE -> {
                    /** 손가락이 움직이는 좌표에 스티커를 위치시킨다 */
                    val layoutParams = view.layoutParams as ConstraintLayout.LayoutParams
                    layoutParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID
                    layoutParams.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID

                    layoutParams.leftMargin = x - xPoint
                    layoutParams.topMargin = y - yPoint
                    view.layoutParams = layoutParams
                }
            }
            rootLayout!!.invalidate()
            return true
        }
    }

    private fun popupDeleteMenu(item: View): Boolean {
        val popupMenu = PopupMenu(context, item)
        popupMenu.inflate(R.menu.delete_menu)
        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menu_delete -> {
                    // View에서 아이템 삭제
                    binding.cstAddProfile.removeView(item)
                    // 함수로 삭제할 아이템의 아이디를 전달
                    viewModel.removeSticker(item.id)
                }
                else -> { // 실행되지 않음
                }
            }
            return@setOnMenuItemClickListener false
        }
        popupMenu.show()
        return true
    }

    companion object {
        private const val PROFILE_IMAGE = 1
        private const val BACKGROUND_IMAGE = 2
        private const val PERMISSION_CALLBACK_CONSTANT = 100

        private const val EMOJI_AWW = 10
        private const val EMOJI_CLAP = 20
        private const val EMOJI_DANCE = 30
        private const val EMOJI_HEART = 40
        private const val EMOJI_PARTY = 50
        private const val EMOJI_SAD = 60
    }
}
