package com.sgs.devcamp2.flametalk_android.ui.signin

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.sgs.devcamp2.flametalk_android.R
import com.sgs.devcamp2.flametalk_android.databinding.FragmentSigninBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SigninFragment : Fragment() {
    lateinit var binding: FragmentSigninBinding

    // firebase google auth
    @Inject
    lateinit var googleSignIn: GoogleSignInOptions
    lateinit var auth: FirebaseAuth
    lateinit var googleSignInClient: GoogleSignInClient
    lateinit var resultLauncher: ActivityResultLauncher<Intent>
    lateinit var signinIntent: Intent
    val TAG: String = "로그"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSigninBinding.inflate(inflater, container, false)
        initUI()

        var gso = googleSignIn
        googleSignInClient = GoogleSignIn.getClient(this.requireActivity(), gso)
        auth = Firebase.auth
        val user = Firebase.auth.currentUser
        Log.d(TAG, "${user?.providerData}")
        signinIntent = googleSignInClient.signInIntent
        resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            result ->
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

        binding.btnSigninGoogle.setOnClickListener {
            googleLogin()
        }
    }

    private fun submitLogin() {
        // TODO: 로그인 통신 구현
    }

    private fun googleLogin() {

        resultLauncher.launch(signinIntent)
    }

    private fun firebaseAuthWithGoogle(idToken: String?) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this.requireActivity()) {
                task ->
                if (task.isSuccessful) {
                    // Log.d("googleAuthToken", "OAuthActivity - firebaseAuthWithGoogle() success called :$idToken")
                } else {
                }
            }
    }
}
