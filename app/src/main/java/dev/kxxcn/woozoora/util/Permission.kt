package dev.kxxcn.woozoora.util

import android.app.Activity
import androidx.core.app.ActivityCompat

object Permission {

    fun request(
        activity: Activity,
        requestCode: Int,
        vararg permissions: String,
    ) {
        ActivityCompat.requestPermissions(
            activity,
            permissions,
            requestCode
        )
    }
}
