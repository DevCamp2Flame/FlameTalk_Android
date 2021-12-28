package com.sgs.devcamp2.flametalk_android.ui.signin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
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
        binding.btnSigninConfirm.setOnClickListener {
            submitLogin()
        }
    }

    private fun submitLogin() {
        // TODO: 로그인 통신 구현
    }
}
