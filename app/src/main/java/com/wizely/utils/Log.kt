package com.wizely.utils

import android.content.Context
import android.util.Log
import android.widget.Toast

/**
 * This file has simple Logging methods for different log levels
 */

val TAG = "taaag"

fun logi(str: String?) {
    Log.i(TAG, str)
}

fun logd(str: String?) {
    Log.d(TAG, str)
}

fun logv(str: String?) {
    Log.v(TAG, str)
}

fun loge(str: String?) {
    Log.e(TAG, str)
}

fun showToast(context: Context, str: String?) {
    Toast.makeText(context, str, Toast.LENGTH_SHORT).show()
}