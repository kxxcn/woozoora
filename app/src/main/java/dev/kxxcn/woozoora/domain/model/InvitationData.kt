package dev.kxxcn.woozoora.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class InvitationData(
    var name: String? = null,
    var id: String? = null,
) : Parcelable {

    val hasInvitation: Boolean
        get() = id?.isNotEmpty() == true

    val code: String
        get() = "sponsor_$id"
}
