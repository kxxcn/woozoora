package dev.kxxcn.woozoora.common.extension

import com.google.gson.Gson

inline fun <reified T> T.toJson(): String {
    return Gson().toJson(this)
}

inline fun <reified T> String.fromJson(): T {
    return Gson().fromJson(this, T::class.java)
}
