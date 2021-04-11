package dev.kxxcn.woozoora.domain.model

import android.os.Parcelable
import dev.kxxcn.woozoora.common.FORMAT_DATE_YEAR_DOT_MONTH_DOT_DAY
import dev.kxxcn.woozoora.common.FORMAT_TIME_HOUR_MINUTE_WITH_MARKER
import dev.kxxcn.woozoora.util.Converter
import kotlinx.android.parcel.Parcelize

@Parcelize
data class HistoryData(
    val transaction: TransactionData?,
    val user: UserData,
    val group: List<UserData>,
    val isNew: Boolean = false,
) : Parcelable {

    val hasMemo: Boolean
        get() = transaction?.description?.isNotEmpty() == true

    val isEditable: Boolean
        get() = isHost && !isNew

    val host: UserData?
        get() = if (isHost) {
            user
        } else {
            group.find(predicate)
        }

    val profile: String?
        get() = host?.profile

    val date: String?
        get() {
            val timeMs = transaction?.date
            return Converter.dateFormat(FORMAT_DATE_YEAR_DOT_MONTH_DOT_DAY, timeMs)
        }

    val time: String?
        get() {
            val timeMs = transaction?.date
            return Converter.dateFormat(FORMAT_TIME_HOUR_MINUTE_WITH_MARKER, timeMs)
        }

    private val predicate = { user: UserData -> user.id == transaction?.userId }

    private val isHost: Boolean
        get() = predicate.invoke(user)

    override fun equals(other: Any?): Boolean {
        return if (other is HistoryData) {
            transaction?.id == other.transaction?.id
        } else {
            false
        }
    }

    override fun hashCode(): Int {
        return (transaction?.hashCode() ?: 0) * 31 + isNew.hashCode() * 31
    }
}
