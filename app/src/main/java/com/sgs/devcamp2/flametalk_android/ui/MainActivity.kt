package com.sgs.devcamp2.flametalk_android.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.sgs.devcamp2.flametalk_android.R
import com.sgs.devcamp2.flametalk_android.databinding.ActivityMainBinding
import com.sgs.devcamp2.flametalk_android.domain.entity.UiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController
    private val model by viewModels<MainViewModel>()
    val TAG: String = "로그"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initNavigationBar()
//        model.connectChatServer()
//        initReceiveMessage()
//        model.getDeviceToken(this)
    }

    private fun initNavigationBar() {
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fcv_main) as NavHostFragment
        navController = navHostFragment.navController
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.navigation_splash || destination.id == R.id.navigation_signin || destination.id == R.id.navigation_signup ||
                destination.id == R.id.navigation_chat_room || destination.id == R.id.navigation_profile ||
                destination.id == R.id.navigation_profile_desc || destination.id == R.id.navigation_edit_profile ||
                destination.id == R.id.navigation_create_chat_room ||
                destination.id == R.id.navigation_chat_Room_Bottom_Sheet || destination.id == R.id.navigation_add_profile ||
                destination.id == R.id.navigation_single_feed || destination.id == R.id.navigation_total_feed ||
                destination.id == R.id.navigation_blocked_friend || destination.id == R.id.navigation_hidden_friend ||
                destination.id == R.id.navigation_add_friend || destination.id == R.id.navigation_search
            ) {
                binding.btnvView.visibility = View.GONE
            } else {
                binding.btnvView.visibility = View.VISIBLE
            }
        }

        binding.btnvView.setupWithNavController(navController)
        binding.btnvView.itemIconTintList = null
    }

    private fun initReceiveMessage() {
        lifecycleScope.launch {
            model.session.collect { state ->
                when (state) {
                    is UiState.Success -> {
                        Log.d(TAG, "MainActivity - CONNECT WEBSOCKET")
                    }
                    is UiState.Loading -> {
                        Log.d(TAG, "MainActivity - initReceiveMessage() called")
                    }
                    is UiState.Error -> {
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}
