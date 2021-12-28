package com.sgs.devcamp2.flametalk_android.ui.oauth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sgs.devcamp2.flametalk_android.R
import com.sgs.devcamp2.flametalk_android.databinding.ActivityOauthBinding

class OAuthActivity : AppCompatActivity() {
    lateinit var binding: ActivityOauthBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOauthBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}
