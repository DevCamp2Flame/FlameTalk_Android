package com.sgs.devcamp2.flametalk_android.ui.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.sgs.devcamp2.flametalk_android.databinding.FragmentSettingBinding
import com.sgs.devcamp2.flametalk_android.ui.friend.FriendViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

/*
    @name SettingFragment
    @auth 박소연
    @description 하단 3번째 탭. 앱의 설정 기능 페이지
*/

@AndroidEntryPoint
@ExperimentalCoroutinesApi
class SettingFragment : Fragment() {
    private val binding by lazy { FragmentSettingBinding.inflate(layoutInflater) }
    // lateinit var binding: FragmentSettingBinding
    private val viewModel: SettingViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // binding = FragmentSettingBinding.inflate(inflater, container,false)
        initUI()
        return binding.root
    }

    private fun initUI() {
    }
}
