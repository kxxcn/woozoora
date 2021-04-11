package dev.kxxcn.woozoora.common.extension

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.annotation.RequiresApi

@RequiresApi(Build.VERSION_CODES.O)
fun NotificationChannel.isEnabled(): Boolean {
    return importance > NotificationManager.IMPORTANCE_NONE
}
