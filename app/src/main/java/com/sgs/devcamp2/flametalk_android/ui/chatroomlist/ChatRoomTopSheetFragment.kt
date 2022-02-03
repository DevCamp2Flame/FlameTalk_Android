package com.sgs.devcamp2.flametalk_android.ui.chatroomlist

import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.sgs.devcamp2.flametalk_android.R
import com.sgs.devcamp2.flametalk_android.databinding.FragmentChatRoomListTopSheetBinding

class ChatRoomTopSheetFragment : DialogFragment(), View.OnClickListener {

    lateinit var binding: FragmentChatRoomListTopSheetBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentChatRoomListTopSheetBinding.inflate(inflater, container, false)

        initUI()
        return binding.root
    }

    fun initUI() {
        binding.ivDialogTopSheetNormalChat.setOnClickListener(this)
        binding.ivDialogTopSheetOpenChat.setOnClickListener(this)
    }
    override fun onStart() {
        super.onStart() // 레이아웃 크기 및 위치 조정
        if (Build.VERSION.SDK_INT >= 30) {
            var display = requireContext().display
            val size = Point()
            display?.getRealSize(size)
            val width = size.x
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            dialog?.window?.setLayout(width, height)
            dialog?.window?.setGravity(Gravity.TOP)
            dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.WHITE))
        } else {
            var display = requireActivity().windowManager.defaultDisplay
            val size = Point()
            display?.getRealSize(size)
            val width = size.x
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            dialog?.window?.setLayout(width, height)
            dialog?.window?.setGravity(Gravity.TOP)
            dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.WHITE))
        }
    }

    override fun onClick(view: View?) {
        when (view) {
            binding.ivDialogTopSheetNormalChat ->
                {
                    findNavController().navigate(R.id.navigation_invite_room)
                    this.dismiss()
                }
            binding.ivDialogTopSheetOpenChat ->
                {
                    findNavController().navigate(R.id.navigation_invite_Open_Chat_Room)
                    this.dismiss()
                }
        }
    }
}
