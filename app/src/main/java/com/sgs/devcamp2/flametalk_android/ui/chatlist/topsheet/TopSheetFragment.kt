package com.sgs.devcamp2.flametalk_android.ui.chatlist.topsheet

import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import com.sgs.devcamp2.flametalk_android.databinding.FragmentTopSheetBinding

class TopSheetFragment : DialogFragment() {

    lateinit var binding: FragmentTopSheetBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentTopSheetBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onStart() {
        super.onStart() // 레이아웃 크기 및 위치 조정
        if (Build.VERSION.SDK_INT >= 30) {
            var display = requireContext().display
        } else {
            var display = requireActivity().windowManager.defaultDisplay
        }

        val width = ViewGroup.LayoutParams.MATCH_PARENT
        val height = ViewGroup.LayoutParams.WRAP_CONTENT
        dialog?.window?.setLayout(width, height)
        dialog?.window?.setGravity(Gravity.TOP)
    }
}
