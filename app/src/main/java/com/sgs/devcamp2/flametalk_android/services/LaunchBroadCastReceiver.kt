package com.sgs.devcamp2.flametalk_android.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging

/**
 * @author boris
 * @created 2022/01/24
 */
class LaunchBroadCastReceiver : BroadcastReceiver() {
    val TAG: String = "로그"
    override fun onReceive(context: Context?, intent: Intent?) {

        // main activity가 켜지면 devicetoken sharedprefence에 저장

        FirebaseMessaging.getInstance().token.addOnCompleteListener(
            OnCompleteListener {
                task ->
                if (!task.isSuccessful) {
                    return@OnCompleteListener
                }
                val token = task.result
                val pref = context?.getSharedPreferences("deviceToken", Context.MODE_PRIVATE)
                val editor = pref?.edit()
                editor?.putString("deviceToken", token)?.apply()
                editor?.commit()
            }
        )
    }
}
