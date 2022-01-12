package com.sgs.devcamp2.flametalk_android.ui.signin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.sgs.devcamp2.flametalk_android.R
import com.sgs.devcamp2.flametalk_android.databinding.FragmentSigninBinding

class SigninFragment : Fragment() {
    lateinit var binding: FragmentSigninBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSigninBinding.inflate(inflater, container, false)
        initUI()
        return binding.root
    }

    private fun initUI() {
        // 로그인 > 회원가입 이동
        binding.tvSigninToSignup.setOnClickListener {
            findNavController().navigate(R.id.navigation_signup)
        }

        // 로그인 요청
        binding.btnSigninConfirm.setOnClickListener {
            submitLogin()
        }
    }

    private fun submitLogin() {
        // TODO: 로그인 통신 구현
    }
}
