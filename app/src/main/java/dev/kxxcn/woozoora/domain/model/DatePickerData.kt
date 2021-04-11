package dev.kxxcn.woozoora.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DatePickerData(
    val nameRes: Int,
    val timeMs: Long,
) : Parcelable
