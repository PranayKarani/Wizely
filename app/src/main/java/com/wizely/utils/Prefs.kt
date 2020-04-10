package com.wizely.utils

import android.content.Context
import com.wizely.MyApp


fun writeToPrefs(key: String, value: String) = getPrefs().edit().putString(key, value).commit()

fun readFromPrefs(key: String) = getPrefs().getString(key, null)

//fun read(key: String) = getPrefs().getInt(key, -1)

fun getPrefs() = MyApp.get().getSharedPreferences("prefs", Context.MODE_PRIVATE)
