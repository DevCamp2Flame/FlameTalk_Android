package com.sgs.devcamp2.flametalk_android.ui.chatroom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.sgs.devcamp2.flametalk_android.R

class ChatRoomCameraFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_chat_room_camera, container, false)
    }
}
