package dev.kxxcn.woozoora.domain.model

import android.os.Parcelable
import dev.kxxcn.woozoora.common.Payment
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TransactionData(
    var id: Int,
    var userId: String,
    var code: String?,
    var description: String?,
    var name: String?,
    val domain: String?,
    var category: Int,
    var payment: Int,
    var price: Int,
    var date: Long,
    var type: Int,
) : Parcelable {

    val paymentNameRes: Int
        get() = Payment.find(payment).nameRes
}
