package com.sgs.devcamp2.flametalk_android.ui.friend

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import androidx.fragment.app.viewModels
import com.sgs.devcamp2.flametalk_android.databinding.FragmentFriendBinding

    /*
        @name FriendFragment
        @auth 박소연
        @description 앱 실행 시 기본 화면. 하단 1번째 탭. 유저의 친구 목록
    */

@AndroidEntryPoint
@ExperimentalCoroutinesApi
class FriendFragment : Fragment() {
    lateinit var binding: FragmentFriendBinding
    private val viewModel: FriendViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFriendBinding.inflate(inflater, container,false)
        initUI()
        return binding.root
    }

    private fun initUI() {
    }
}
