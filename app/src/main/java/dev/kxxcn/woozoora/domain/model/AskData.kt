package dev.kxxcn.woozoora.domain.model

import android.os.Build
import dev.kxxcn.woozoora.BuildConfig
import dev.kxxcn.woozoora.common.FORMAT_DATE_YEAR_DOT_MONTH_DOT_DAY
import dev.kxxcn.woozoora.util.Converter
import java.util.*

data class AskData(
    val userId: String = "",
    val message: String,
    val version: String = BuildConfig.VERSION_NAME,
    val device: String = "${Build.MANUFACTURER}-${Build.DEVICE}".toUpperCase(Locale.getDefault()),
    val os: Int = Build.VERSION.SDK_INT,
    val date: Long = System.currentTimeMillis(),
    val reply: ReplyData? = null,
) {

    val hasReply: Boolean
        get() = reply != null

    val dateText: String?
        get() = Converter.dateFormat(FORMAT_DATE_YEAR_DOT_MONTH_DOT_DAY, date)
}
