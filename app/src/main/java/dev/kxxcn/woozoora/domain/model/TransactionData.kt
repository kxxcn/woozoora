package dev.kxxcn.woozoora.domain.model

import android.os.Parcelable
import dev.kxxcn.woozoora.common.Category
import dev.kxxcn.woozoora.common.Payment
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TransactionData(
    var id: Int,
    var userId: String,
    var code: String?,
    var description: String?,
    var name: String?,
    var category: Int,
    var payment: Int,
    var price: Int,
    var date: Long,
) : Parcelable {

    val categoryNameRes: Int
        get() = Category.find(category).nameRes

    val categoryIconRes: Int
        get() = Category.find(category).iconRes

    val categoryColorRes: Int
        get() = Category.find(category).colorRes

    val paymentNameRes: Int
        get() = Payment.find(payment).nameRes
}
