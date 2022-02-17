package com.sgs.devcamp2.flametalk_android.ui.signup

import android.app.DatePickerDialog
import android.os.Bundle
import android.provider.Settings
import android.telephony.PhoneNumberFormattingTextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.auth0.android.jwt.JWT
import com.sgs.devcamp2.flametalk_android.databinding.FragmentAdditionalUserInfoBinding
import com.sgs.devcamp2.flametalk_android.domain.entity.UiState
import com.sgs.devcamp2.flametalk_android.util.onTextChanged
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

/**
 * A simple [Fragment] subclass.
 * Use the [AdditionalUserInfoFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class AdditionalUserInfoFragment : Fragment(), View.OnClickListener {

    lateinit var binding: FragmentAdditionalUserInfoBinding
    private val model by viewModels<AdditionalUserInfoViewModel>()
    private val args by navArgs<AdditionalUserInfoFragmentArgs>()
    val TAG: String = "로그"
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAdditionalUserInfoBinding.inflate(inflater, container, false)
        initUI()
        initObserve()
        val jwt: JWT = JWT(args.token!!)
        val s = jwt.getClaim("email").asString()
        Log.d(TAG, "email - $s")
        model.updateEmail(s!!)

        return binding.root
    }

    fun initUI() {
        binding.imgSignupBack.setOnClickListener(this)
        binding.imgSignupBack.setOnClickListener(this)
        binding.btnSignupConfirm.setOnClickListener(this)

        binding.edtSignupName.onTextChanged {
            model.updateName(it.toString())
        }

        binding.edtSignupTel.addTextChangedListener(PhoneNumberFormattingTextWatcher())
        binding.edtSignupTel.onTextChanged {
            model.updateTel(it.toString())
        }

        binding.edtSignupPassword.onTextChanged {
            model.updatePassword(it.toString())
        }
        binding.edtSignupBirth.setOnClickListener(this)
    }
    fun initObserve() {
        viewLifecycleOwner.lifecycleScope.launch {
            model.signUpUiState.collect {
                state ->
                when (state) {
                    is UiState.Success ->
                        {
                            findNavController().popBackStack()
                        }
                }
            }
        }
    }
    override fun onClick(view: View?) {
        when (view) {
            binding.btnSignupConfirm ->
                {
                    model.submitAdditionalUserData(
                        Settings.Secure.getString(
                            requireContext().contentResolver,
                            Settings.Secure.ANDROID_ID
                        )
                    )
                }
            binding.imgSignupBack ->
                {
                }
            binding.imgSignupProfile ->
                {
                }
            binding.edtSignupBirth ->
                {
                    var formatDate = SimpleDateFormat("YYYY.MM.DD")
                    val getDate: Calendar = Calendar.getInstance()
                    val datePicker = DatePickerDialog(
                        this.requireContext(),
                        DatePickerDialog.OnDateSetListener { datePicker, i, i2, i3 ->
                            val selectDate: Calendar = Calendar.getInstance()
                            selectDate.set(Calendar.YEAR, i)
                            selectDate.set(Calendar.MONTH, i2)
                            selectDate.set(Calendar.DAY_OF_MONTH, i3)
                            val date = formatDate.format(selectDate.time)
                            model.updateBirth(date)
                            binding.edtSignupBirth.setText(date)
                        },
                        getDate.get(Calendar.YEAR), getDate.get(Calendar.MONDAY), getDate.get(Calendar.DAY_OF_MONTH)
                    )
                    datePicker.show()
                }
        }
    }
}
