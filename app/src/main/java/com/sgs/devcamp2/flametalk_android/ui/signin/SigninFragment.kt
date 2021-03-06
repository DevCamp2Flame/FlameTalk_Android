package com.sgs.devcamp2.flametalk_android.ui.signin

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.sgs.devcamp2.flametalk_android.R
import com.sgs.devcamp2.flametalk_android.databinding.FragmentSigninBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

/**
 * @author 박소연
 * @created 2022/01/19
 * @updated 2022/01/26
 * @desc 로그인 화면. (이메일을 이용한 자체 로그인, Google 로그인)
 */

@AndroidEntryPoint
@ExperimentalCoroutinesApi
class SigninFragment : Fragment() {
    private val binding by lazy { FragmentSigninBinding.inflate(layoutInflater) }
    private val viewModel by viewModels<SigninViewModel>()
    private val args: SigninFragmentArgs by navArgs()

    // firebase google auth
    @Inject
    lateinit var googleSignIn: GoogleSignInOptions
    lateinit var auth: FirebaseAuth
    lateinit var googleSignInClient: GoogleSignInClient
    lateinit var resultLauncher: ActivityResultLauncher<Intent>
    lateinit var signinIntent: Intent

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initUI()

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        clearUI()
    }

    private fun initUI() {
        binding.edtSigninEmail.setText(args.id)
        binding.edtSigninPwd.setText(args.password)

        initEventListener()
        initGoogleSignin()
    }

    // 입력 창 초기화
    private fun clearUI() {
        Timber.d("Signin onResume 호출")
        binding.edtSigninEmail.text.clear()
        binding.edtSigninPwd.text.clear()
    }

    private fun initEventListener() {
        // 로그인 > 회원가입 이동
        binding.tvSigninToSignup.setOnClickListener {
            findNavController().navigate(R.id.navigation_signup)
        }

        // 로그인 요청
        binding.btnSigninConfirm.setOnClickListener {
            submitLogin()
        }

        binding.btnSigninGoogle.setOnClickListener {
            googleLogin()
        }
    }

    // [현국] Google 로그인
    private fun initGoogleSignin() {
        var gso = googleSignIn
        googleSignInClient = GoogleSignIn.getClient(this.requireActivity(), gso)
        auth = Firebase.auth
        val user = Firebase.auth.currentUser
        signinIntent = googleSignInClient.signInIntent
        resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                run {
                    if (result.resultCode == RESULT_OK) {
                        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                        try {
                            val account = task.getResult(ApiException::class.java)
                            firebaseAuthWithGoogle(account.idToken)
                        } catch (e: ApiException) {
                        }
                    }
                }
            }
    }

    // [소연] 로그인
    private fun submitLogin() {
        viewModel.signIn( // 이메일, 비번, 소셜, 디바이스ID
            binding.edtSigninEmail.text.toString(),
            binding.edtSigninPwd.text.toString(),
            "LOGIN",
            Settings.Secure.getString(requireContext().contentResolver, Settings.Secure.ANDROID_ID)
        )

        // 로그인된 유저의 닉네임 띄움
        lifecycleScope.launch {
            viewModel.nickname.collectLatest {
                if (it.isNotEmpty()) {
                    Snackbar.make(requireView(), "${it}님 로그인 되었습니다.", Snackbar.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.navigation_friend)
                }
            }
        }

        // 로그인 결과
        lifecycleScope.launch {
            viewModel.message.collect {
                if (it.isNotEmpty()) {
                    Snackbar.make(requireView(), it, Snackbar.LENGTH_SHORT).show()
                }
            }
        }

        // 에러 메세지
        lifecycleScope.launch {
            viewModel.error.collectLatest {
                if (it.isNotEmpty()) {
                    Snackbar.make(requireView(), it, Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun googleLogin() {
        resultLauncher.launch(signinIntent)
    }

    private fun firebaseAuthWithGoogle(idToken: String?) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this.requireActivity()) { task ->
                if (task.isSuccessful) {
                    Log.d("googleAuthToken", "OAuthActivity - firebaseAuthWithGoogle() success called :$idToken")
                    val action = SigninFragmentDirections.actionNavigationSigninToNavigationAdditionalUserInfo(idToken)
                    findNavController().navigate(action)
                } else {
                }
            }
    }
}
