package com.sgs.devcamp2.flametalk_android.ui.signup

import android.annotation.SuppressLint
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.sgs.devcamp2.flametalk_android.databinding.FragmentSignupBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * @author 박소연
 * @created 2022/01/20
 * @updated 2022/02/06
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
    ): View {
        initUI()
        return binding.root
    }

    private fun initUI() {
        // 뒤로가기: 회원가입 > 로그인
        binding.imgSignupBack.setOnClickListener {
            findNavController().navigateUp()
        }

        // 이메일 중복 확인 요청
        binding.btnSignupEmailCheck.setOnClickListener {
            emailDoubleCheck()
        }

        // 회원가입 요청
        binding.btnSignupConfirm.setOnClickListener {
            if (viewModel.emailCheck.value) {
                submitSignup()
            } else {
                Snackbar.make(requireView(), "이메일 중복확인이 필요합니다.", Snackbar.LENGTH_SHORT).show()
            }
        }

        // 사용자에게 피드백 할 메세지
        lifecycleScope.launch {
            viewModel.message.collect {
                if (it.isNotEmpty()) {
                    Snackbar.make(requireView(), it, Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }

    @SuppressLint("HardwareIds")
    private fun submitSignup() {
        // 이벤트가 발생하면 회원가입 요청을 보냄
        if (viewModel.emailCheck.value) {
            viewModel.signUp( // 이메일, 비번, 닉네임, 폰번, 생일, 소셜로그인 여부, 지역코드, 디바이스ID
                binding.edtSignupEmail.text.toString(),
                binding.edtSignupPwd.text.toString(),
                binding.edtSignupName.text.toString(),
                binding.edtSignupTel.text.toString(),
                binding.edtSignupBirth.text.toString(),
                "LOGIN",
                "82",
                "KR",
                Settings.Secure.getString(
                    requireContext().contentResolver,
                    Settings.Secure.ANDROID_ID
                )
            )
        } else {
            Snackbar.make(requireView(), "이메일 중복 확인을 진행해주세요", Snackbar.LENGTH_SHORT).show()
        }

        // 회원가입 된 유저의 닉네임 띄움
        lifecycleScope.launch {
            viewModel.nickname?.collectLatest {
                if (it != "" && it != null) { // 서버로부터 응답이 옴
                    Snackbar.make(requireView(), "${it}님 회원가입 되었습니다.", Snackbar.LENGTH_SHORT)
                        .show()
                    val signupToSigninDirections: NavDirections =
                        SignupFragmentDirections.actionSignupToSignin(
                            id = binding.edtSignupEmail.text.toString(),
                            password = binding.edtSignupPwd.text.toString()
                        )
                    findNavController().navigate(signupToSigninDirections)
                }
            }
        }
    }

    private fun emailDoubleCheck() {
        // 회원가입 통신 요청
        viewModel.emailCheck(binding.edtSignupEmail.toString())
    }
}
