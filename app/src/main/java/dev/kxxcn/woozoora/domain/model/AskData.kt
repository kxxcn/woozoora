package dev.kxxcn.woozoora.domain.model

import android.os.Build
import dev.kxxcn.woozoora.BuildConfig
import java.util.*

data class AskData(
    val message: String,
    val userId: String = "",
    val version: String = BuildConfig.VERSION_NAME,
    val device: String = "${Build.MANUFACTURER}-${Build.DEVICE}".toUpperCase(Locale.getDefault()),
    val os: Int = Build.VERSION.SDK_INT,
    val date: Long = System.currentTimeMillis(),
)
