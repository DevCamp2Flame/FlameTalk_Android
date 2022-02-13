package com.sgs.devcamp2.flametalk_android.ui.friend.phonebook

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import com.sgs.devcamp2.flametalk_android.databinding.FragmentPhoneBookBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import timber.log.Timber

/**
 * @author 박소연
 * @created 2022/02/01
 * @updated 2022/02/01
 * @desc 주소록 연락처 불러오기를 위한 임시 Fragment
 */

@AndroidEntryPoint
@ExperimentalCoroutinesApi
class PhoneBookFragment : Fragment() {
    private val binding by lazy { FragmentPhoneBookBinding.inflate(layoutInflater) }
    private val viewModel: PhoneBookViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initUI()
        return binding.root
    }

    private fun initUI() {
        binding.btnPhoneBook.setOnClickListener {
            if (checkPermission()) {
                // 권한이 있으면 연락처 리스트를 불러온다
                val contacts = getContactList()
                viewModel.getContacts(contacts)
            } else {
                Snackbar.make(
                    requireView(), "권한이 없으면 연락처 동기화로 친구추가할 수 없습니다.", Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }

    // 연락처 가져오기
    private fun getContactList(): ArrayList<String> {
        var arrayList = ArrayList<String>()
        val uri = ContactsContract.Contacts.CONTENT_URI
        val sort = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
        val cursor: Cursor? = requireContext().contentResolver.query(
            uri, null, null, null, sort
        )
        if (cursor != null) {
            if (cursor.count > 0) {
                while (cursor.moveToNext()) {
                    @SuppressLint("Range") val id = cursor.getString(
                        cursor.getColumnIndex(
                            ContactsContract.Contacts._ID
                        )
                    )
                    @SuppressLint("Range") val name = cursor.getString(
                        cursor.getColumnIndex(
                            ContactsContract.Contacts.DISPLAY_NAME
                        )
                    )
                    val uriPhone = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
                    val selection = ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " =?"
                    val phoneCursor: Cursor? = requireContext().contentResolver.query(
                        uriPhone, null, selection, arrayOf(id), null
                    )
                    if (phoneCursor!!.moveToNext()) {
                        @SuppressLint("Range") val number = phoneCursor.getString(
                            phoneCursor.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER
                            )
                        )
                        arrayList.add(number)
                        phoneCursor.close()
                    }
                }
                cursor.close()
            }
        }
        Timber.d("연락처 %s", arrayList)
        return arrayList
    }

    // 주소록 접근 권한 확인
    private fun checkPermission(): Boolean {
        val status = ContextCompat.checkSelfPermission(
            requireContext(),
            "android.permission.READ_CONTACTS"
        )
        // 권한 확인
        return if (status == PackageManager.PERMISSION_GRANTED) {
            true
        } else { // 권한 요청 다이얼로그
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf("android.permission.READ_CONTACTS"),
                100
            )
            Timber.d("Contacts Permission Denied")
            false
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Timber.d("Contacts Permission Grants")
        } else {
            Timber.d("Contacts Permission Denied")
        }
    }
}
