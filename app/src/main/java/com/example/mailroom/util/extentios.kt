package com.example.mailroom.util

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment

fun Fragment.dismissKeyboard() {
    val imm: InputMethodManager? = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    activity?.currentFocus?.windowToken?.let {
        imm?.hideSoftInputFromWindow(it, 0)
    }
}


fun View.fadeShow(){
    this.apply {
        alpha=0f
        visibility= View.VISIBLE
        animate().alpha(1f)
            .setDuration(300)
    }
}
fun View.fadeHide(){
    this.animate()
        .alpha(0f)
        .setDuration(300)
        .setListener(object: AnimatorListenerAdapter(){
            override fun onAnimationEnd(animation: Animator?) {
                this@fadeHide.visibility= View.GONE
            }
        })
}