package com.sgs.devcamp2.flametalk_android.util

import android.view.View

// 뷰의 visible 변경
fun View.toVisible() {
    this.visibility = View.VISIBLE
}

fun View.toInvisible() {
    this.visibility = View.INVISIBLE
}

fun View.toVisibleGone() {
    this.visibility = View.GONE
}
