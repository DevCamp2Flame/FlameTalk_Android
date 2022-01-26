package com.sgs.devcamp2.flametalk_android.ui.setting

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.sgs.devcamp2.flametalk_android.databinding.FragmentSettingBinding
import com.sgs.devcamp2.flametalk_android.util.toVisibleGone
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * @author 박소연
 * @created 2022/01/26
 * @updated
 * @desc 기타 설정 화면 (탈퇴하기)
 */

@AndroidEntryPoint
@ExperimentalCoroutinesApi
class SettingFragment : Fragment() {
    private val binding by lazy { FragmentSettingBinding.inflate(layoutInflater) }
    private val viewModel: SettingViewModel by viewModels()

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
        initLeave()

        // 메세지 알림
        lifecycleScope.launch {
            viewModel.message.collectLatest {
                Snackbar.make(requireView(), it, Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun initAppbar() {
        binding.abSetting.tvAppbar.text = "설정"
        binding.abSetting.imgAppbarSearch.toVisibleGone()
        binding.abSetting.imgAppbarAddFriend.toVisibleGone()
        binding.abSetting.imgAppbarSetting.toVisibleGone()
    }

    private fun initLeave() {
        binding.itemLeave.root.setOnClickListener {
            initLeaveDialog()
        }
        binding.itemLeave.tvSettingTitle.text = "탈퇴하기"
        binding.itemLeave.tvSettingDesc.toVisibleGone()
        binding.itemLeave.swSettingTitle.toVisibleGone()
        binding.itemLeave.imgSettingRefresh.toVisibleGone()
    }

    private fun initLeaveDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("탈퇴 하시겠습니까?")
            .setMessage("탈퇴하면 일정 기간 동안 재가입이 불가능합니다.")
            .setPositiveButton(
                "탈퇴",
                object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface, which: Int) {
                        viewModel.leaveUser()
                        dialog.cancel()
                    }
                }
            ).setNegativeButton(
                "취소",
                object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface, which: Int) {
                        dialog.dismiss()
                    }
                }
            )
            .create()
            .show()
    }
}
