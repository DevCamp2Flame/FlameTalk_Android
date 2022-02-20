package com.sgs.devcamp2.flametalk_android.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.app.TaskStackBuilder
import android.content.Context
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.navigation.NavDeepLinkBuilder
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.sgs.devcamp2.flametalk_android.R
import java.io.UnsupportedEncodingException
import java.net.URLDecoder

/**
 * @author 김현국
 * @created 2022/01/23
 */
class FcmService : FirebaseMessagingService() {
    val TAG: String = "로그"

    override fun onNewToken(token: String) {
        Log.d(TAG, "new Token: $token")

        // 토큰 값을 따로 저장해둔다.
        val pref = this.getSharedPreferences("deviceToken", Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.putString("deviceToken", token).apply()
        editor.commit()

        Log.i("로그: ", "성공적으로 토큰을 저장함")
    }
    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        Log.d(TAG,"remoteMessage - ${remoteMessage.data["title"]}() called")
        if (remoteMessage.data.isNotEmpty()) {
            sendNotification(remoteMessage)
        } else {
            Log.i("수신에러: ", "data가 비어있습니다. 메시지를 수신하지 못했습니다.")
            Log.i("data값: ", remoteMessage.data.toString())
        }
    }

    private fun sendNotification(remoteMessage: RemoteMessage) {

        val worker = OneTimeWorkRequest.Builder(FcmUpdateWorker::class.java)
        val data = Data.Builder()

        var KEY_TALK_GROUP = "FlameTalk"
        var title = ""
        var body = ""
        var roomId = ""
        try {
            title = URLDecoder.decode(remoteMessage.data["title"], "UTF-8")
            body = URLDecoder.decode(remoteMessage.data["body"], "UTF-8")
            roomId = remoteMessage.data["room"].toString()
            data.putString("text", body)
            data.putString("room", roomId)
        } catch (e: UnsupportedEncodingException) {
        }
        worker.setInputData(data.build())
        WorkManager.getInstance(applicationContext).enqueue(worker.build())

        Log.d(TAG, "room - ${remoteMessage.data["room"]}() called")

        // RequestCode, Id를 고유값으로 지정하여 알림이 개별 표시되도록 함
        val uniId: Int = (System.currentTimeMillis() / 7).toInt()

        // 일회용 PendingIntent
        var bundle: Bundle = Bundle()
        bundle.putString("chatroomId", remoteMessage.data["room"])
        var builder: androidx.core.app.TaskStackBuilder =
            NavDeepLinkBuilder(applicationContext)
                .setGraph(R.navigation.main_navigation)
                .setDestination(R.id.navigation_chat_room)
                .setArguments(bundle).createTaskStackBuilder()
        var pendingIntent: PendingIntent = builder.getPendingIntent(bundle.hashCode(), FLAG_IMMUTABLE)!!
        // 알림 채널 이름
        val channelId = "channel" // getString(R.string.firebase_notification_channel_id)

        // 알림 소리
        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        fun getSummaryNoti(context: Context): NotificationCompat.Builder {
            return NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setOnlyAlertOnce(true)
                .setGroup(KEY_TALK_GROUP)
                .setGroupSummary(true)
        }

        // 알림에 대한 UI 정보와 작업을 지정한다.
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.ic_launcher) // 아이콘 설정
            .setContentText(body) // 메시지 내용
            .setContentTitle(title) // 제목
            .setPriority(NotificationCompat.PRIORITY_MAX) // Head up display
            .setOnlyAlertOnce(true)
            .setGroup(KEY_TALK_GROUP)
            .setAutoCancel(true)
            .setSound(soundUri)
            .setContentIntent(pendingIntent) // 알림 실행 시 Intent

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // 오레오 버전 이후에는 채널이 필요하다.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Notice", NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
        }

        // 알림 생성
        notificationManager.notify(uniId, notificationBuilder.build())
        notificationManager.notify(1234, getSummaryNoti(this).build())
    }
}
