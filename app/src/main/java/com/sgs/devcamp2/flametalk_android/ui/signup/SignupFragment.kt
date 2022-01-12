package com.sgs.devcamp2.flametalk_android.ui.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.sgs.devcamp2.flametalk_android.R
import com.sgs.devcamp2.flametalk_android.databinding.FragmentSignupBinding

class SignupFragment : Fragment() {
    lateinit var binding: FragmentSignupBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignupBinding.inflate(inflater, container, false)
        initUI()
        return binding.root
    }

    private fun initUI() {
        // 뒤로가기 // 회원가입 > 로그인
        binding.imgSignupBack.setOnClickListener {
            findNavController().navigateUp()
        }

        // 회원가입 요청
        binding.btnSignupConfirm.setOnClickListener {
            submitSignup()
        }
    }

    private fun submitSignup() {
        // TODO: 회원가입 통신 구현
        findNavController().navigate(R.id.navigation_signin)

    }
}
