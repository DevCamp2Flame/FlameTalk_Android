package com.sgs.devcamp2.flametalk_android.util

import android.view.View
import android.widget.LinearLayout
import com.google.android.material.tabs.TabLayout

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

fun swapViewVisibility(view1: View, view2: View) {
    view1.visibility = View.INVISIBLE
    view2.visibility = View.VISIBLE
}

fun View.disableClickTemporarily() {
    isClickable = false
    postDelayed({
        isClickable = true
    }, 500)
}

fun TabLayout.setTabWidthAsWrapContent(tabPosition: Int) {
    val layout = (this.getChildAt(0) as LinearLayout).getChildAt(tabPosition) as LinearLayout
    val layoutParams = layout.layoutParams as LinearLayout.LayoutParams
    layoutParams.weight = 0f
    layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT
    layout.layoutParams = layoutParams
}
