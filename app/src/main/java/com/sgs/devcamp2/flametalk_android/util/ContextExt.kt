package com.sgs.devcamp2.flametalk_android.util

import android.content.Context
import android.widget.Toast

/**
 * @author 김현국
 * @created 2022/01/30
 */

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}
