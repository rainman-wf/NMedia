package ru.netology.nmedia.common.utils
import timber.log.Timber

import android.content.Context
import android.text.format.DateFormat
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast

fun formatDate(unixValue: Long): String {
    return DateFormat.format("dd MMM yyyy в HH:mm", unixValue * 1000).toString()
}

fun Int.asUnit (): String{
    if (this < 1000) return this.toString()
    val unit = if (this < 1_000_000) "K" else "M"
    val majDiv = if (this < 1_000_000) 1_000 else 1_000_000
    val minDiv = majDiv /10
    val major = this / majDiv
    val minor = this % majDiv / minDiv
    return "$major${if (major >= 10 || minor == 0) "" else ".$minor"}$unit"
}

fun View.hideKeyboard() {
    val imm = this.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(this.windowToken, 0)
}

fun notifyEmptyMessage(view: View) {
    Toast.makeText(view.context, "Content can't be empty", Toast.LENGTH_SHORT).show()
}

fun Any.log(msg: Any?) = Timber.d("CODE34" + this.javaClass.simpleName + " " + msg.toString())
