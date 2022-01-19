package com.sgs.devcamp2.flametalk_android.util

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

/**
 * @author boris
 * @created 2022/01/18
 */
fun EditText.onTextChanged(completion: (Editable?) -> Unit) {
    val TAG: String = "로그"
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun afterTextChanged(p0: Editable?) {
            completion(p0)
        }
    })
}
