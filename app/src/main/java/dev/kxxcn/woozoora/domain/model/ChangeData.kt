package dev.kxxcn.woozoora.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ChangeData(
    val defaultValue: String?,
    val ordinal: Int?,
) : Parcelable
