package com.sgs.devcamp2.flametalk_android.ui.chatroom

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.sgs.devcamp2.flametalk_android.databinding.FragmentChatRoomBottomSheetBinding
import com.sgs.devcamp2.flametalk_android.util.pathToMultipartImageFile
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

/**
 * @author 김현국
 * @created 2022/01/26
 */
@AndroidEntryPoint
class ChatRoomBottomSheetFragment : DialogFragment(), View.OnClickListener {
    val TAG: String = "로그"
    lateinit var binding: FragmentChatRoomBottomSheetBinding
    lateinit var resultLauncher: ActivityResultLauncher<Intent>
    private val model by activityViewModels<ChatRoomViewModel>()
    lateinit var currentPhotoPath: String
    private val getBackgroundImageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK && result.data?.data != null) {
                val multiPartfile: MultipartBody.Part? = pathToMultipartImageFile(uriToPath(result.data!!.data!!), "image.*".toMediaTypeOrNull())
                if (multiPartfile != null) {
                    model.uploadImage(multiPartfile)
                }
            } else {
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatRoomBottomSheetBinding.inflate(inflater, container, false)
        binding.layoutDialogBottomSheetCamera.setOnClickListener(this)
        binding.layoutDialogBottomSheetAlbum.setOnClickListener(this)
        resultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    val multiPartfile: MultipartBody.Part? = pathToMultipartImageFile(currentPhotoPath, "image.*".toMediaTypeOrNull())
                    if (multiPartfile != null) {
                        model.uploadImage(multiPartfile)
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
            binding.layoutDialogBottomSheetAlbum ->
                {
                    Log.d(TAG, "ChatRoomBottomSheetFragment - onClick() called")
                    getProfileImage(2)
                }
            binding.layoutDialogBottomSheetCamera -> {
                dispatchTakePictureIntent()
            }
        }
    }

    fun dispatchTakePictureIntent() {
        val Intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(requireActivity().packageManager).also {
                val photoFile: File? = try {
                    createImageFile()
                } catch (ex: IOException) {
                    null
                }
                photoFile?.also {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        requireContext(), "com.example.android.provider", it
                    )
                    Log.d(TAG, "photoUri - $photoURI() called")
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                }
            }
        }
        resultLauncher.launch(Intent)
    }
    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File = context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefix */
            ".jpg", /* suffix */
            storageDir /* directory */
        ).apply {
            // Save a file: path for use with ACTION_VIEW intents
            currentPhotoPath = absolutePath
            Log.d(TAG, "currentPhotoPath - $currentPhotoPath")
        }
    }
    // 이미지 변경
    private fun getProfileImage(type: Int) {
        // TODO: SDK 버전에 따라 외장 저장소 접근 로직을 다르게 처리 (Android 10(Q, 29) 이상/미만)
        if (checkPermission()) {
            Log.d(TAG, "ChatRoomBottomSheetFragment - checkPermission() called")
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = MediaStore.Images.Media.CONTENT_TYPE
            intent.type = "image/*"
            intent.putExtra("crop", true)
            intent.putExtra("aspectX", 1)
            intent.putExtra("aspectY", 1)
            intent.putExtra("scale", true)
            when (type) {
                BACKGROUND_IMAGE -> {
                    getBackgroundImageLauncher.launch(intent)
                }
            }
        } else {
            Log.d(TAG, "ChatRoomBottomSheetFragment - requestPermission() called")
            requestPermission()
        }
    }

    private fun checkPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireActivity(),
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }
    // 저장소 접근 권한
    private fun requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        ) {
            Toast.makeText(requireActivity(), "권한이 거부되었습니다. 직접 권한을 허용해야 합니다.", Toast.LENGTH_SHORT)
                .show()
        } else {
            requestPermissions(
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                PERMISSION_CALLBACK_CONSTANT
            )
        }
    }
    // image uri를 path로 전환
    @SuppressLint("Range")
    private fun uriToPath(uri: Uri): String {
        val cursor = requireActivity().contentResolver.query(uri, null, null, null, null)
        cursor?.moveToNext()
        val path: String? = cursor?.getString(cursor.getColumnIndex("_data"))
        cursor?.close()

        if (path.isNullOrEmpty()) {
            return ""
        }
        return path
    }
    companion object {
        private const val BACKGROUND_IMAGE = 2
        private const val PERMISSION_CALLBACK_CONSTANT = 100
    }
}
