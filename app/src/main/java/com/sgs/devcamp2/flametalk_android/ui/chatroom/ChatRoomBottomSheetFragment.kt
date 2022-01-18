package com.sgs.devcamp2.flametalk_android.ui.chatroom

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.sgs.devcamp2.flametalk_android.R
import com.sgs.devcamp2.flametalk_android.databinding.FragmentChatRoomBottomSheetBinding
import com.sgs.devcamp2.flametalk_android.network.response.chat.Chat
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ChatRoomBottomSheetFragment : DialogFragment(), View.OnClickListener {
    val TAG: String = "로그"
    lateinit var binding: FragmentChatRoomBottomSheetBinding
    lateinit var resultLauncher: ActivityResultLauncher<Intent>
    private val model: ChatRoomViewModel by navGraphViewModels(R.id.navigation_chat_room) {
        defaultViewModelProviderFactory
    }
//    private lateinit var model: ChatRoomViewModel

    val REQUEST_IMAGE_CAPTURE = 1
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatRoomBottomSheetBinding.inflate(inflater, container, false)
        binding.layoutDialogBottomSheetCamera.setOnClickListener(this)
        resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val imageBitMap = result.data!!.extras!!.get("data") as Bitmap
                val bundle = bundleOf("image" to imageBitMap)
                Log.d(TAG, "model bottom sheet hashcode : ${model.hashCode()}")
                lifecycleScope.launch {

                    findNavController().previousBackStackEntry?.savedStateHandle?.set("chat", Chat(4, "4", "4", "$imageBitMap"))
                    // model.addChatting()
                }
            }
        }

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        if (Build.VERSION.SDK_INT >= 30) {
            var display = requireContext().display
            val size = Point()
            display?.getRealSize(size)
            val width = size.x
            dialog?.window?.setLayout(width, 900)
            dialog?.window?.setGravity(Gravity.BOTTOM)
            dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.WHITE))
        } else {
            var display = requireActivity().windowManager.defaultDisplay
            val size = Point()
            display?.getRealSize(size)
            val width = size.x
            // val height = ViewGroup.LayoutParams.WRAP_CONTENT
            dialog?.window?.setLayout(width, 900)
            dialog?.window?.setGravity(Gravity.BOTTOM)
            dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.WHITE))
        }
    }

    override fun onClick(view: View?) {
        when (view) {
            binding.layoutDialogBottomSheetAlbum -> {
            }
            binding.layoutDialogBottomSheetCamera -> {

                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                resultLauncher.launch(cameraIntent)
            }
            binding.layoutDialogBottomSheetFile -> {
            }
        }
    }
}
