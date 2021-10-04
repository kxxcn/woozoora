package dev.kxxcn.woozoora.domain.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TimelineData(
    val transactions: List<TransactionData>,
    val range: Pair<Long, Long>? = null,
    val category: String? = null,
) : Parcelable {

    fun inPrice(price: Int): Boolean {
        return price != -1
    }

    fun inRange(timeMs: Long): Boolean {
        return if (range == null) {
            true
        } else {
            timeMs >= range.first && timeMs <= range.second
        }
    }

    fun inCategory(domain: String?): Boolean {
        return if (category == null) {
            true
        } else {
            category == domain
        }
    }
}
