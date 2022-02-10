package com.sgs.devcamp2.flametalk_android.ui

import android.content.BroadcastReceiver
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.sgs.devcamp2.flametalk_android.R
import com.sgs.devcamp2.flametalk_android.databinding.ActivityMainBinding
import com.sgs.devcamp2.flametalk_android.services.LaunchBroadCastReceiver
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        boardCastIntent()
        initNavigationBar()
    }

    private fun initNavigationBar() {
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fcv_main) as NavHostFragment
        navController = navHostFragment.navController
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.navigation_signin || destination.id == R.id.navigation_signup ||
                destination.id == R.id.navigation_chat_room || destination.id == R.id.navigation_profile ||
                destination.id == R.id.navigation_profile_desc || destination.id == R.id.navigation_edit_profile ||
                destination.id == R.id.navigation_chat_Room_Bottom_Sheet || destination.id == R.id.navigation_add_profile ||
                destination.id == R.id.navigation_single_feed || destination.id == R.id.navigation_total_feed ||
                destination.id == R.id.navigation_blocked_friend || destination.id == R.id.navigation_hidden_friend ||
                destination.id == R.id.navigation_add_friend
            ) {
                binding.btnvView.visibility = View.GONE
            } else {
                binding.btnvView.visibility = View.VISIBLE
            }
        }

        binding.btnvView.setupWithNavController(navController)
        binding.btnvView.itemIconTintList = null
    }

    fun boardCastIntent() {
        val br: BroadcastReceiver = LaunchBroadCastReceiver()
        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION).apply {
            addAction("com.course.APP_LAUNCH")
        }
        registerReceiver(br, filter)
    }
}
