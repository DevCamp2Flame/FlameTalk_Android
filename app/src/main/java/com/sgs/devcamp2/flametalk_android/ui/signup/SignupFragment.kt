package com.sgs.devcamp2.flametalk_android.ui.signup

import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.sgs.devcamp2.flametalk_android.R
import com.sgs.devcamp2.flametalk_android.databinding.FragmentSignupBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * @author 박소연
 * @created 2022/01/20
 * @updated 2022/01/25
 * @desc 회원가입 화면
 */

@AndroidEntryPoint
@ExperimentalCoroutinesApi
class SignupFragment : Fragment() {
    private val binding by lazy { FragmentSignupBinding.inflate(layoutInflater) }

    private val viewModel by viewModels<SignupViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initUI()
        return binding.root
    }

    private fun initUI() {
        // 뒤로가기 // 회원가입 > 로그인
        binding.imgSignupBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.btnSignupEmailCheck.setOnClickListener {

        }

        // 회원가입 요청
        binding.btnSignupConfirm.setOnClickListener {
            submitSignup()
        }
    }

    private fun submitSignup() {
        // 이벤트가 발생하면 회원가입 요청을 보냄
        viewModel.signUp( // 이메일, 비번, 닉네임, 폰번, 생일, 소셜로그인 여부, 지역코드, 디바이스ID
            binding.edtSignupEmail.text.toString(),
            binding.edtSignupPwd.text.toString(),
            binding.edtSignupName.text.toString(),
            binding.edtSignupTel.text.toString(),
            binding.edtSignupBirth.text.toString(),
            "LOGIN",
            "82",
            "KR",
            Settings.Secure.getString(context!!.contentResolver, Settings.Secure.ANDROID_ID)
        )

        // 회원가입 된 유저의 닉네임 띄움
        lifecycleScope.launch {
            viewModel.nickname.collectLatest {
                if (it != "") {
                    Snackbar.make(requireView(), "${it}님 회원가입 되었습니다.", Snackbar.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.navigation_signin)
                } else {
                    Snackbar.make(requireView(), "회원가입에 실패했습니다.", Snackbar.LENGTH_SHORT).show()
                    findNavController().navigateUp()
                }
            }
        }
    }

    private fun emailDoubleCheck() {
        // 이벤트가 발생하면 회원가입 요청을 보냄
        viewModel.emailCheck(binding.edtSignupEmail.toString())

        // 회원가입 된 유저의 닉네임 띄움
        lifecycleScope.launch {
            viewModel.emailCheck.collectLatest {
                if (it) {
                    Snackbar.make(requireView(), "사용 가능한 이메일입니다.", Snackbar.LENGTH_SHORT).show()
                } else {
                    Snackbar.make(requireView(), "이미 가입된 이메일입니다.", Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }
}
