package dev.kxxcn.woozoora.common.extension

import android.content.SharedPreferences

inline fun <reified T> SharedPreferences.put(key: String, value: T) {
    when (value) {
        is String -> edit().putString(key, value).apply()
        is Int -> edit().putInt(key, value).apply()
        is Boolean -> edit().putBoolean(key, value).apply()
    }
}
