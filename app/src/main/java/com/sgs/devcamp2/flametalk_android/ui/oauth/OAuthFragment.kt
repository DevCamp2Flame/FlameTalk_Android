package com.sgs.devcamp2.flametalk_android.ui.oauth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.sgs.devcamp2.flametalk_android.databinding.FragmentOAuthBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class OAuthFragment : Fragment() {
    val TAG: String = "로그"
    lateinit var binding: FragmentOAuthBinding

    @Inject
    lateinit var googleSignIn: GoogleSignInOptions

    private val viewModel: AuthViewModel by viewModels<AuthViewModel>()
    lateinit var auth: FirebaseAuth

    lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentOAuthBinding.inflate(layoutInflater, container, false)
        var gso = googleSignIn
        googleSignInClient = GoogleSignIn.getClient(this.requireContext(), gso)
        auth = Firebase.auth
        val user = Firebase.auth.currentUser
        Log.d(TAG, "OAuthActivity - onCreate() called ${user?.providerData}")
        binding.btnGoogle.setOnClickListener {

            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, 1)
        }
        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        try {
            val account = task.getResult(ApiException::class.java)!!
            firebaseAuthWithGoogle(account.idToken)
        } catch (e: ApiException) {
            Log.d(TAG, "onActivityResult: error", e.cause)
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String?) {

        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this.requireActivity()) {
                task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "OAuthActivity - firebaseAuthWithGoogle() success called :$idToken")
                    Log.d(TAG, "OAuthActivity - firebaseAuthWithGoogle() success called")
                } else {
                    Log.d(TAG, "OAuthActivity - firebaseAuthWithGoogle() failed called")
                }
            }
    }
}
